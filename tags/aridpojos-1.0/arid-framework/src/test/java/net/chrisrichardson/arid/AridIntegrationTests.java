package net.chrisrichardson.arid;

import junit.framework.TestCase;
import net.chrisrichardson.arid.beans1.Foo;
import net.chrisrichardson.arid.beans2.Bar;

import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AridIntegrationTests extends TestCase {

	public void testGood() {
		ApplicationContext appCtx = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		Foo foo = (Foo) appCtx.getBean("foo", Foo.class);
		assertEquals("FooBarBaz", foo.doit());
		assertNotNull(appCtx.getBean("bar", Bar.class));
	}

	public void testInvalidNameGenerator() {
		try {
			new ClassPathXmlApplicationContext(
					"applicationContextBadNameGenerator.xml");
			fail("Expected exception");
		} catch (BeanDefinitionParsingException e) {

		}
	}
}
