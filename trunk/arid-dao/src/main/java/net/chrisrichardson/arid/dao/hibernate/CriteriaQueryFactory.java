package net.chrisrichardson.arid.dao.hibernate;

import org.hibernate.criterion.DetachedCriteria;

public class CriteriaQueryFactory {

	private final String methodName;

	private final Class entityClass;

	public CriteriaQueryFactory(Class entityClass, String methodName) {
		this.entityClass = entityClass;
		this.methodName = methodName;
	}


	public DetachedCriteria makeCriteriaQuery(Object[] args) {
		CriteriaBuilder builder = new CriteriaBuilder(entityClass, args);
		if (methodName.equals("findAll")) {
			// done
		} else {
			int index = "findBy".length();

			while (index != methodName.length()) {
				int keywordStart = -1;
				String theKeyword = null;

				for (String keyword : CriteriaBuilder.KEYWORDS) {
					keywordStart = methodName.indexOf(keyword, index);
					if (keywordStart != -1) {
						theKeyword = keyword;
						break;
					}
				}
				if (keywordStart != -1) {
					String propertyName = makePropertyName(methodName
							.substring(index, keywordStart));
					builder.handleKeyword(theKeyword, propertyName);
					index = keywordStart + theKeyword.length();
				} else {
					String propertyName = methodName
							.substring(index, index + 1).toLowerCase()
							+ methodName.substring(index + 1, methodName
									.length());
					builder.eq(propertyName);
					index = methodName.length();
				}
			}
		}
		return builder.getCriteriaQuery();
	}

	private String makePropertyName(String string) {
		return string.substring(0, 1).toLowerCase() + string.substring(1);
	}

}
