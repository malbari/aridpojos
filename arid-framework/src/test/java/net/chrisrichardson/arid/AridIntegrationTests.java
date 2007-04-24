package net.chrisrichardson.arid;

import net.chrisrichardson.arid.beans1.Foo;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class AridIntegrationTests extends AbstractDependencyInjectionSpringContextTests {
	
	private Foo foo;
	
	@Override
	protected String[] getConfigLocations() {
		return new String[]{"applicationContext.xml"};
	}

	public void setFoo(Foo foo) {
		this.foo = foo;
	}

	public void test() {
		assertEquals("FooBarBaz", foo.doit());
	}
}
