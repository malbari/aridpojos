package net.chrisrichardson.arid;

/**
 * Generates a bean name for the specified class
 * @author cer
 *
 */
public interface AridBeanNameGenerator {

	String getBeanName(Class beanClass);

}
