package com.transazionePos.moneyNet.transazionePos.Service;

import com.transazionePos.moneyNet.transazionePos.DTO.TransactionDTO;
import com.transazionePos.moneyNet.transazionePos.Entity.Transaction;

import java.util.List;
import java.util.concurrent.CompletableFuture;

// Interfaccia del servizio per la transazione
public interface ITransactionService {

    CompletableFuture<Transaction> create(TransactionDTO transactionDTO);
    CompletableFuture<Transaction> getById(Long id);
    CompletableFuture<List<Transaction>> getAll();
    CompletableFuture<Void> update(TransactionDTO transactionDTO, Long id);
    CompletableFuture<Void> deleteById (Long id);
    CompletableFuture<Void> deleteAll();
}
