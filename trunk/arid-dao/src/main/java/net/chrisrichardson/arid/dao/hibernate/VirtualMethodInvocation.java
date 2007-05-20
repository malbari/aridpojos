package net.chrisrichardson.arid.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class VirtualMethodInvocation implements DaoMethodInvocation {

	private final HibernateTemplate hibernateTemplate;

	final Class entityClass;

	private CriteriaQueryFactory criteriaQueryFactory;


	public VirtualMethodInvocation(HibernateTemplate hibernateTemplate,
			Class entityClass, String methodName) {
		this.hibernateTemplate = hibernateTemplate;
		this.entityClass = entityClass;
		this.criteriaQueryFactory = new CriteriaQueryFactory(entityClass, methodName);
	}

	public List invoke(Object[] args) {
		DetachedCriteria criteria = criteriaQueryFactory.makeCriteriaQuery(args);
		return hibernateTemplate.findByCriteria(criteria);
	}

}
