package net.chrisrichardson.arid.dao.exampledomain;

import java.util.List;

import net.chrisrichardson.arid.domain.GenericDao;

public interface CustomerRepository extends GenericDao<Customer, Integer> {
	Customer findByCustomerId(String customerId);
	List<Customer> findUsingComplexQuery();
	Customer findUsingSomeStrangeNamedQuery(String firstName, String lastName);
	
	Customer findRequiredUsingSomeStrangeNamedQuery(String firstName, String lastName);
	List<Customer> findRequiredByCustomerId(String customerId);
}
