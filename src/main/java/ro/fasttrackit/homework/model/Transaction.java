package ro.fasttrackit.homework.model;

public record Transaction(
		int id,
		String product,
		Type transactionType,
		double amount) {

}
