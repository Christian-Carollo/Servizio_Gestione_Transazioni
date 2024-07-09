package com.transazionePos.moneyNet.transazionePos.Config;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
// Questa classe gestisce le eccezioni a livello globale grazie al bean @ControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandlerConfig {


    // Questo metodo gestisce l'eccezione EntityNotFoundException
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFountException (EntityNotFoundException entityNotFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(entityNotFoundException.getMessage());
    }

    // Questo metodo gestisce tutte le altre eccezioni
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExcpetion (Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
