package net.chrisrichardson.arid.dao;

import java.util.List;

import junit.framework.TestCase;
import net.chrisrichardson.arid.dao.exampledomain.Account;
import net.chrisrichardson.arid.dao.exampledomain.AccountRepository;
import net.chrisrichardson.arid.dao.hibernate.GenericDAOHibernateImpl;

import org.springframework.orm.hibernate3.HibernateTemplate;

public class ConcreteDaoTests extends TestCase {
  
  public class MyDao extends GenericDAOHibernateImpl<Account, Integer> implements AccountRepository {

    public Account findByAccountId(String accountId) {
      return null;
    }

    public List<Account> findByBalanceBetween(double lo, double hi) {
      return null;
    }

    public List<Account> findByBalanceGreaterThan(double lower) {
      return null;
    }
  }
  
  public void testGetEntityType() {
    MyDao dao = new MyDao();
    dao.setHibernateTemplate(new HibernateTemplate());
    dao.afterPropertiesSet();
    assertEquals(Account.class, dao.getEntityType());
  }
  
}
