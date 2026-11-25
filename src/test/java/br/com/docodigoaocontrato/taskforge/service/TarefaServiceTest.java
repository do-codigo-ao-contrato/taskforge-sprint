package br.com.docodigoaocontrato.taskforge.service;

import br.com.docodigoaocontrato.taskforge.dto.TarefaDTO;
import br.com.docodigoaocontrato.taskforge.model.Tarefa;
import br.com.docodigoaocontrato.taskforge.repository.TarefaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @InjectMocks
    private TarefaService tarefaService;

    @Test
    void deveDevolverUmaTarefaQuandoOIdExiste() {
        Tarefa tarefa = new Tarefa(1L, "Estudar", 1, false);
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));

        Optional<TarefaDTO> resultado = tarefaService.buscarTarefaPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Estudar", resultado.get().getNome());
        assertEquals(1, resultado.get().getPrioridade());
    }

    @Test
    void deveDevolverVazioQuandoOIdNaoExiste() {
        when(tarefaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<TarefaDTO> resultado = tarefaService.buscarTarefaPorId(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveDevelverAprioridadeEditada(){
        Tarefa tarefa = new Tarefa(1L, "Estudar", 1, false);
        TarefaDTO tarefaNova = new TarefaDTO(1L, "Estudar", 5, false, LocalDate.now(), LocalDateTime.now());

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.save(any(Tarefa.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Optional<TarefaDTO> tarefaAtualizada = tarefaService.atualizarTarefa(1L, tarefaNova);

        assertTrue(tarefaAtualizada.isPresent());
        assertEquals(5, tarefaAtualizada.get().getPrioridade());
    }

    @Test
    void deveBuscarTarefasConcluidasQuandoFiltroForTrue() {
        Tarefa tarefaConcluida = new Tarefa(1L, "Estudar", 1, true);

        when(tarefaRepository.findByConcluida(true, Sort.unsorted()))
                .thenReturn(List.of(tarefaConcluida));

        List<TarefaDTO> resultado = tarefaService.buscarTodos(true, null);

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getConcluida());
        verify(tarefaRepository).findByConcluida(true, Sort.unsorted());
    }

    @Test
    void deveBuscarTarefasPendentesQuandoFiltroForFalse() {
        Tarefa tarefaPendente = new Tarefa(2L, "Fazer exercício", 2, false);

        when(tarefaRepository.findByConcluida(false, Sort.unsorted()))
                .thenReturn(List.of(tarefaPendente));

        List<TarefaDTO> resultado = tarefaService.buscarTodos(false, null);

        assertEquals(1, resultado.size());
        assertFalse(resultado.get(0).getConcluida());
        verify(tarefaRepository).findByConcluida(false, Sort.unsorted());
    }

    @Test
    void deveApagarTarefaConformeImplementacaoAtualDoService() {
        when(tarefaRepository.existsById(99L)).thenReturn(true);

        boolean apagada = tarefaService.deletarTarefa(99L);

        assertTrue(apagada);
        verify(tarefaRepository).deleteById(99L);
    }
}