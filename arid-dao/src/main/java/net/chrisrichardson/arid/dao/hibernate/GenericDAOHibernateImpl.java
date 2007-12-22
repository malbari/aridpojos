package net.chrisrichardson.arid.dao.hibernate;

import java.io.Serializable;

import net.chrisrichardson.arid.domain.GenericDao;

import org.hibernate.SessionFactory;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

/*
 * This is a generic DAO implementation
 */

public abstract class GenericDAOHibernateImpl<T, U extends Serializable> extends HibernateDaoSupport implements
		GenericDao<T, U> {

	private Class<T> type;

  public GenericDAOHibernateImpl() {
  }
  
	public GenericDAOHibernateImpl(Class<T> type, SessionFactory sessionFactory) {
	  this();
		this.type = type;
		setSessionFactory(sessionFactory);
	}
	
	public GenericDAOHibernateImpl(SessionFactory sessionFactory) {
    this();
	  setSessionFactory(sessionFactory);
	}
	
  public GenericDAOHibernateImpl(HibernateTemplate hibernateTemplate) {
    this();
    setHibernateTemplate(hibernateTemplate);
  }
  
  private Class<T> determineType() {
	  ParameterizedGenericDaoLocator locator = new ParameterizedGenericDaoLocator(getClass());
    return locator.getEntityClass();
  }

	@Override
	protected void initDao() throws Exception {
	  super.initDao();
	  if (type == null) 
	    type = determineType();
	}
	
	public void setEntityClass(Class<T> entityClass) {
		this.type = entityClass;
	}

	public T findById(U pk) {
		return (T) getHibernateTemplate().get(getEntityType(), pk);
	}

	public void add(T object) {
		getHibernateTemplate().save(object);
	}

	public Class<T> getEntityType() {
		Assert.notNull(type, "Entity type for generic Dao not set");
		return type;
	}

	public T findReferenceById(U pk) {
		return (T) getHibernateTemplate().load(getEntityType(), pk);
	}

	public T findRequiredById(U pk) {
		T result = findById(pk);
		if (result == null) 
			throw new ObjectRetrievalFailureException(getEntityType(), pk);
		return result;
	}

	public T merge(T object) {
		return (T) getHibernateTemplate().merge(object);
	}
	
	protected String makeScopedQueryName(String name) {
		return getEntityType().getName() + "." + name;
	}

	public void delete(T object) {
		getHibernateTemplate().delete(object);
	}


}
