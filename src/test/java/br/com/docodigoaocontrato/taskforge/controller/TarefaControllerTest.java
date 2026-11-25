package br.com.docodigoaocontrato.taskforge.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.docodigoaocontrato.taskforge.dto.TarefaDTO;
import br.com.docodigoaocontrato.taskforge.service.IaService;
import br.com.docodigoaocontrato.taskforge.service.TarefaService;

class TarefaControllerTest {

    private final TarefaService tarefaService = mock(TarefaService.class);
    private final IaService iaService = mock(IaService.class);

    private final TarefaController controller =
            new TarefaController(tarefaService, iaService);

    @Test
    void deveListarTarefasERetornarStatusOk() {
        when(tarefaService.buscarTodos(null, null)).thenReturn(List.of());

        ResponseEntity<List<TarefaDTO>> resposta = controller.listar(null, null);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(0, resposta.getBody().size());
        verify(tarefaService).buscarTodos(null, null);
    }
}