package br.com.docodigoaocontrato.taskforge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Integer prioridade;
    private Boolean concluida;
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "tarefa")
    private List<Comentario> comentarios = new ArrayList<>();

    @PrePersist
    public void aoCriar() {
        this.dataCriacao = LocalDateTime.now();
    }

    public Tarefa(String nome, Integer prioridade, Boolean concluida) {
        this.nome = nome;
        this.prioridade = prioridade;
        this.concluida = concluida;
    }

    public Tarefa(Long id, String nome, Integer prioridade, Boolean concluida, List<Comentario> comentarios) {
        this.id = id;
        this.nome = nome;
        this.prioridade = prioridade;
        this.concluida = concluida;
        this.comentarios = comentarios;
    }
}
