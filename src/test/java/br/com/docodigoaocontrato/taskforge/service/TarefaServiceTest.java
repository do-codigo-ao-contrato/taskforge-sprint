package br.com.docodigoaocontrato.taskforge.service;

import br.com.docodigoaocontrato.taskforge.dto.TarefaDTO;
import br.com.docodigoaocontrato.taskforge.dto.TarefaEstatisticasDTO;
import br.com.docodigoaocontrato.taskforge.model.Tarefa;
import br.com.docodigoaocontrato.taskforge.repository.TarefaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
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
    void deveDevelverAprioridadeEditada() {
        // DADO que o banco tem uma tarefa para editar
        Tarefa tarefa = new Tarefa(1L, "Estudar", 1, false);
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
        Tarefa tarefaConcluida = new Tarefa(1L, "Estudar", 1, true);
        when(tarefaRepository.findByConcluida(true, null))
                .thenReturn(new PageImpl<>(List.of(tarefaConcluida)));

        Page<TarefaDTO> resultado = tarefaService.buscarTodos(true, null, null);

        assertEquals(1, resultado.getContent().size());
        assertTrue(resultado.getContent().get(0).getConcluida());
        verify(tarefaRepository).findByConcluida(true, null);
    }

    @Test
    void deveBuscarTarefasPendentesQuandoFiltroForFalse() {
        Tarefa tarefaPendente = new Tarefa(2L, "Fazer exercício", 2, false);
        when(tarefaRepository.findByConcluida(false, null))
                .thenReturn(new PageImpl<>(List.of(tarefaPendente)));

        Page<TarefaDTO> resultado = tarefaService.buscarTodos(false, null, null);

        assertEquals(1, resultado.getContent().size());
        assertFalse(resultado.getContent().get(0).getConcluida());
        verify(tarefaRepository).findByConcluida(false, null);
    }

    @Test
    void deveObterEstatisticasDasTarefas() {
        when(tarefaRepository.count()).thenReturn(5L);
        when(tarefaRepository.countByConcluida(true)).thenReturn(3L);
        when(tarefaRepository.countByConcluida(false)).thenReturn(2L);

        TarefaEstatisticasDTO estatisticas = tarefaService.obterEstatisticas();

        assertEquals(5L, estatisticas.getTotal());
        assertEquals(3L, estatisticas.getConcluidas());
        assertEquals(2L, estatisticas.getPendentes());

        verify(tarefaRepository, times(1)).count();
        verify(tarefaRepository, times(1)).countByConcluida(true);
        verify(tarefaRepository, times(1)).countByConcluida(false);
    }

    @Test
    void deveApagarTarefaConformeImplementacaoAtualDoService() {
        when(tarefaRepository.existsById(99L)).thenReturn(true);

        boolean apagada = tarefaService.deletarTarefa(99L);

        assertTrue(apagada);
        verify(tarefaRepository).deleteById(99L);
    }

    @Test
    void deveBuscarTarefasPorNome() {
        Tarefa tarefaRepositorio = new Tarefa(2L, "estudar", 2, false);
        when(tarefaRepository.findByNomeContainingIgnoreCase("estudar", null))
                .thenReturn(new PageImpl<>(List.of(tarefaRepositorio)));

        Page<TarefaDTO> tarefaBuscada = tarefaService.buscarTodos(null, "estudar", Pageable.unpaged());

        assertEquals(2L, tarefaBuscada.getContent().get(0).getId());
        assertEquals("estudar", tarefaBuscada.getContent().get(0).getNome());
    }

    @Test
    void deveDevolverAListaDeTarefasPorPagina() {
        Tarefa tarefaRepositorio1 = new Tarefa(2L, "estudar", 2, false);
        Tarefa tarefaRepositorio2 = new Tarefa(3L, "Descansar", 3, true);
        Pageable paginas = PageRequest.of(0, 10);

        when(tarefaRepository.findAll(paginas)).thenReturn(new PageImpl<>(List.of(tarefaRepositorio1, tarefaRepositorio2)));

        Page<TarefaDTO> tarefaBuscada = tarefaService.buscarTodos(null, null, paginas);

        verify(tarefaRepository).findAll(paginas);
        assertEquals(1, tarefaBuscada.getTotalPages());
        assertEquals(2, tarefaBuscada.getContent().size());
    }
}
