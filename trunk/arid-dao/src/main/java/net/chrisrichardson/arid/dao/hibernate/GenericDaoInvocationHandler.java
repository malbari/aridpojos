package net.chrisrichardson.arid.dao.hibernate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class GenericDaoInvocationHandler implements InvocationHandler {

	private GenericDAOHibernateImpl genericDao;

	public GenericDaoInvocationHandler(GenericDAOHibernateImpl genericDao) {
		this.genericDao = genericDao;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		return genericDao.invoke(method.getName(), method.getParameterTypes(), method.getReturnType(), args);
	}

}
