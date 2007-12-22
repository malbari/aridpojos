package net.chrisrichardson.arid.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

public class NamedQueryInvocation implements DaoMethodInvocation {

	private final HibernateTemplate hibernateTemplate;

	private final Class entityClass;

	private final String namedQuery;

	public NamedQueryInvocation(HibernateTemplate hibernateTemplate,
			Class entityClass, String methodName) {
		this.hibernateTemplate = hibernateTemplate;
		this.entityClass = entityClass;
		this.namedQuery = methodName;
	}

	public List invoke(final Object[] args) {
		String queryName = entityClass.getName() + "." + namedQuery;
		return hibernateTemplate.findByNamedQuery(queryName, args);
	}

}
