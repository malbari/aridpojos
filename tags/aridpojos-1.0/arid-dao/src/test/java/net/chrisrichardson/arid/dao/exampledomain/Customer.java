package net.chrisrichardson.arid.dao.exampledomain;

public class Customer {

	private int id;

	private String customerId;

	private String firstName;
	private String lastName;
	
	Customer() {
	}

	public Customer(String customerId, String firstName, String lastName) {
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	
	
}
