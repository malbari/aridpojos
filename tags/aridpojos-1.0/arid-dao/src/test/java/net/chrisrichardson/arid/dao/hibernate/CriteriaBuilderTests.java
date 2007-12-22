package net.chrisrichardson.arid.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.jmock.integration.junit3.MockObjectTestCase;

public class CriteriaBuilderTests extends MockObjectTestCase {

	public class MyCriteriaBuilder extends CriteriaBuilder {

		public MyCriteriaBuilder(Class<?> entityClass, Object[] args) {
			super(entityClass, args);
		}

	}

	public void testGetKeywordsLongestFirst() {
		List<String> keywords = CriteriaBuilder.getKeywordsLongestFirst();
		assertTrue("First must be longer than second",
				keywords.get(0).length() >= keywords.get(1).length());
	}

	public void testFindAll() {
		CriteriaBuilder builder = new MyCriteriaBuilder(Foo.class,
				new Object[] {});
		DetachedCriteria query = builder.getCriteriaQuery();
		assertNotNull(query);

	}

	public void testLeftOverArguments() {
		CriteriaBuilder builder = new MyCriteriaBuilder(Foo.class,
				new Object[] { 1 });
		try {
			builder.getCriteriaQuery();
			fail();
		} catch (RuntimeException e) {

		}

	}

	public void testEqual() {
		CriteriaBuilder builder = new MyCriteriaBuilder(Foo.class,
				new Object[] {1});
		builder.processKeyword("Equal", "bar");
		DetachedCriteria query = builder.getCriteriaQuery();
		assertNotNull(query);

	}
}
