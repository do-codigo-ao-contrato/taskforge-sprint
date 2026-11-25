package br.com.docodigoaocontrato.taskforge.repository;

import br.com.docodigoaocontrato.taskforge.model.Tarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    Page<Tarefa> findByConcluida(Boolean concluida, Pageable pageable);

    Page<Tarefa> findByNomeContainingIgnoreCase(String busca, Pageable pageable);

    long countByConcluida(boolean concluida);
}
