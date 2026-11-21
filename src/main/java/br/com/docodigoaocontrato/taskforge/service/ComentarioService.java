package br.com.docodigoaocontrato.taskforge.service;

import br.com.docodigoaocontrato.taskforge.dto.ComentarioDTO;
import br.com.docodigoaocontrato.taskforge.model.Comentario;
import br.com.docodigoaocontrato.taskforge.model.Tarefa;
import br.com.docodigoaocontrato.taskforge.repository.ComentarioRepository;
import br.com.docodigoaocontrato.taskforge.repository.TarefaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final TarefaRepository tarefaRepository;

    public ComentarioService(ComentarioRepository comentarioRepository, TarefaRepository tarefaRepository) {
        this.comentarioRepository = comentarioRepository;
        this.tarefaRepository = tarefaRepository;
    }

    public List<ComentarioDTO> listarTodos() {
        return comentarioRepository.findAll()
                .stream()
                .map(comentario -> toDto(comentario))
                .toList();
    }

    public Optional<ComentarioDTO> buscarPorId(Long id) {
        return comentarioRepository.findById(id)
                .map(comentario -> toDto(comentario));
    }

    public Optional<ComentarioDTO> criarComentario(Long id, ComentarioDTO comentarioDTO) {
        Optional<Tarefa> tarefa = tarefaRepository.findById(id);
        if (tarefa.isEmpty()){
            return Optional.empty();
        }
        Tarefa tarefaEncontrada = tarefa.get();
        Comentario comentario = toEntity(comentarioDTO);
        comentario.setTarefa(tarefaEncontrada);
        Comentario comentarioSalvo = comentarioRepository.save(comentario);
        return Optional.of(toDto(comentarioSalvo));
    }

    public Optional<ComentarioDTO> atualizarComentario(Long id, ComentarioDTO comentarioDTO) {
        Optional<Comentario> comentarioRecuperado = comentarioRepository.findById(id);
        if (comentarioRecuperado.isPresent()) {
            Comentario comentario = comentarioRecuperado.get();
            comentario.setDescricao(comentarioDTO.getDescricao());
            return Optional.of(toDto(comentarioRepository.save(comentario)));
        }
        return Optional.empty();
    }

    public boolean deletarComentario(Long id) {
        if (comentarioRepository.existsById(id)) {
            comentarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ComentarioDTO toDto(Comentario comentario) {
        return new ComentarioDTO(comentario.getId(),
                comentario.getDescricao(), comentario.getAutor());
    }

    private Comentario toEntity(ComentarioDTO comentarioDTO) {
        return new Comentario(comentarioDTO.getId(),
                comentarioDTO.getDescricao(), comentarioDTO.getAutor());
    }
}
