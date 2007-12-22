package net.chrisrichardson.arid;

import org.springframework.beans.factory.support.AbstractBeanDefinition;

/**
 * Creates a bean definition for a class
 * @author cer
 *
 */
public interface BeanDefinitionGenerator {

	public void setAutoWire(int autowire);

	public void setParentBeanName(String parentBeanName);

	/**
	 * Creates a bean definition for the specified class
	 * @param beanClass
	 * @return
	 */
	public AbstractBeanDefinition makeBeanDefinition(Class beanClass);


}