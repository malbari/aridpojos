package net.chrisrichardson.arid.dao.hibernate;


import net.sf.cglib.proxy.Enhancer;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.Assert;

public class GenericDAOFactoryBean implements FactoryBean, InitializingBean {

	private HibernateTemplate hibernateTemplate;

	private Class<?> objectType;

	Class<?> daoInterfaceOrClass;

	private ParameterizedGenericDaoLocator helper;

	public GenericDAOFactoryBean(Class<?> daoInterfaceOrClass) {
		this.objectType = daoInterfaceOrClass;
		this.daoInterfaceOrClass = daoInterfaceOrClass;
		this.helper = new ParameterizedGenericDaoLocator(daoInterfaceOrClass);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(hibernateTemplate, "Inject a SessionFactory or a HibernateTemplate");
		Assert.isTrue(helper.isValid(), "Must be an interface that extends GenericDao or a class that extends GenericHibernateDaoImpl and implements an interface that extends GenericDao");
	}


	public Class<?> getObjectType() {
		return objectType;
	}

	public boolean isSingleton() {
		return true;
	}

	public Object getObject() throws Exception {
		Class<?> daoSuperClass = helper.getDaoSuperClass();

		Class entityClass = helper.getEntityClass();

		GenericDAOHibernateImpl genericDao = (GenericDAOHibernateImpl) Enhancer
				.create(daoSuperClass, helper.getProxyInterfaces(),
						new GenericDaoMethodInterceptor(hibernateTemplate,
								entityClass));
		genericDao.setEntityClass(entityClass);
		genericDao.setHibernateTemplate(hibernateTemplate);
		genericDao.afterPropertiesSet();
		return genericDao;
	}

}
