package ro.fasttrackit.homework.controller;

import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.homework.model.Transaction;
import ro.fasttrackit.homework.model.Type;
import ro.fasttrackit.homework.service.TransactionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("transactions")
public class TransactionController {
	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@GetMapping
	List<Transaction> getAllTransactions() {
		return transactionService.getAllTransactions();
	}

	@GetMapping("product")
	List<String> getAllProducts() {
		return transactionService.getAllProducts();
	}

	@GetMapping("buy")
	List<Transaction> getAllBuyTransactions(@RequestParam(required = false) Type transactionType) {
		return transactionService.getAllBuyTransactions(Type.BUY);
	}

	@GetMapping("sell")
	List<Transaction> getAllSellTransactions(@RequestParam(required = false) Type transactionType) {
		return transactionService.getAllSellTransactions(Type.SELL);
	}

	@GetMapping("minimum")
	List<Transaction> getMinAmount() {
		return transactionService.getMinAmount();
	}

	@GetMapping("maximum")
	List<Transaction> getMaxAmount() {
		return transactionService.getMaxAmount();
	}

	@GetMapping("{transactionId}")
	Transaction getById(@PathVariable int transactionId) {
		return transactionService.getById(transactionId)
				.orElse(null);
	}

	@PostMapping
	Transaction createTransaction(@RequestBody Transaction transaction) {
		return transactionService.postTransaction(transaction);
	}

	@PutMapping("{transactionId}")
	Transaction putTransaction(@PathVariable int transactionId, @RequestBody Transaction newTransaction) {
		return transactionService.putTransaction(transactionId, newTransaction).orElse(null);
	}

	@PatchMapping("{transactionId}")
	Transaction patchTransaction(@PathVariable int transactionId, @RequestBody Transaction transaction) {
		return transactionService.patchTransaction(transactionId, transaction).orElse(null);
	}

	@DeleteMapping("{transactionId}")
	Transaction deleteTransaction(@PathVariable int transactionId) {
		return transactionService.deleteTransaction(transactionId).orElse(null);
	}

	@GetMapping("type/amount")
	Map<Type, List<Transaction>> mapTypeToAmount(){
		return transactionService.mapTypeToAmount();
	}

	@GetMapping("product/amount")
	Map<String, List<Transaction>> mapProductToAmount(){
		return transactionService.mapProductToAmount();
	}
}
