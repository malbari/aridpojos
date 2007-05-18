package net.chrisrichardson.arid.dao.exampledomain.hibernate;

import java.util.List;

import net.chrisrichardson.arid.dao.exampledomain.Customer;
import net.chrisrichardson.arid.dao.exampledomain.CustomerRepository;
import net.chrisrichardson.arid.dao.hibernate.GenericDAOHibernateImpl;

public abstract class CustomerRepositoryImpl extends GenericDAOHibernateImpl<Customer, Integer>
	implements CustomerRepository {
	
	public List<Customer> findUsingComplexQuery() {
		// TODO Auto-generated method stub
		return null;
	}

}
