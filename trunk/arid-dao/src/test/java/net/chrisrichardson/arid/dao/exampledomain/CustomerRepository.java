package net.chrisrichardson.arid.dao.exampledomain;

import java.util.List;

import net.chrisrichardson.arid.domain.GenericDao;

public interface CustomerRepository extends GenericDao<Customer, Integer> {
	Customer findByCustomerId(String customerId);
	List<Customer> findUsingComplexQuery();
}
