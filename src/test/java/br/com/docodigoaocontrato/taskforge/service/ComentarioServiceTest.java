package br.com.docodigoaocontrato.taskforge.service;

import br.com.docodigoaocontrato.taskforge.dto.ComentarioDTO;
import br.com.docodigoaocontrato.taskforge.model.Comentario;
import br.com.docodigoaocontrato.taskforge.repository.ComentarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ComentarioServiceTest {

    @Mock
    private ComentarioRepository comentarioRepository;

    @InjectMocks
    private ComentarioService comentarioService;

    @Test
    void deveAtualizarDescricaoEAutorDoComentario() {
        Comentario comentarioExistente = new Comentario(1L, "Texto antigo", "Ana");
        ComentarioDTO comentarioAtualizadoDTO = new ComentarioDTO(1L, "Texto novo", "Bruno");

        when(comentarioRepository.findById(1L)).thenReturn(Optional.of(comentarioExistente));
        when(comentarioRepository.save(any(Comentario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Optional<ComentarioDTO> resultado = comentarioService.atualizarComentario(1L, comentarioAtualizadoDTO);

        assertTrue(resultado.isPresent());
        assertEquals("Texto novo", resultado.get().getDescricao());
        assertEquals("Bruno", resultado.get().getAutor());
    }

    @Test
    void deveRetornarVazioAoAtualizarComentarioInexistente() {
        ComentarioDTO comentarioDTO = new ComentarioDTO(99L, "Texto", "Bruno");
        when(comentarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<ComentarioDTO> resultado = comentarioService.atualizarComentario(99L, comentarioDTO);

        assertTrue(resultado.isEmpty());
    }
}
