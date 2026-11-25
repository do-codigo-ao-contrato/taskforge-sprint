package br.com.docodigoaocontrato.taskforge.controller;

import br.com.docodigoaocontrato.taskforge.dto.TarefaDTO;
import br.com.docodigoaocontrato.taskforge.dto.TarefaEstatisticasDTO;
import br.com.docodigoaocontrato.taskforge.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @GetMapping
    public ResponseEntity<List<TarefaDTO>> listarTodos(
            @RequestParam(required = false) Boolean concluida,
            @RequestParam(required = false) String ordenar) {
        return ResponseEntity.ok(tarefaService.buscarTodos(concluida, ordenar));
    }

    @GetMapping("/atrasadas")
    public ResponseEntity<List<TarefaDTO>> listarAtrasadas() {
        return ResponseEntity.ok(tarefaService.buscarAtrasadas());
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<TarefaEstatisticasDTO> obterEstatisticas() {
        return ResponseEntity.ok(tarefaService.obterEstatisticas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaDTO> buscarPorId(@PathVariable Long id) {
        return tarefaService.buscarTarefaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TarefaDTO> criar(@Valid @RequestBody TarefaDTO tarefaDTO) {
        return ResponseEntity.status(201).body(tarefaService.criarTarefa(tarefaDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody TarefaDTO tarefaDTO) {
        return tarefaService.atualizarTarefa(id, tarefaDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (tarefaService.deletarTarefa(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}