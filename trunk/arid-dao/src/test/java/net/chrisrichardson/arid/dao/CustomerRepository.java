package net.chrisrichardson.arid.dao;

import net.chrisrichardson.arid.domain.GenericDao;

public interface CustomerRepository extends GenericDao<Customer, Integer> {
	Customer findByCustomerId(String customerId);
}
