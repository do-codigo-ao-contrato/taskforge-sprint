package br.com.docodigoaocontrato.taskforge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.docodigoaocontrato.taskforge.dto.TarefaDTO;
import br.com.docodigoaocontrato.taskforge.model.Tarefa;
import br.com.docodigoaocontrato.taskforge.repository.TarefaRepository;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @InjectMocks
    private TarefaService tarefaService;

    @Test
    void deveBuscarTodasAsTarefasEConverterParaDto() {
        Tarefa tarefa = new Tarefa("Estudar Java", 1, false);
        tarefa.setId(1L);

        when(tarefaRepository.findAll()).thenReturn(List.of(tarefa));

        List<TarefaDTO> resultado = tarefaService.buscarTodos(null, null);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Estudar Java", resultado.get(0).getNome());
        assertEquals(1, resultado.get(0).getPrioridade());
        assertEquals(false, resultado.get(0).getConcluida());

        verify(tarefaRepository).findAll();
    }
}