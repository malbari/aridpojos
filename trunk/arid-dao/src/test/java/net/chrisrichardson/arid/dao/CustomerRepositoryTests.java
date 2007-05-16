package net.chrisrichardson.arid.dao;

import java.util.List;

import net.chrisrichardson.arid.dao.exampledomain.AccountRepository;
import net.chrisrichardson.arid.dao.exampledomain.Customer;
import net.chrisrichardson.arid.dao.exampledomain.CustomerRepository;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class CustomerRepositoryTests extends
		AbstractDependencyInjectionSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		return new String[]{"applicationContext.xml"};
	}
	
	private CustomerRepository customerRepository;
	private Customer c;

	public void setCustomerRepository(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		c = new Customer(generateCustomerId());
	}
	
	private String generateCustomerId() {
		return "Foo." + System.nanoTime();
	}
	
	public void testNaming() {
		assertNotNull(applicationContext.getBean("customerRepository", CustomerRepository.class));
	}

	public void testBasic() {
		customerRepository.add(c);
		
		Customer b = customerRepository.findById(c.getId());
		assertNotNull(b);
	}

	public void testFindByCustomerId() {
		customerRepository.add(c);
		
		Customer b = customerRepository.findByCustomerId(c.getCustomerId());
		assertNotNull(b);
	}
	
	public void testFindUsingComplexQuery() {
		List<Customer> customers = customerRepository.findUsingComplexQuery();
		assertNull(customers);
	}
	
		
}
