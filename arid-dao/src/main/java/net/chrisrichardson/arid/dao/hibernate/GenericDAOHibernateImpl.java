package net.chrisrichardson.arid.dao.hibernate;

import java.io.Serializable;

import net.chrisrichardson.arid.domain.GenericDao;

import org.hibernate.SessionFactory;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/*
 * This is a generic DAO implementation
 */

public abstract class GenericDAOHibernateImpl<T, U extends Serializable> extends HibernateDaoSupport implements
		GenericDao<T, U> {

	private Class type;

	public GenericDAOHibernateImpl(Class type, SessionFactory sessionFactory) {
		this.type = type;
		setSessionFactory(sessionFactory);
	}
	
	public GenericDAOHibernateImpl() {
	}
	
	public void setEntityClass(Class entityClass) {
		type = entityClass;
	}

	public T findById(U pk) {
		return (T) getHibernateTemplate().get(type, pk);
	}

	public void add(T object) {
		getHibernateTemplate().save(object);
	}

	public Class getType() {
		return type;
	}

	public T findReferenceById(U pk) {
		return (T) getHibernateTemplate().load(type, pk);
	}

	public T findRequiredById(U pk) {
		T result = findById(pk);
		if (result == null) 
			throw new ObjectRetrievalFailureException(type, pk);
		return result;
	}

	public T merge(T object) {
		return (T) getHibernateTemplate().merge(object);
	}

	
}
