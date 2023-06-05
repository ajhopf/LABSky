package com.devinhouse.labsky.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bilhetes_de_embarque")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BilheteDeEmbarque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eticket;
    private String assento;
    @Column(name = "malas_despachadas")
    private Boolean malasDespachadas;
    @Column(name = "data_e_hora_confirmacao")
    private LocalDateTime dataHoraConfirmacao;
    @OneToOne
    @JoinColumn(nullable = false)
    private Passageiro passageiro;

    public BilheteDeEmbarque(String eticket, String assento, Boolean malasDespachadas, Passageiro passageiro) {
        this.eticket = eticket;
        this.assento = assento;
        this.malasDespachadas = malasDespachadas;
        this.passageiro = passageiro;
        this.dataHoraConfirmacao = LocalDateTime.now();
    }
}
