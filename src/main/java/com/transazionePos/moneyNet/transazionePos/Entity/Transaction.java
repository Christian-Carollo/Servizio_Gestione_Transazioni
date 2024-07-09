package com.transazionePos.moneyNet.transazionePos.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
// Entità Transazione
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(name = "numero_di_Carta", nullable = false)
    private String numeroDiCarta;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato_transazione", nullable = false)
    private EStatoTransazione statoTransazione;
}
