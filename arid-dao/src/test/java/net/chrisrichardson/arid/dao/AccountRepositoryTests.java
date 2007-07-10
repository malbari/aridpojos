package net.chrisrichardson.arid.dao;

import java.util.List;

import net.chrisrichardson.arid.dao.exampledomain.Account;
import net.chrisrichardson.arid.dao.exampledomain.AccountRepository;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class AccountRepositoryTests extends
		AbstractDependencyInjectionSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		return new String[]{"applicationContext.xml"};
	}
	
	private AccountRepository accountRepository;
	private Account a;

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		a = new Account(generateAccountId(), 10.0);
	}
	
	private String generateAccountId() {
		return "Foo." + System.nanoTime();
	}

	public void testNaming() {
		assertNotNull(applicationContext.getBean("accountRepository", AccountRepository.class));
	}
	
	public void testBasic() {
		accountRepository.add(a);
		
		Account b = accountRepository.findById(a.getId());
		assertNotNull(b);
	}

	public void testFindByBalanceGreaterThan() {
		accountRepository.add(a);
		
		List<Account> accounts = accountRepository.findByBalanceGreaterThan(0.5);
		assertFalse(accounts.isEmpty());
	}

	public void testFindByAccountId() {
		accountRepository.add(a);
		
		Account b = accountRepository.findByAccountId(a.getAccountId());
		assertNotNull(b);
	}
	
	public void testFindByBalanceBetween() {
		accountRepository.add(a);
		
		List<Account> accounts = accountRepository.findByBalanceBetween(0.5, 10.0);
		assertFalse(accounts.isEmpty());
	}

	public void testFindByBalanceBetweenTwice() {
		accountRepository.add(a);
		
		assertFalse(accountRepository.findByBalanceBetween(0.5, 10.0).isEmpty());
		assertFalse(accountRepository.findByBalanceBetween(0.5, 10.0).isEmpty());
	}

		
}
