package net.chrisrichardson.arid;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

/**
 * Creates an Spring bean that instantitates the bean class and initializes it using autowiring
 * @author cer
 *
 */

public class DefaultBeanDefinitionGenerator extends AbstractBeanDefinitionGenerator {

	public AbstractBeanDefinition makeBeanDefinition(Class beanClass) {
		
		assert parentBeanName == null;
		
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
				.rootBeanDefinition(beanClass);
		beanDefinitionBuilder.setAutowireMode(autowire);
		AbstractBeanDefinition beanDefinition = beanDefinitionBuilder
				.getBeanDefinition();
		return beanDefinition;
	}

}
