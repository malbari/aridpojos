package net.chrisrichardson.arid.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import net.chrisrichardson.arid.domain.GenericDao;

import org.hibernate.SessionFactory;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

/*
 * This is a generic DAO
 */

public class GenericDAOHibernateImpl<T, U extends Serializable> extends HibernateDaoSupport implements
		GenericDao<T, U> {

	private Class type;

	private InvocationHelper invocationHelper;

	public GenericDAOHibernateImpl(Class type, SessionFactory sessionFactory) {
		this.type = type;
		setSessionFactory(sessionFactory);
		Assert.notNull(getHibernateTemplate());
		this.invocationHelper = new InvocationHelper(getHibernateTemplate(), type);
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

	public Object invoke(String methodName, Class[] parameterTypes,
			Class returnType, Object[] args) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		return invocationHelper.invoke(this, methodName, parameterTypes, returnType, args);
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
