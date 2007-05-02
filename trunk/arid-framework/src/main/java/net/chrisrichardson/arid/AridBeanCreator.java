package net.chrisrichardson.arid;

import java.io.IOException;
import java.lang.reflect.Modifier;
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
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AridBeanCreator {

	private AridBeanNameGenerator beanNameGenerator;

	private ResourcePatternResolver rl;

	private BeanDefinitionRegistry registry;

	private BeanDefinitionParserDelegate delegate;

	private final ReaderContext readerContext;

	public AridBeanCreator(String beanNameGeneratorName,
			ResourcePatternResolver resourcePatternResolver,
			BeanDefinitionRegistry beanDefinitionRegistry,
			BeanDefinitionParserDelegate parserDelegate, ReaderContext readerContext) {
		this.readerContext = readerContext;
		beanNameGenerator = getBeanNameGenerator(beanNameGeneratorName);
		rl = resourcePatternResolver;
		registry = beanDefinitionRegistry;
		delegate = parserDelegate;
	}

	void createBeans(Element element, String packageName, String pattern,
			int autowire) {
		List<Class> classNames = getClasses(packageName);
		List<Class> classes = getMatchingClasses(pattern, classNames);
		Map<String, BeanDefinition> overrides = parseOverrides(element);
		createBeanDefinitions(classes, autowire, overrides);
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

	AridBeanNameGenerator getBeanNameGenerator(String attribute) {
		if (attribute == null || attribute.trim().equals(""))
			attribute = DefaultAridBeanNameGenerator.class.getName();
		try {
			return (AridBeanNameGenerator) Class.forName(attribute)
					.newInstance();
		} catch (InstantiationException e) {
			fatal(e);
			return null;
		} catch (IllegalAccessException e) {
			fatal(e);
			return null;
		} catch (ClassNotFoundException e) {
			fatal(e);
			return null;
		}
	}

	private void fatal(Throwable e) {
		readerContext.fatal(e.getMessage(), null, e);
	}

	List<Class> getClasses(String packageName) {
		List<Class> result = new ArrayList<Class>();
		try {
			String packagePart = packageName.replace('.', '/');
			String classPattern = "classpath*:/" + packagePart + "/**/*.class";
			Resource[] resources = rl.getResources(classPattern);
			for (int i = 0; i < resources.length; i++) {
				Resource resource = resources[i];
				String fileName = resource.getURL().toString();
				String className = fileName.substring(
						fileName.indexOf(packagePart),
						fileName.length() - ".class".length())
						.replace('/', '.');
				Class<?> type = Class.forName(className);
				if (isConcreteClass(type))
					result.add(type);
			}
		} catch (IOException e) {
			fatal(e);
			return null;
		} catch (ClassNotFoundException e) {
			fatal(e);
			return null;
		}
		return result;
	}

	boolean isConcreteClass(Class<?> type) {
		return !type.isInterface() && !isAbstract(type);
	}

	boolean isAbstract(Class<?> type) {
		return (type.getModifiers() ^ Modifier.ABSTRACT) == 0;
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

	void createBeanDefinitions(List<Class> classes, int autowire,
			Map<String, BeanDefinition> overrides) {
		for (Class beanClass : classes)
			createBeanDefinition(autowire, overrides, beanClass);

	}

	void createBeanDefinition(int autowire,
			Map<String, BeanDefinition> overrides, Class beanClass) {
		{
			BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
					.rootBeanDefinition(beanClass);
			beanDefinitionBuilder.setAutowireMode(autowire);
			String beanName = beanNameGenerator.getBeanName(beanClass);
			AbstractBeanDefinition beanDefinition = beanDefinitionBuilder
					.getBeanDefinition();
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
