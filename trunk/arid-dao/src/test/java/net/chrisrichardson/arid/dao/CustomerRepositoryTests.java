package net.chrisrichardson.arid.dao;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class CustomerRepositoryTests extends
		AbstractDependencyInjectionSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		return new String[]{"applicationContext.xml"};
	}
	
	private CustomerRepository customerRepository;
	private Customer c;

	public void setAccountRepository(CustomerRepository customerRepository) {
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
	
		
}
