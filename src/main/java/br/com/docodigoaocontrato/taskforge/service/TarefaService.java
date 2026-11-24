package br.com.docodigoaocontrato.taskforge.service;

import br.com.docodigoaocontrato.taskforge.dto.TarefaDTO;
import br.com.docodigoaocontrato.taskforge.model.Tarefa;
import br.com.docodigoaocontrato.taskforge.repository.TarefaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public List<TarefaDTO> buscarTodos(Boolean concluida, String ordenar) {
        List<Tarefa> tarefas;
        if (concluida == null) {
            tarefas = tarefaRepository.findAll();
        } else {
            tarefas = tarefaRepository.findByConcluida(concluida);
        }

        if ("prioridade".equalsIgnoreCase(ordenar)) {
            tarefas.sort(Comparator.comparing(Tarefa::getPrioridade).reversed());
        }

        return tarefas.stream()
                .map(tarefa -> toDto(tarefa))
                .toList();
    }

    public List<TarefaDTO> buscarAtrasadas() {
        LocalDate hoje = LocalDate.now();
        List<Tarefa> tarefasAtrasadas = tarefaRepository.findByDataPrazoBeforeAndConcluidaFalse(hoje);

        return tarefasAtrasadas.stream()
                .map(tarefa -> toDto(tarefa))
                .toList();
    }

    public Optional<TarefaDTO> buscarTarefaPorId(Long id) {
        return tarefaRepository.findById(id)
                .map(tarefa -> toDto(tarefa));
    }

    public TarefaDTO criarTarefa(TarefaDTO tarefaDTO) {
        Tarefa tarefa = toEntity(tarefaDTO);
        return toDto(tarefaRepository.save(tarefa));
    }

    public Optional<TarefaDTO> atualizarTarefa(Long id, TarefaDTO tarefaDTO) {
        Optional<Tarefa> tarefaRecuperada = tarefaRepository.findById(id);
        if (tarefaRecuperada.isPresent()) {
            Tarefa tarefa = tarefaRecuperada.get();
            tarefa.setNome(tarefaDTO.getNome());
            tarefa.setConcluida(tarefaDTO.getConcluida());
            tarefa.setPrioridade(tarefaDTO.getPrioridade());
            tarefa.setDataPrazo(tarefaDTO.getDataPrazo());

            return Optional.of(toDto(tarefaRepository.save(tarefa)));
        }
        return Optional.empty();
    }

    public boolean deletarTarefa(Long id) {
        if (tarefaRepository.existsById(id)){
            tarefaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private TarefaDTO toDto(Tarefa tarefa) {
        return new TarefaDTO(
                tarefa.getId(),
                tarefa.getNome(),
                tarefa.getPrioridade(),
                tarefa.getConcluida(),
                tarefa.getDataPrazo(),
                tarefa.getDataCriacao()
        );
    }

    private Tarefa toEntity(TarefaDTO tarefaDTO) {
        Tarefa tarefa = new Tarefa(tarefaDTO.getNome(),
                tarefaDTO.getPrioridade(), tarefaDTO.getConcluida());
        tarefa.setDataPrazo(tarefaDTO.getDataPrazo());
        return tarefa;
    }
}