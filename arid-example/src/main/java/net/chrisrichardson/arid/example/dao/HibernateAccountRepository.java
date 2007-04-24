package net.chrisrichardson.arid.example.dao;

import net.chrisrichardson.arid.example.annotations.RepositoryImpl;
import net.chrisrichardson.arid.example.domain.Account;
import net.chrisrichardson.arid.example.domain.AccountRepository;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

@RepositoryImpl
public class HibernateAccountRepository extends HibernateDaoSupport implements AccountRepository {

	public void addAccount(Account account) {
		getHibernateTemplate().save(account);
	}

	public Account findAccount(int accountId) {
		return (Account) getHibernateTemplate().get(Account.class, new Integer(accountId));
	}

}
