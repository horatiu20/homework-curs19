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
	List<Transaction> getAllTransactions(@RequestParam(required = false) String product,
	                                     @RequestParam(required = false) Type transactionType,
	                                     @RequestParam(required = false) Double minAmount,
	                                     @RequestParam(required = false) Double maxAmount) {
		return transactionService.getAllTransactions(product, transactionType, minAmount, maxAmount);
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

	@GetMapping("reports/type")
	Map<Type, List<Map<Type, Double>>> sumTypeToAmount() {
		return transactionService.sumTypeToAmount();
	}

//	@GetMapping("reports/product")
//	Map<String, List<Transaction>> mapProductToAmount() {
//		return transactionService.mapProductToAmount();
//	}
}
