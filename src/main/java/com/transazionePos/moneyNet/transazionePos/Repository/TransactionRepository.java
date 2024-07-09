package com.transazionePos.moneyNet.transazionePos.Repository;

import com.transazionePos.moneyNet.transazionePos.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
// Interfaccia del repository per la transazione
@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
