package net.chrisrichardson.arid;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

/**
 * Creates a child bean of the specified parentBean that has the class as a constructor argument
 * 
 * @author cer
 *
 */
public class ChildWithConstructorArgBeanGenerator extends AbstractBeanDefinitionGenerator {

	public AbstractBeanDefinition makeBeanDefinition(Class beanClass) {
		assert autowire == AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR;
		
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
				.childBeanDefinition(parentBeanName);
		beanDefinitionBuilder.addConstructorArg(beanClass);
		AbstractBeanDefinition beanDefinition = beanDefinitionBuilder
				.getBeanDefinition();
		return beanDefinition;
	}

}
