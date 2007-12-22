package net.chrisrichardson.arid;

import net.chrisrichardson.arid.example.domain.Account;
import net.chrisrichardson.arid.example.domain.AccountRepository;
import net.chrisrichardson.arid.example.domain.MoneyTransferService;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class ExampleServiceTests extends AbstractDependencyInjectionSpringContextTests {

	private MoneyTransferService service;
	private AccountRepository repository;
	
	public void setRepository(AccountRepository repository) {
		this.repository = repository;
	}


	@Override
	protected String[] getConfigLocations() {
		return new String[]{"appctx/**/*.xml"};
	}
	
	
	public void setService(MoneyTransferService service) {
		this.service = service;
	}


	public void test() {
		assertNotNull(service);
	}

	public void testTransfer() throws Exception {
		double initialBalance1 = 10;
		double initialBalance2 = 20;

		Account account1 = new Account(initialBalance1);
		Account account2 = new Account(initialBalance2);
		
		repository.addAccount(account1);
		repository.addAccount(account2);

		int accountId1 = account1.getId();
		int accountId2 = account2.getId();
		
		service.transfer(accountId1, accountId2, 5);

		double balance1 = repository.findAccount(accountId1).getBalance();
		double balance2 = repository.findAccount(accountId2).getBalance();

		assertEquals(initialBalance1 - 5, balance1, 0.0);
		assertEquals(initialBalance2 + 5, balance2, 0.0);

	}
}
