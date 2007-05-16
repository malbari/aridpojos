package net.chrisrichardson.arid.dao.hibernate;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class VirtualMethodInvocation implements DaoMethodInvocation {

	private final HibernateTemplate hibernateTemplate;

	final Class entityClass;

	private CriteriaQueryFactory criteriaQueryFactory;

	private final Class returnType;

	public VirtualMethodInvocation(HibernateTemplate hibernateTemplate,
			Class entityClass, String methodName, Class returnType) {
		this.hibernateTemplate = hibernateTemplate;
		this.entityClass = entityClass;
		this.returnType = returnType;
		this.criteriaQueryFactory = new CriteriaQueryFactory(entityClass, methodName);
	}

	public Object invoke(Object[] args) {
		DetachedCriteria criteria = criteriaQueryFactory.makeCriteriaQuery(args);
		List result = hibernateTemplate.findByCriteria(criteria);
		if (Collection.class.isAssignableFrom(returnType))
			return result;
		else
			return DataAccessUtils.uniqueResult(result);
	}

}
