package com.devinhouse.labsky.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private String classificacao;
    @Column(nullable = false)
    private Integer milhas;
    private String eticket;
    private String assento;
    @Column(name = "malas_despachadas")
    private Boolean malasDespachadas;
    @Column(name = "data_hora_confirmacao")
    private LocalDateTime dataHoraConfirmacao;
}
