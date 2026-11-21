package br.com.docodigoaocontrato.taskforge.service;

import br.com.docodigoaocontrato.taskforge.dto.ComentarioDTO;
import br.com.docodigoaocontrato.taskforge.dto.TarefaDTO;
import br.com.docodigoaocontrato.taskforge.model.Comentario;
import br.com.docodigoaocontrato.taskforge.model.Tarefa;
import br.com.docodigoaocontrato.taskforge.repository.TarefaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @InjectMocks
    private TarefaService tarefaService;

    @Test
    void deveDevolverUmaTarefaComComentarioQuandoOIdExiste() {
        // DADO que o banco "tem" a tarefa 1L
        Comentario comentario = new Comentario(2L, "Estudar", "Junior");
        List<Comentario> comentarios = new ArrayList<>();
        comentarios.add(comentario);
        Tarefa tarefa = new Tarefa(1L, "Estudar", 1, false, comentarios);
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));

        // QUANDO eu busco a tarefa
        Optional<TarefaDTO> tarefaEncontrada = tarefaService.buscarTarefaComComentarios(1L);

        // ENTAO a tarefa é recuperada E RETORNA UM COMENTÁRIO
        assertTrue(tarefaEncontrada.isPresent());
        ComentarioDTO comentarioEncontrado = tarefaEncontrada.get().getComentarios().get(0);
        assertEquals("Estudar", comentarioEncontrado.getDescricao());
    }

    @Test
    void deveDevolverVazioQuandoOIdNaoExiste() {
        // DADO que o banco nao "tem" a tarefa 99
        when(tarefaRepository.findById(99L)).thenReturn(Optional.empty());

        // QUANDO eu busco a tarefa 99
        Optional<TarefaDTO> resultado = tarefaService.buscarTarefaPorId(99L);

        // ENTAO nao vem nenhum resultado
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveDevelverAprioridadeEditada(){
        // DADO que o banco tem uma tarefa para editar
        Tarefa tarefa = new Tarefa(1L, "Estudar", 1, false, new ArrayList<>());
        TarefaDTO tarefaNova = new TarefaDTO(1L, "Estudar", 5, false, LocalDateTime.now());
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.save(any(Tarefa.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // QUANDO eu edito a tarefa
        Optional<TarefaDTO> tarefaAtualizada = tarefaService.atualizarTarefa(1L, tarefaNova);

        // ENTAO tarefa muda o valor da prioridade
        assertTrue(tarefaAtualizada.isPresent());
        assertEquals(5, tarefaAtualizada.get().getPrioridade());
    }

    @Test
    void deveBuscarTarefasConcluidasQuandoFiltroForTrue() {
        Tarefa tarefaConcluida = new Tarefa(1L, "Estudar", 1, true, new ArrayList<>());
        when(tarefaRepository.findByConcluida(true))
                .thenReturn(List.of(tarefaConcluida));

        List<TarefaDTO> resultado = tarefaService.buscarTodos(true);

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getConcluida());
        verify(tarefaRepository).findByConcluida(true);
    }

    @Test
    void deveBuscarTarefasPendentesQuandoFiltroForFalse() {
        Tarefa tarefaPendente = new Tarefa(2L, "Fazer exercício", 2, false, new ArrayList<>());
        when(tarefaRepository.findByConcluida(false))
                .thenReturn(List.of(tarefaPendente));

        List<TarefaDTO> resultado = tarefaService.buscarTodos(false);

        assertEquals(1, resultado.size());
        assertFalse(resultado.get(0).getConcluida());
        verify(tarefaRepository).findByConcluida(false);
    }

    @Test
    void deveApagarTarefaConformeImplementacaoAtualDoService() {
        boolean apagada = tarefaService.deletarTarefa(99L);

        assertTrue(apagada);
        verify(tarefaRepository).deleteById(99L);
    }
}