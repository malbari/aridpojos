package net.chrisrichardson.arid.dao.hibernate;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

import org.springframework.util.Assert;

import net.chrisrichardson.arid.domain.GenericDao;

public class ParameterizedGenericDaoLocator {

	private final Class<?> daoInterfaceOrClass;

	public ParameterizedGenericDaoLocator(Class<?> daoInterfaceOrClass) {
		this.daoInterfaceOrClass = daoInterfaceOrClass;
	}

	public ParameterizedType locate() {
		return findParameterizedGenericDao();
	}

	private ParameterizedType findParameterizedGenericDao() {

		if (isInterface()) {
			return findParameterizedGenericDao(daoInterfaceOrClass);
		} else {
			ParameterizedType directSubinterfaceOfGenericDao = null;
			for (Class currentConcreteClass = daoInterfaceOrClass; currentConcreteClass != Object.class
					&& directSubinterfaceOfGenericDao == null; currentConcreteClass = currentConcreteClass
					.getSuperclass()) {
				directSubinterfaceOfGenericDao = findParameterizedGenericDao(currentConcreteClass
						.getInterfaces());
			}
			return directSubinterfaceOfGenericDao;
		}
	}

	private boolean isInterface() {
		return Modifier.isInterface(daoInterfaceOrClass.getModifiers());
	}

	private ParameterizedType findParameterizedGenericDao(Class[] interfaces) {
		for (Class<?> iface : interfaces) {
			ParameterizedType genericDaoInterface = findParameterizedGenericDao(iface);
			if (genericDaoInterface != null)
				return genericDaoInterface;
		}
		return null;
	}

	private ParameterizedType findParameterizedGenericDao(Class<?> iface) {
		int i = 0;
		for (Class<?> superInterface : iface.getInterfaces()) {
			if (superInterface == GenericDao.class)
				return (ParameterizedType) iface.getGenericInterfaces()[i];
			i++;
		}
		return findParameterizedGenericDao(iface.getInterfaces());
	}

	public boolean isValid() {
		if (isInterface()) {
			return isParameterizedGenericDao();
		} else {
			return GenericDAOHibernateImpl.class.isAssignableFrom(daoInterfaceOrClass)
			 && isParameterizedGenericDao();
		}
	}

	private boolean isParameterizedGenericDao() {
		return locate() != null;
	}

	public Class getEntityClass() {
		
		ParameterizedType parameterizedGenericDao = locate();
		Assert.notNull(parameterizedGenericDao, "Couldn't find parameterized GenericDao");
		
		return (Class) parameterizedGenericDao.getActualTypeArguments()[0];
		
	}

	public Class[] getProxyInterfaces() {
		if (isInterface()) {
			return new Class[]{daoInterfaceOrClass};
		} else {
			return daoInterfaceOrClass.getInterfaces();
		}
	}

	public Class<GenericDAOHibernateImpl> getDaoSuperClass() {
		if (isInterface()) {
			return GenericDAOHibernateImpl.class;
		} else {
			return (Class<GenericDAOHibernateImpl>)daoInterfaceOrClass;
		}
	}

}
