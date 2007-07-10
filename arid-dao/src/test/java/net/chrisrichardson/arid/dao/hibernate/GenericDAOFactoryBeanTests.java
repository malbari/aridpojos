package net.chrisrichardson.arid.dao.hibernate;

import junit.framework.TestCase;
import net.chrisrichardson.arid.dao.badexamples.WeirdRepositoryImpl;
import net.chrisrichardson.arid.dao.exampledomain.Account;
import net.chrisrichardson.arid.dao.exampledomain.AccountRepository;
import net.chrisrichardson.arid.dao.exampledomain.Customer;
import net.chrisrichardson.arid.dao.exampledomain.CustomerRepository;
import net.chrisrichardson.arid.dao.exampledomain.hibernate.CustomerRepositoryImpl;

import org.springframework.orm.hibernate3.HibernateTemplate;

public class GenericDAOFactoryBeanTests extends TestCase {

	public void testWithInterface() throws Exception {
		GenericDAOFactoryBean gdfb = new GenericDAOFactoryBean(
				AccountRepository.class);
		gdfb.setHibernateTemplate(new HibernateTemplate());
		assertTrue(AccountRepository.class.isAssignableFrom(gdfb
				.getObjectType()));
		assertEquals(Account.class, ((AccountRepository) gdfb.getObject())
				.getType());
	}

	public void testWithClassThatExtendsInterface() throws Exception {
		GenericDAOFactoryBean gdfb = new GenericDAOFactoryBean(
				CustomerRepositoryImpl.class);
		gdfb.setHibernateTemplate(new HibernateTemplate());
		assertTrue(CustomerRepository.class.isAssignableFrom(gdfb
				.getObjectType()));
		assertEquals(Customer.class, ((CustomerRepository) gdfb.getObject())
				.getType());
	}

	public void testWithWeirdClassThatExtendsGenericDaoDirectly()
			throws Exception {
		GenericDAOFactoryBean gdfb = new GenericDAOFactoryBean(
				WeirdRepositoryImpl.class);
		gdfb.setHibernateTemplate(new HibernateTemplate());
		try {
			gdfb.afterPropertiesSet();
			fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {

		}
	}
}
