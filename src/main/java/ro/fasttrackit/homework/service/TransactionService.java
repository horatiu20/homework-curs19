package ro.fasttrackit.homework.service;

import org.springframework.stereotype.Service;
import ro.fasttrackit.homework.model.Transaction;
import ro.fasttrackit.homework.model.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class TransactionService {
	private final List<Transaction> transactions = new ArrayList<>();

	public TransactionService() {
		transactions.addAll(List.of(
				new Transaction(1, "laptop", Type.BUY, 1000),
				new Transaction(2, "headphones", Type.BUY, 100),
				new Transaction(3, "player", Type.BUY, 250),
				new Transaction(4, "tv", Type.SELL, 750),
				new Transaction(5, "car", Type.SELL, 10000),
				new Transaction(6, "fridge", Type.SELL, 500)));
	}

	public List<Transaction> getAllTransactions() {
		return transactions;
	}

	public List<String> getAllProducts() {
		return this.transactions.stream()
				.map(Transaction::product)
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

	public List<Transaction> getMinAmount() {
		return this.transactions.stream()
				.filter(transaction -> transaction.amount() == fetchMinAmount())
				.collect(toList());
	}

	public List<Transaction> getMaxAmount() {
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

	public Transaction postTransaction(Transaction transaction) {
		return postTransaction(fetchMaxId() + 1, transaction);
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

	public Optional<Transaction> patchTransaction(int transactionId, Transaction transaction) {
		Optional<Transaction> transactionById = getById(transactionId);
		Optional<Transaction> patchedTransaction = transactionById
				.map(oldTransaction -> new Transaction(
						oldTransaction.id(),
						transaction.product() != null ? transaction.product() : oldTransaction.product(),
						transaction.transactionType() != null ? transaction.transactionType() : oldTransaction.transactionType(),
						transaction.amount() != 0 ? transaction.amount() : oldTransaction.amount()));
		patchedTransaction.ifPresent(newTransaction -> putTransaction(transactionId, newTransaction));
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
