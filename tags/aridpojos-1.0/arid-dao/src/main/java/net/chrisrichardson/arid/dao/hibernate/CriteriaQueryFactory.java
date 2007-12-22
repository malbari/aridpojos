package net.chrisrichardson.arid.dao.hibernate;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.util.Assert;

public class CriteriaQueryFactory {

	private final String methodName;

	private final Class<?> entityClass;

	public CriteriaQueryFactory(Class<?> entityClass, String methodName) {
		this.entityClass = entityClass;
		this.methodName = methodName;
	}

	class KeywordAndIndex {
		int keywordStart = -1;
		String theKeyword = null;

		void process(String keyword, int thisKeywordStart) {
			if (thisKeywordStart != -1) {
				if (keywordStart == -1 || thisKeywordStart < keywordStart) {
					keywordStart = thisKeywordStart;
					theKeyword = keyword;
				}
			}
		}

		boolean isKeywordFound() {
			return keywordStart != -1;
		}

		int getIndexAfterEndOfKeyword() {
			return keywordStart + theKeyword.length();
		}
	}

	public DetachedCriteria makeCriteriaQuery(Object[] args) {
		CriteriaBuilder builder = makeCriteriaBuilder(args);
		if (methodName.equals("findAll")) {
			// done
		} else {
			Assert.isTrue(methodName.startsWith("findBy")
					|| methodName.startsWith("findRequiredBy"),
					"must start with findBy or findRequiredBy: " + methodName);

			int index = methodName.startsWith("findRequiredBy") ? "findRequiredBy"
					.length()
					: "findBy".length();

			while (index != methodName.length()) {
				KeywordAndIndex position = findNextKeyword(index);

				if (position.isKeywordFound()) {
					String propertyName = makePropertyName(methodName
							.substring(index, position.keywordStart));
					builder.processKeyword(position.theKeyword, propertyName);
					index = position.getIndexAfterEndOfKeyword();
				} else {
					String propertyName = makePropertyName(methodName.substring(index));
					builder.handleEq(propertyName);
					index = methodName.length();
				}
			}
		}
		return builder.getCriteriaQuery();
	}

	private KeywordAndIndex findNextKeyword(int index) {
		KeywordAndIndex position = new KeywordAndIndex();

		for (String keyword : CriteriaBuilder.getKeywordsLongestFirst()) {
			int thisKeywordStart = methodName.indexOf(keyword, index);
			position.process(keyword, thisKeywordStart);
		}
		return position;
	}

	protected CriteriaBuilder makeCriteriaBuilder(Object[] args) {
		return new CriteriaBuilder(entityClass, args);
	}

	private String makePropertyName(String string) {
		return string.substring(0, 1).toLowerCase() + string.substring(1);
	}

}
