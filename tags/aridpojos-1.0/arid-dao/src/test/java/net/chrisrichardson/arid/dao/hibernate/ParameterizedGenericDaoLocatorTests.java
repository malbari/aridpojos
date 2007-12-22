package net.chrisrichardson.arid.dao.hibernate;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

import junit.framework.TestCase;
import net.chrisrichardson.arid.dao.exampledomain.Account;
import net.chrisrichardson.arid.dao.exampledomain.AccountRepository;
import net.chrisrichardson.arid.dao.exampledomain.Customer;
import net.chrisrichardson.arid.dao.exampledomain.CustomerRepository;
import net.chrisrichardson.arid.dao.exampledomain.hibernate.CustomerRepositoryImpl;
import net.chrisrichardson.arid.domain.GenericDao;

public class ParameterizedGenericDaoLocatorTests extends TestCase {

	public void testSimpleInterface() {
		ParameterizedGenericDaoLocator locator = new ParameterizedGenericDaoLocator(
				AccountRepository.class);
		assertParameterizedGenericDao(locator, Account.class, GenericDAOHibernateImpl.class, new Class[]{AccountRepository.class});
	}

	private void assertParameterizedGenericDao(
			ParameterizedGenericDaoLocator locator, Class<?> entityType, Class<?> daoSuperClass, Class<?>[] proxyInterfaces) {
		ParameterizedType parameterizedType = locator.locate();
		assertNotNull(parameterizedType);
		assertEquals(GenericDao.class, parameterizedType.getRawType());
		assertEquals(entityType, locator.getEntityClass());
		assertEquals(daoSuperClass, locator.getDaoSuperClass());
		assertTrue(Arrays.equals(proxyInterfaces, locator.getProxyInterfaces()));
	}

	interface MyAccountRepository extends AccountRepository {

	}

	public void testIndirectInterface() {

		ParameterizedGenericDaoLocator locator = new ParameterizedGenericDaoLocator(
				MyAccountRepository.class);
		assertParameterizedGenericDao(locator, Account.class, GenericDAOHibernateImpl.class, new Class[]{MyAccountRepository.class});
	}

	public void testSimpleClass() {
		ParameterizedGenericDaoLocator locator = new ParameterizedGenericDaoLocator(
				CustomerRepositoryImpl.class);
		assertParameterizedGenericDao(locator, Customer.class, CustomerRepositoryImpl.class, CustomerRepositoryImpl.class.getInterfaces());
	}

	public void testIndirectSubclassClass() {
		abstract class MyCustomerRepositoryImpl extends CustomerRepositoryImpl {

		}

		ParameterizedGenericDaoLocator locator = new ParameterizedGenericDaoLocator(
				MyCustomerRepositoryImpl.class);
		assertParameterizedGenericDao(locator, Customer.class, MyCustomerRepositoryImpl.class, MyCustomerRepositoryImpl.class.getInterfaces());
	}

	interface FooInterface {

	}

	public void testClassWithMultipleInterfaces() {
		abstract class CustomerRepositoryImpl extends
				GenericDAOHibernateImpl<Customer, Integer> implements
				FooInterface, CustomerRepository {

		}
		ParameterizedGenericDaoLocator locator = new ParameterizedGenericDaoLocator(
				CustomerRepositoryImpl.class);

		assertParameterizedGenericDao(locator, Customer.class, CustomerRepositoryImpl.class, CustomerRepositoryImpl.class.getInterfaces());

	}
}
