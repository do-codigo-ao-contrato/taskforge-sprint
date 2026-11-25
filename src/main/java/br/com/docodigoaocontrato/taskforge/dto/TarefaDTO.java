package br.com.docodigoaocontrato.taskforge.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TarefaDTO {

    private Long id;

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(max = 10, message = "O nome deve ter no máximo 10 caracteres")
    private String nome;

    @NotNull(message = "A prioridade é obrigatória")
    @Min(value = 1, message = "A prioridade deve estar entre 1 e 5")
    @Max(value = 50, message = "A prioridade deve estar entre 1 e 5")
    private Integer prioridade;

    @NotNull(message = "Status de conclusão é obrigatório")
    private Boolean concluida;

    private List<ComentarioDTO> comentarios;

    private LocalDateTime dataCriacao;

    public TarefaDTO(Long id, String nome, Integer prioridade, Boolean concluida, List<ComentarioDTO> comentarios,
                     LocalDateTime dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.prioridade = prioridade;
        this.concluida = concluida;
        this.comentarios = comentarios;
        this.dataCriacao = dataCriacao;
    }
}
