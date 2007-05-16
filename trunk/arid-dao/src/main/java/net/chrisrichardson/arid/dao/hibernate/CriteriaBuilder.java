package net.chrisrichardson.arid.dao.hibernate;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class CriteriaBuilder {

	private final Object[] args;
	private DetachedCriteria criteria;
	private int argIndex = 0;
	static String[] KEYWORDS = { "GreaterThan", "And", "Between" };


	public CriteriaBuilder(Class entityClass, Object[] args) {
		this.args = args;
		criteria = DetachedCriteria.forClass(entityClass);
	}

	public DetachedCriteria getCriteriaQuery() {
		return criteria;
	}

	public void greaterThan(String propertyName) {
		criteria.add(Restrictions.ge(propertyName,
				args[argIndex++]));
	}

	public void and(String propertyName) {
		criteria.add(Restrictions.eq(propertyName,
				args[argIndex++]));
	}

	public void between(String propertyName) {
		criteria.add(Restrictions.ge(propertyName,
				args[argIndex++]));
		criteria.add(Restrictions.le(propertyName,
				args[argIndex++]));
	}

	public void eq(String propertyName) {
		criteria.add(Restrictions
				.eq(propertyName, args[argIndex++]));
	}

	void handleKeyword(String theKeyword, String propertyName) {
		if (theKeyword.equals("GreaterThan"))
			greaterThan(propertyName);
		else if (theKeyword.equals("And"))
			and(propertyName);
		else if (theKeyword.equals("Between")) {
			between(propertyName);
		} else
			throw new UnsupportedOperationException(
					"Don't know what to do with: " + theKeyword);
	}

}
