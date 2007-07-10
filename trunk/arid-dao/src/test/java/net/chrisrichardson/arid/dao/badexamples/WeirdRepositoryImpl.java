package net.chrisrichardson.arid.dao.badexamples;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import net.chrisrichardson.arid.dao.exampledomain.Customer;
import net.chrisrichardson.arid.domain.GenericDao;

public abstract class WeirdRepositoryImpl extends HibernateDaoSupport
		implements GenericDao<Customer, Integer> {

	public List<Customer> findUsingComplexQuery() {
		// TODO Auto-generated method stub
		return null;
	}

}
