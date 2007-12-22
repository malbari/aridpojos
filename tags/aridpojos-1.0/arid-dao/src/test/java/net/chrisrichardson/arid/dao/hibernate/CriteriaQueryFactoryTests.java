package net.chrisrichardson.arid.dao.hibernate;

import org.hibernate.criterion.DetachedCriteria;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.jmock.lib.legacy.ClassImposteriser;

public class CriteriaQueryFactoryTests extends MockObjectTestCase {

	private CriteriaBuilder criteriaBuilder;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setImposteriser(ClassImposteriser.INSTANCE);
		criteriaBuilder = mock(CriteriaBuilder.class); 
	}
	
	class MyCriteriaQueryFactory extends CriteriaQueryFactory {

		public MyCriteriaQueryFactory(Class<?> entityClass, String methodName) {
			super(entityClass, methodName);
		}
		
		@Override
		protected CriteriaBuilder makeCriteriaBuilder(Object[] args) {
			super.makeCriteriaBuilder(args);
			return criteriaBuilder;
		}
		
	}
	
	public void testFindAll() {
		CriteriaQueryFactory factory = new MyCriteriaQueryFactory(Foo.class, "findAll");
		checking(new Expectations() {{
			one(criteriaBuilder).getCriteriaQuery();
		}}
		);
		DetachedCriteria result = factory.makeCriteriaQuery(new Object[]{"x", 1, 5});
		assertNotNull(result);
		verify();
	}

	public void testMultiple() {
		CriteriaQueryFactory factory = new MyCriteriaQueryFactory(Foo.class, "findByBarEqualBazBetween");
		checking(new Expectations() {{
			one(criteriaBuilder).processKeyword("Equal", "bar");
			one(criteriaBuilder).processKeyword("Between", "baz");
			one(criteriaBuilder).getCriteriaQuery();
		}}
		);
		DetachedCriteria result = factory.makeCriteriaQuery(new Object[]{"x", 1, 5});
		assertNotNull(result);
    verify();
	}

	public void testFindByJustProperty() {
		CriteriaQueryFactory factory = new MyCriteriaQueryFactory(Foo.class, "findByBar");
		checking(new Expectations() {{
			one(criteriaBuilder).handleEq("bar");
			one(criteriaBuilder).getCriteriaQuery();
		}}
		);
		DetachedCriteria result = factory.makeCriteriaQuery(new Object[]{"x"});
		assertNotNull(result);
    verify();
	}

	public void testFindRequiredByJustProperty() {
		CriteriaQueryFactory factory = new MyCriteriaQueryFactory(Foo.class, "findRequiredByBar");
		checking(new Expectations() {{
			one(criteriaBuilder).handleEq("bar");
			one(criteriaBuilder).getCriteriaQuery();
		}}
		);
		DetachedCriteria result = factory.makeCriteriaQuery(new Object[]{"x"});
		assertNotNull(result);
    verify();
	}

	public void testTwoThings() {
		CriteriaQueryFactory factory = new MyCriteriaQueryFactory(Foo.class, "findByBarEqualBaz");
		checking(new Expectations() {{
			one(criteriaBuilder).processKeyword("Equal", "bar");
			one(criteriaBuilder).handleEq("baz");
			one(criteriaBuilder).getCriteriaQuery();
		}}
		);
		DetachedCriteria result = factory.makeCriteriaQuery(new Object[]{"x", 1, 5});
		assertNotNull(result);
		verify();
	}

}
