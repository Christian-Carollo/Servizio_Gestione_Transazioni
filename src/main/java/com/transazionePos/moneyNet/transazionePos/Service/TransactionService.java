package com.transazionePos.moneyNet.transazionePos.Service;

import com.transazionePos.moneyNet.transazionePos.DTO.TransactionDTO;
import com.transazionePos.moneyNet.transazionePos.Entity.EStatoTransazione;
import com.transazionePos.moneyNet.transazionePos.Entity.Transaction;
import com.transazionePos.moneyNet.transazionePos.Repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
// Implementazione del servizio per la transazione
@Service
@Validated
public class TransactionService implements ITransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private final ModelMapper modelMapper;

    public TransactionService(ModelMapper modelMapper, TransactionRepository transactionRepository) {
        this.modelMapper = modelMapper;
        this.transactionRepository = transactionRepository;
    }

    // Creazione di una nuova transazione
    @Override
    @Transactional
    public CompletableFuture<Transaction> create(@Valid TransactionDTO transactionDTO) {
        Transaction transaction = modelMapper.map(transactionDTO,Transaction.class);

        return CompletableFuture.supplyAsync(()->{
            try{
                transaction.setStatoTransazione(EStatoTransazione.ESEGUITA_CON_SUCCESSO);
                return transactionRepository.save(transaction);
            }catch (Exception e){
                transaction.setStatoTransazione(EStatoTransazione.OPERAZIONE_NON_RIUSCITA);
                throw new EntityNotFoundException("Operation Failed");
            }
        });
    }
    // Recupero di una transazione per ID
    @Override
    @Transactional(readOnly = true)
    public CompletableFuture<Transaction> getById(Long id) {
        return CompletableFuture.supplyAsync(()->{
            Optional<Transaction> optionalTransaction = transactionRepository.findById(id);
            return optionalTransaction.orElseThrow(()-> new EntityNotFoundException("Cannot found transaction with this id " + id));
        });
    }
    // Recupero di tutte le transazioni
    @Override
    @Transactional(readOnly = true)
    public CompletableFuture<List<Transaction>> getAll() {
        return CompletableFuture.supplyAsync(()->{
            List<Transaction> transactions = transactionRepository.findAll();
            return transactions.isEmpty() ? Collections.emptyList() : transactions;
        });
    }
    // Aggiornamento di una transazione
    @Override
    @Transactional
    public CompletableFuture<Void> update(TransactionDTO transactionDTO, Long id) {
        Transaction transaction = modelMapper.map(transactionDTO,Transaction.class);
        return CompletableFuture.runAsync(()->{
            Transaction transactionExisting = transactionRepository.findById(id)
                    .orElseThrow(()-> new EntityNotFoundException("Cannot found transaction with this id "+ id));

            transactionExisting.setStatoTransazione(transaction.getStatoTransazione());
            transactionExisting.setNumeroDiCarta(transaction.getNumeroDiCarta());
            transactionExisting.setData(transaction.getData());

            transactionRepository.save(transactionExisting);
        });
    }

    // Eliminazione di una transazione per ID
    @Override
    @Transactional
    public CompletableFuture<Void> deleteById(Long id) {
        return CompletableFuture.runAsync(()->{
            transactionRepository.findById(id)
                    .orElseThrow(()-> new EntityNotFoundException("Cannot delete transaction with this id "+ id));
            transactionRepository.deleteById(id);
        });
    }
    // Eliminazione di tutte le transazioni
    @Override
    @Transactional
    public CompletableFuture<Void> deleteAll() {
        return CompletableFuture.runAsync(transactionRepository::deleteAll)
                .exceptionally(e -> {
                    throw new EntityNotFoundException("Cannot delete transaction list");
                });
    }
}
