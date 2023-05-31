package com.devinhouse.labsky.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassageiroCompletoResponseDto {
    private String cpf;
    private String nome;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataDeNascimento;
    private String classificacao;
    private Integer milhas;
}
