package com.transazionePos.moneyNet.transazionePos.DTO;

import com.transazionePos.moneyNet.transazionePos.Entity.EStatoTransazione;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
// DTO per la transazione
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    @NotEmpty(message = "Card number is required")
    @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
    private String numeroDiCarta;
    @NotNull(message = "Date is required")
    private LocalDate data;
    private EStatoTransazione statoTransazione;
}
