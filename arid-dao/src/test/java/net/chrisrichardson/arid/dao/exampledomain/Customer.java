package net.chrisrichardson.arid.dao.exampledomain;

public class Customer {

	private int id;

	private String customerId;

	Customer() {
	}

	public Customer(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public int getId() {
		return id;
	}
	
}
