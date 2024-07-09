package com.transazionePos.moneyNet.transazionePos.Controller;

import com.transazionePos.moneyNet.transazionePos.DTO.TransactionDTO;
import com.transazionePos.moneyNet.transazionePos.Entity.Transaction;
import com.transazionePos.moneyNet.transazionePos.Service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Controller per la transazione
@RestController
@RequestMapping("/transaction")
@Validated
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    // Creazione di una nuova transazione
    @PostMapping("/createTransaction")
    @PreAuthorize("isAuthenticated()")
    public CompletableFuture<ResponseEntity<Object>> create (@RequestBody @Valid TransactionDTO transactionDTO){

        return transactionService.create(transactionDTO)
                .thenApply(transaction1 ->{
                    logger.info("Transaction Created Successfully");
                    return ResponseEntity.status(HttpStatus.CREATED).build();
                })
                .exceptionally(e-> {
                    logger.info("Transaction Created Failed");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }

    // Recupero di una transazione per ID
    @GetMapping("/getTransactionById/{id}")
    @PreAuthorize("isAuthenticated()")
    public CompletableFuture<ResponseEntity<Transaction>> getById(@PathVariable Long id){
        return transactionService.getById(id)
                .thenApply(ResponseEntity::ok)
                .exceptionally(e-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Recupero di tutte le transazioni
    @GetMapping("/getAllTransaction")
    @PreAuthorize("isAuthenticated()")
    public CompletableFuture<ResponseEntity<List<Transaction>>> getAll (){
        return transactionService.getAll()
                .thenApply(ResponseEntity::ok)
                .exceptionally(e-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Aggiornamento di una transazione
    @PutMapping("/updateTransaction/{id}")
    @PreAuthorize("isAuthenticated()")
    public CompletableFuture<ResponseEntity<TransactionDTO>> update(@RequestBody @Valid TransactionDTO transactionDTO,
                                                                    @PathVariable Long id ){
        return transactionService.getById(id)
                .thenCompose(transaction1 -> transactionService.update(transactionDTO,id)
                .thenApply(t->{
                    logger.info("Transaction Update Successfully");
                    return ResponseEntity.ok(transactionDTO);})
                .exceptionally(e-> {
                    logger.info("Transactions Update Failed");
                    return (ResponseEntity<TransactionDTO>) ResponseEntity
                            .status(e instanceof EntityNotFoundException ? HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                ));
    }

    // Eliminazione di una transazione per ID
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public CompletableFuture<ResponseEntity<Object>> deleteById(@PathVariable Long id){
        return transactionService.deleteById(id).thenApply(t-> {
            logger.info("Transaction Deleted Successfully");
            return ResponseEntity.status(200).build();
                })
                .exceptionally(e-> {
                    logger.info("Transaction Deleted Failed");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    // Eliminazione di tutte le transazioni
    @DeleteMapping("/deleteTransaction")
    @PreAuthorize("isAuthenticated()")
    public CompletableFuture<ResponseEntity<Object>> deleteAll(){
        return transactionService.deleteAll().thenApply(t->{
            logger.info("Transactions Deleted Successfully");
            return ResponseEntity.status(200).build();
                })
                .exceptionally(e-> {
                    logger.info("Transactions Deleted Failed");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }
}
