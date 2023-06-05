package com.devinhouse.labsky.models;

import com.devinhouse.labsky.enums.Classificacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "passageiros")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passageiro {
    @Id
    private String cpf;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, name = "data_de_nascimento")
    private LocalDate dataDeNascimento;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Classificacao classificacao;
    @Column(nullable = false)
    private Integer milhas;
}
