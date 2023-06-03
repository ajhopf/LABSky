package com.devinhouse.labsky.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assentos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String assento;
    @Column(nullable = false, name = "fileira_de_emergencia")
    private char fileiraDeEmergencia;
    @Column(columnDefinition = "char default '0'")
    private char reservado;
}
