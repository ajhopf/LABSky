package com.devinhouse.labsky.models;

import com.devinhouse.labsky.enums.Classificacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "passageiros")
public class Passageiro {
    @Id
    private String cpf;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private LocalDate dataDeNascimento;
    @Column(nullable = false)
    private Classificacao classificacao;
    @Column(nullable = false)
    private Integer milhas;
}
