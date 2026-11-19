package br.com.docodigoaocontrato.taskforge.controller;

import br.com.docodigoaocontrato.taskforge.dto.TarefaDTO;
import br.com.docodigoaocontrato.taskforge.service.IaService;
import br.com.docodigoaocontrato.taskforge.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tarefas")
@Tag(name = "Tarefas", description = "Gerenciador de tarefas")
public class TarefaController {

    private final TarefaService tarefaService;
    private final IaService iaService;

    public TarefaController(TarefaService tarefaService, IaService iaService) {
        this.tarefaService = tarefaService;
        this.iaService = iaService;
    }

    @GetMapping
    public ResponseEntity<List<TarefaDTO>> listar(
            @RequestParam(required = false) Boolean concluida,
            @RequestParam(required = false) String ordenar) {
        return ResponseEntity.ok(tarefaService.buscarTodos(concluida, ordenar));
    }

    @GetMapping("/atrasadas")
    public ResponseEntity<List<TarefaDTO>> listarAtrasadas() {
        return ResponseEntity.ok(tarefaService.buscarAtrasadas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaDTO> buscarTarefaPorId(@PathVariable Long id) {
        Optional<TarefaDTO> tarefaDTO = tarefaService.buscarTarefaPorId(id);
        if (tarefaDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(tarefaDTO.get());
    }

    @Operation(summary = "Criar uma tarefa")
    @PostMapping
    public ResponseEntity<TarefaDTO> criarTarefa(@Valid @RequestBody TarefaDTO tarefaDTO) {
        TarefaDTO tarefaCriada = tarefaService.criarTarefa(tarefaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefaCriada);
    }

    @PostMapping("/sugerir-descricao")
    public Map<String, String> sugerir(@RequestBody Map<String, String> body) {
        String titulo = body.get("titulo");
        String descricao = iaService.sugerirDescricao(titulo);
        return Map.of("descricao", descricao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaDTO> atualizarTarefa(@PathVariable Long id, @RequestBody TarefaDTO tarefaDTO){
        Optional<TarefaDTO> tarefaAtualizada = tarefaService.atualizarTarefa(id, tarefaDTO);
        if (tarefaAtualizada.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(tarefaAtualizada.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long id) {
        if (!tarefaService.deletarTarefa(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}