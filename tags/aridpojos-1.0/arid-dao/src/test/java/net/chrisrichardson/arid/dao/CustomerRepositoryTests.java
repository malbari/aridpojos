package net.chrisrichardson.arid.dao;

import java.util.List;

import net.chrisrichardson.arid.dao.exampledomain.Customer;
import net.chrisrichardson.arid.dao.exampledomain.CustomerRepository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
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
		c = new Customer(generateCustomerId(), "John", "Done");
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
	
	public void testFindUsingNamedQuery() {
		String lastName = "Done" + generateCustomerId();
		customerRepository.add(new Customer(generateCustomerId(), "John", lastName));
		Customer customer = customerRepository.findUsingSomeStrangeNamedQuery("John", lastName);
		assertNotNull(customer);
		assertNull(customerRepository.findUsingSomeStrangeNamedQuery("Mary", lastName));
	}

	public void testFindRequiredUsingNamedQuery() {
		String lastName = "Done" + generateCustomerId();
		customerRepository.add(new Customer(generateCustomerId(), "John", lastName));
		Customer customer = customerRepository.findRequiredUsingSomeStrangeNamedQuery("John", lastName);
		assertNotNull(customer);
		try {
			customerRepository.findRequiredUsingSomeStrangeNamedQuery("Mary", lastName);
			fail("expected: " + EmptyResultDataAccessException.class.getName());
		} catch (EmptyResultDataAccessException e) {
			
		}
	}
	
	public void testFindRequiredByCustomerId() {
		customerRepository.add(c);
		
		assertFalse(customerRepository.findRequiredByCustomerId(c.getCustomerId()).isEmpty());

		try {
			customerRepository.findRequiredByCustomerId("foobar");
			fail("expected: " + EmptyResultDataAccessException.class.getName());
		} catch (IncorrectResultSizeDataAccessException e) {
		}
	}
		
}
