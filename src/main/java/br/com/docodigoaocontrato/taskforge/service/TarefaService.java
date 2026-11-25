package br.com.docodigoaocontrato.taskforge.service;

import br.com.docodigoaocontrato.taskforge.dto.ComentarioDTO;
import br.com.docodigoaocontrato.taskforge.dto.TarefaDTO;
import br.com.docodigoaocontrato.taskforge.dto.TarefaEstatisticasDTO;
import br.com.docodigoaocontrato.taskforge.model.Comentario;
import br.com.docodigoaocontrato.taskforge.model.Tarefa;
import br.com.docodigoaocontrato.taskforge.repository.ComentarioRepository;
import br.com.docodigoaocontrato.taskforge.repository.TarefaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final ComentarioRepository comentarioRepository;

    public TarefaService(TarefaRepository tarefaRepository, ComentarioRepository comentarioRepository) {
        this.tarefaRepository = tarefaRepository;
        this.comentarioRepository = comentarioRepository;
    }

    public Optional<List<ComentarioDTO>> buscarTarefaComComentarios(Long id) {
        if (!tarefaRepository.existsById(id)) {
            return Optional.empty();
        }
        List<ComentarioDTO> comentarioDTO = comentarioRepository.findByTarefaId(id)
                .stream()
                .map(this::comentarioToDto)
                .toList();

        return Optional.of(comentarioDTO);
    }

    public Page<TarefaDTO> buscarTodos(Boolean concluida, String busca, Pageable pageable) {
        Page<Tarefa> tarefas;
        if (busca != null && !busca.isBlank()) {
            tarefas = tarefaRepository.findByNomeContainingIgnoreCase(busca, pageable);
        } else if (concluida == null) {
            tarefas = tarefaRepository.findAll(pageable);
        } else {
            tarefas = tarefaRepository.findByConcluida(concluida, pageable);
        }
        return tarefas.map(tarefa -> toDto(tarefa));
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
            return Optional.of(toDto(tarefaRepository.save(tarefa)));
        }
        return Optional.empty();
    }

    public boolean deletarTarefa(Long id) {
        if (tarefaRepository.existsById(id)) {
            tarefaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public TarefaEstatisticasDTO obterEstatisticas() {
        long total = tarefaRepository.count();
        long concluidas = tarefaRepository.countByConcluida(true);
        long pendentes = tarefaRepository.countByConcluida(false);

        return new TarefaEstatisticasDTO(total, concluidas, pendentes);
    }

    private TarefaDTO toDto(Tarefa tarefa) {
        List<ComentarioDTO> comentarios = tarefa.getComentarios()
                .stream()
                .map(this::comentarioToDto)
                .toList();

        return new TarefaDTO(tarefa.getId(), tarefa.getNome(),
                tarefa.getPrioridade(), tarefa.getConcluida(), comentarios, tarefa.getDataCriacao());
    }

    private Tarefa toEntity(TarefaDTO tarefaDTO) {
        return new Tarefa(tarefaDTO.getNome(), tarefaDTO.getPrioridade(), tarefaDTO.getConcluida());
    }

    private ComentarioDTO comentarioToDto(Comentario comentario) {
        return new ComentarioDTO(comentario.getId(), comentario.getDescricao(), comentario.getAutor());
    }
}
