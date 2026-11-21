package br.com.docodigoaocontrato.taskforge.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
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

    public TarefaDTO(Long id, String nome, Integer prioridade, Boolean concluida, LocalDateTime dataCriacao) {
    }
}
