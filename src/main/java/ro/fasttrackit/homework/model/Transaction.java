package ro.fasttrackit.homework.model;

/*Create a webapp backend that manages the transactions in a budget:
Transaction
	- id
	- product
	- type: SELL/BUY
	- amount: double

GET /transactions - get all transactions. Make it filterable by product , type, minAmount, maxAmount
GET /transactions/{id} - get transaction with id
POST /transactions - adds a new transaction
PUT  /transactions/{id} - replaces the transaction with id
PATCH /transactions/{id} - supports changing the product and the amount
DELETE /transactions/{id} - deletes the transaction with id
GET /transactions/reports/type -> returns a map from type to sum of amount
GET /transactions/reports/product -> returns a map from product to sum of amount*/

public record Transaction(
		int id,
		String product,
		Type transactionType,
		double amount) {

}
