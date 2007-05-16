package net.chrisrichardson.arid.dao.hibernate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;

public class GenericDAOFactoryBean implements FactoryBean {

	private SessionFactory sessionFactory;
	
	private Class daoInterface;

	public GenericDAOFactoryBean(Class daoInterface) {
		this.daoInterface = daoInterface;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Class getObjectType() {
		return daoInterface;
	}

	public boolean isSingleton() {
		return true;
	}

	public Object getObject() throws Exception {
		ParameterizedType genericSuperclass = (ParameterizedType) daoInterface.getGenericInterfaces()[0];
		GenericDAOHibernateImpl genericDao = new GenericDAOHibernateImpl((Class)genericSuperclass.getActualTypeArguments()[0], sessionFactory);
		genericDao.afterPropertiesSet();
		
		return Proxy.newProxyInstance(this.getClass().getClassLoader(),
				new Class[] { daoInterface }, new GenericDaoInvocationHandler(
						genericDao));
	}

}
