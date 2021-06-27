package ro.fasttrackit.homework.service;

import org.springframework.stereotype.Service;
import ro.fasttrackit.homework.model.Transaction;
import ro.fasttrackit.homework.model.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.*;

@Service
public class TransactionService {
	private final List<Transaction> transactions = new ArrayList<>();

	public List<Transaction> getAllProducts(String product) {
		return this.transactions.stream()
				.filter(transaction -> transaction.product().equalsIgnoreCase(product))
				.collect(toList());
	}

	public List<Transaction> getAllBuyTransactions(Type transactionType) {
		return this.transactions.stream()
				.filter(transaction -> transaction.transactionType().equals(Type.BUY))
				.collect(toList());
	}

	public List<Transaction> getAllSellTransactions(Type transactionType) {
		return this.transactions.stream()
				.filter(transaction -> transaction.transactionType().equals(Type.SELL))
				.collect(toList());
	}

	public List<Transaction> getMinAmount(double minAmount) {
		return this.transactions.stream()
				.filter(transaction -> transaction.amount() == fetchMinAmount())
				.collect(toList());
	}

	public List<Transaction> getMaxAmount(double maxAmount) {
		return this.transactions.stream()
				.filter(transaction -> transaction.amount() == fetchMaxAmount())
				.collect(toList());
	}

	private double fetchMinAmount() {
		return this.transactions.stream()
				.mapToDouble(Transaction::amount)
				.min()
				.orElse(1);
	}

	private double fetchMaxAmount() {
		return this.transactions.stream()
				.mapToDouble(Transaction::amount)
				.max()
				.orElse(1);
	}

	public Optional<Transaction> getById(int transactionId) {
		return this.transactions.stream()
				.filter(transaction -> transaction.id() == transactionId)
				.findFirst();
	}

	public Transaction postTransaction(int transactionId, Transaction transaction) {
		Transaction newTransaction = new Transaction(
				fetchMaxId() + 1,
				transaction.product(),
				transaction.transactionType(),
				transaction.amount());
		this.transactions.add(newTransaction);
		return newTransaction;

	}

	private int fetchMaxId() {
		return this.transactions.stream()
				.mapToInt(Transaction::id)
				.max()
				.orElse(1);
	}

	public Optional<Transaction> putTransaction(int transactionId, Transaction newTransaction) {
		Optional<Transaction> replacedTransaction = deleteTransaction(transactionId);
		replacedTransaction
				.ifPresent(deleteTransaction -> postTransaction(transactionId, newTransaction));
		return replacedTransaction;
	}

	public Optional<Transaction> deleteTransaction(int transactionId) {
		Optional<Transaction> transactionOptional = getById(transactionId);
		transactionOptional
				.ifPresent(transactions::remove);
		return transactionOptional;
	}

	public Transaction postTransaction(Transaction transaction) {
		return postTransaction(fetchMaxId() + 1, transaction);
	}

	public Optional<Transaction> patchTransaction(int transactionId, Transaction transaction) {
		Optional<Transaction> transactionById = getById(transactionId);
		Optional<Transaction> patchedTransaction = transactionById
				.map(oldTransaction -> new Transaction(
						oldTransaction.id(),
						transaction.product() != null ? transaction.product() : oldTransaction.product(),
						transaction.transactionType() != null ? transaction.transactionType() : oldTransaction.transactionType(),
						transaction.amount() != 0 ? transaction.amount() : oldTransaction.amount()));
		patchedTransaction.ifPresent(newCountry -> putTransaction(transactionId, newCountry));
		return patchedTransaction;
	}

	public Map<Type, List<Transaction>> mapTypeToAmount() {
		return transactions.stream()
				.collect(groupingBy(Transaction::transactionType));
	}

	public Map<String, List<Transaction>> mapProductToAmount() {
		return transactions.stream()
				.collect(groupingBy(Transaction::product));
	}
}
