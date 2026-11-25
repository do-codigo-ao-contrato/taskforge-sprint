package br.com.docodigoaocontrato.taskforge.repository;

import br.com.docodigoaocontrato.taskforge.model.Tarefa;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByConcluida(Boolean concluida, Sort sort);

    List<Tarefa> findByDataPrazoBeforeAndConcluidaFalse(LocalDate data);

    List<Tarefa> findByConcluida(Boolean concluida);

    long countByConcluida(boolean concluida);
}