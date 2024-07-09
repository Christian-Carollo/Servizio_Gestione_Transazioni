package com.transazionePos.moneyNet.transazionePos;

import com.transazionePos.moneyNet.transazionePos.DTO.TransactionDTO;
import com.transazionePos.moneyNet.transazionePos.Entity.EStatoTransazione;
import com.transazionePos.moneyNet.transazionePos.Entity.Transaction;
import com.transazionePos.moneyNet.transazionePos.Repository.TransactionRepository;
import com.transazionePos.moneyNet.transazionePos.Service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
// Test per il servizio di transazione
@SpringBootTest
@RunWith(SpringRunner.class)
class TransazionePosApplicationTests {


	@Autowired
	private TransactionService transactionService;

	@MockBean
	private TransactionRepository transactionRepository;

	// Test per la creazione di una nuova transazione
	@Test
	public void testCreateTransaction() {
		Transaction transaction = new Transaction(1L, "1234567890", LocalDate.now(), EStatoTransazione.ESEGUITA_CON_SUCCESSO);
		Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);

		CompletableFuture<Transaction> createdTransaction = transactionService.create(new TransactionDTO());

		assertEquals(transaction, createdTransaction.join());
	}

	// Test per il recupero di una transazione per ID
	@Test
	public void testGetTransactionById() {
		Transaction transaction = new Transaction(1L, "1234567890", LocalDate.now(), EStatoTransazione.ESEGUITA_CON_SUCCESSO);
		Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

		CompletableFuture<Transaction> foundTransaction = transactionService.getById(1L);

		assertEquals(transaction, foundTransaction.join());
	}

	// Test per il recupero di tutte le transazioni
	@Test
	public void testGetAllTransactions() {
		List<Transaction> transactions = Arrays.asList(
				new Transaction(1L, "1234567890", LocalDate.now(), EStatoTransazione.ESEGUITA_CON_SUCCESSO),
				new Transaction(2L, "0987654321", LocalDate.now(), EStatoTransazione.OPERAZIONE_NON_RIUSCITA)
		);
		Mockito.when(transactionRepository.findAll()).thenReturn(transactions);

		CompletableFuture<List<Transaction>> foundTransactions = transactionService.getAll();

		assertEquals(transactions, foundTransactions.join());


	}

	// Test per l'aggiornamento di una transazione
	@Test
	public void testUpdateTransaction() {
		// Dati di input
		TransactionDTO transactionDTO = new TransactionDTO("1234567890", LocalDate.now(), EStatoTransazione.ESEGUITA_CON_SUCCESSO);
		Transaction transaction = new Transaction();
		transaction.setNumeroDiCarta(transactionDTO.getNumeroDiCarta());
		transaction.setData(transactionDTO.getData());
		transaction.setStatoTransazione(transactionDTO.getStatoTransazione());

		// Dati aggiornati
		TransactionDTO updatedTransactionDTO = new TransactionDTO("1234567890", LocalDate.now(), EStatoTransazione.OPERAZIONE_NON_RIUSCITA);
		Transaction updatedTransaction = new Transaction();
		updatedTransaction.setNumeroDiCarta(updatedTransactionDTO.getNumeroDiCarta());
		updatedTransaction.setData(updatedTransactionDTO.getData());
		updatedTransaction.setStatoTransazione(updatedTransactionDTO.getStatoTransazione());

		// Mock dei metodi del repository
		Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
		Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(updatedTransaction);

		// Chiamata al metodo di update
		CompletableFuture<Void> updateFuture = transactionService.update(updatedTransactionDTO, 1L);

		// Attesa della conclusione dell'operazione
		updateFuture.join();

		// Verifica che il metodo save sia stato chiamato con l'entità aggiornata
		Mockito.verify(transactionRepository).save(Mockito.any(Transaction.class));

		// Ulteriori verifiche sui valori aggiornati
		Assert.assertEquals(EStatoTransazione.OPERAZIONE_NON_RIUSCITA, transaction.getStatoTransazione());
		Assert.assertEquals("1234567890", transaction.getNumeroDiCarta());
		Assert.assertEquals(LocalDate.now(), transaction.getData());
	}

	// Test per l'eliminazione di una transazione per ID
	@Test
	public void testDeleteTransactionById() {
		Transaction transaction = new Transaction(1L, "1234567890", LocalDate.now(), EStatoTransazione.ESEGUITA_CON_SUCCESSO);
		Mockito.when(transactionRepository.existsById(1L)).thenReturn(true);

		CompletableFuture<Void> deleteFuture = transactionService.deleteById(1L);

		deleteFuture.join();
		Mockito.verify(transactionRepository).deleteById(1L);
	}

	// Test per l'eliminazione di tutte le transazioni
	@Test
	public void testDeleteAllTransactions() {
		CompletableFuture<Void> deleteFuture = transactionService.deleteAll();

		deleteFuture.join();
		Mockito.verify(transactionRepository).deleteAll();
	}

	// Test per la gestione dell'eccezione EntityNotFoundException
	@Test
	public void testHandleEntityNotFoundException() {
		Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> transactionService.getById(1L).join());
	}

	// Test per la gestione di altre eccezioni
	@Test
	public void testHandleOtherExceptions() {
		Mockito.when(transactionRepository.findById(1L)).thenThrow(new RuntimeException("Test exception"));

		assertThrows(RuntimeException.class, () -> transactionService.getById(1L).join());
	}

}
