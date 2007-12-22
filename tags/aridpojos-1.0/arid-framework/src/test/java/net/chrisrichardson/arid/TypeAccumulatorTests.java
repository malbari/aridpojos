package net.chrisrichardson.arid;

import java.util.List;

import junit.framework.TestCase;

public class TypeAccumulatorTests extends TestCase {

	private TypeAccumulator accumulator;

	@Override
	protected void setUp() throws Exception {
		accumulator = new TypeAccumulator();
	}
	
	class A{};
	class B{};
	class C extends B {};
	
	public void test_simple() {
		
		accumulator.add(A.class);
		accumulator.add(B.class);
		
		List<Class> result = accumulator.asList();
		assertEquals(2, result.size());
		assertTrue(result.contains(A.class));
		assertTrue(result.contains(B.class));
	}

	public void test_derivedFirst() {
		accumulator.add(C.class);
		accumulator.add(A.class);
		accumulator.add(B.class);
		
		List<Class> result = accumulator.asList();
		assertEquals(2, result.size());
		assertTrue(result.contains(A.class));
		assertTrue(result.contains(C.class));
	}

	public void test_derivedLast() {
		accumulator.add(A.class);
		accumulator.add(B.class);
		accumulator.add(C.class);
		
		List<Class> result = accumulator.asList();
		assertEquals(2, result.size());
		assertTrue(result.contains(A.class));
		assertTrue(result.contains(C.class));
	}
}
