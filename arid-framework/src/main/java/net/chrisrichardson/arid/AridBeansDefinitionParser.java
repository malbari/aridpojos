package net.chrisrichardson.arid;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.Element;

public class AridBeansDefinitionParser implements BeanDefinitionParser {

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String packageName = element.getAttribute("package");
		String pattern = element.getAttribute("pattern");
		int autowire = AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR;
		BeanDefinitionParserDelegate delegate = parserContext
								.getDelegate();
		if (element.hasAttribute("autowire")) {
			autowire = delegate.getAutowireMode(
					element.getAttribute("autowire"));
		}
		String beanNameGeneratorName = element.getAttribute("name-generator");
		ResourcePatternResolver resourceLoader = (ResourcePatternResolver) parserContext
				.getReaderContext().getReader().getResourceLoader();
		BeanDefinitionRegistry registry = parserContext.getReaderContext()
				.getRegistry();
		AridBeanCreator aridBeanCreator = new AridBeanCreator(
				beanNameGeneratorName, resourceLoader, registry, delegate, parserContext.getReaderContext());
		aridBeanCreator.createBeans(element, packageName, pattern, autowire);
		return null;
	}
}
