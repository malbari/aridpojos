package net.chrisrichardson.arid.dao.hibernate;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

import net.sf.cglib.proxy.Enhancer;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class GenericDAOFactoryBean implements FactoryBean {

	private HibernateTemplate hibernateTemplate;

	private Class daoInterface;

	private Class daoSuperClass = GenericDAOHibernateImpl.class;

	public GenericDAOFactoryBean(Class daoInterface) {
		if (Modifier.isInterface(daoInterface.getModifiers()))
			this.daoInterface = daoInterface;
		else {
			this.daoSuperClass = daoInterface;
			this.daoInterface = daoInterface.getInterfaces()[0];
		}
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public Class getObjectType() {
		return daoInterface;
	}

	public boolean isSingleton() {
		return true;
	}

	public Object getObject() throws Exception {
		ParameterizedType genericSuperclass = (ParameterizedType) daoInterface
				.getGenericInterfaces()[0];
		Class entityClass = (Class) genericSuperclass.getActualTypeArguments()[0];

		GenericDAOHibernateImpl genericDao = (GenericDAOHibernateImpl) Enhancer
				.create(daoSuperClass, new Class[] { daoInterface },
						new GenericDaoMethodInterceptor(hibernateTemplate,
								entityClass));
		genericDao.setEntityClass(entityClass);
		genericDao.setHibernateTemplate(hibernateTemplate);
		genericDao.afterPropertiesSet();
		return genericDao;
	}

}
