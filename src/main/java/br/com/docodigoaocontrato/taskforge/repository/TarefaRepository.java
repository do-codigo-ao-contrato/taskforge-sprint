package br.com.docodigoaocontrato.taskforge.repository;

import br.com.docodigoaocontrato.taskforge.model.Tarefa;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate; // <-- IMPORTANTE: Adicione este import
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    // Método com ordenação que o Jefferson pediu
    List<Tarefa> findByConcluida(Boolean concluida, Sort sort);

    // Método das tarefas atrasadas (que veio junto no seu stash)
    List<Tarefa> findByDataPrazoBeforeAndConcluidaFalse(LocalDate data);
}