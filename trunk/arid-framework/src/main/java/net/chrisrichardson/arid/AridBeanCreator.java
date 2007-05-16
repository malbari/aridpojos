package net.chrisrichardson.arid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.aop.aspectj.TypePatternClassFilter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.ReaderContext;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AridBeanCreator {

	private AridBeanNameGenerator beanNameGenerator = new DefaultAridBeanNameGenerator();

	ResourcePatternResolver rl;

	private BeanDefinitionRegistry registry;

	private BeanDefinitionParserDelegate delegate;

	private final ReaderContext readerContext;

	private BeanDefinitionGenerator beanDefinitionGenerator = new DefaultBeanDefinitionGenerator();
	
	private PackageScanner packageScanner = new ConcreteClassPackageScanner();
	
	public AridBeanCreator(ResourcePatternResolver resourcePatternResolver,
			BeanDefinitionRegistry beanDefinitionRegistry,
			BeanDefinitionParserDelegate parserDelegate,
			ReaderContext readerContext) {
		this.readerContext = readerContext;
		rl = resourcePatternResolver;
		registry = beanDefinitionRegistry;
		delegate = parserDelegate;
	}

	
	public void setBeanNameGenerator(AridBeanNameGenerator beanNameGenerator) {
		this.beanNameGenerator = beanNameGenerator;
	}


	public void setBeanDefinitionGenerator(
			BeanDefinitionGenerator beanDefinitionGenerator) {
		this.beanDefinitionGenerator = beanDefinitionGenerator;
	}

	public void setPackageScanner(PackageScanner packageScanner) {
		this.packageScanner = packageScanner;
	}

	void createBeans(Element element, String packageName, String pattern) {
		List<Class> classNames = packageScanner.getClasses(this, packageName);
		List<Class> classes = getMatchingClasses(pattern, classNames);
		Map<String, BeanDefinition> overrides = parseOverrides(element);
		createBeanDefinitions(classes, overrides);
	}

	Map<String, BeanDefinition> parseOverrides(Element element) {
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if ("extras".equals(node.getLocalName())) {
				return parseSpringBeans(node);
			}
		}
		return Collections.EMPTY_MAP;
	}

	Map<String, BeanDefinition> parseSpringBeans(Node springBeansNode) {
		Map<String, BeanDefinition> result = new HashMap<String, BeanDefinition>();
		for (int i = 0; i < springBeansNode.getChildNodes().getLength(); i++) {
			Node node = springBeansNode.getChildNodes().item(i);
			if ("bean".equals(node.getLocalName())) {
				BeanDefinitionHolder beanDefinition = delegate
						.parseBeanDefinitionElement((Element) node);
				result.put(beanDefinition.getBeanName(), beanDefinition
						.getBeanDefinition());
			}
		}
		return result;
	}

	void fatal(Throwable e) {
		readerContext.fatal(e.getMessage(), null, e);
	}

	List<Class> getMatchingClasses(String pattern, List<Class> classes) {
		List<Class> result = new ArrayList<Class>();
		if (pattern == null || pattern.trim().equals("")) {
			return classes;
		} else {
			TypePatternClassFilter filter = new TypePatternClassFilter(pattern);
			for (Class type : classes) {
				if (filter.matches(type))
					result.add(type);
			}
		}
		return result;
	}

	void createBeanDefinitions(List<Class> classes, Map<String, BeanDefinition> overrides) {
		for (Class beanClass : classes)
			createBeanDefinition(overrides, beanClass);

	}

	void createBeanDefinition(Map<String, BeanDefinition> overrides,
			Class beanClass) {
		{
			String beanName = beanNameGenerator.getBeanName(beanClass);
			AbstractBeanDefinition beanDefinition = beanDefinitionGenerator.makeBeanDefinition(beanClass);
			BeanDefinition overridingBeanDefinition = overrides.get(beanName);
			if (overridingBeanDefinition != null) {
				((AbstractBeanDefinition) overridingBeanDefinition)
						.overrideFrom(beanDefinition);
				beanDefinition = (AbstractBeanDefinition) overridingBeanDefinition;
			}
			registry.registerBeanDefinition(beanName, beanDefinition);
		}
	}

}
