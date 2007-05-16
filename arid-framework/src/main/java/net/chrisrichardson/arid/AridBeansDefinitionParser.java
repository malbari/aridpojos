package net.chrisrichardson.arid;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.Element;

public class AridBeansDefinitionParser implements BeanDefinitionParser {

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String packageName = element.getAttribute("package");
		String pattern = element.getAttribute("pattern");
		int autowire = AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR;
		BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
		if (element.hasAttribute("autowire")) {
			autowire = delegate.getAutowireMode(element
					.getAttribute("autowire"));
		}
		XmlReaderContext readerContext = parserContext.getReaderContext();
		ResourcePatternResolver resourceLoader = (ResourcePatternResolver) readerContext
				.getReader().getResourceLoader();
		BeanDefinitionRegistry registry = readerContext.getRegistry();

		AridBeanCreator aridBeanCreator = new AridBeanCreator(resourceLoader,
				registry, delegate, readerContext);

		String beanNameGeneratorName = element.getAttribute("name-generator");
		if (beanNameGeneratorName != null
				&& !beanNameGeneratorName.trim().equals(""))
			aridBeanCreator.setBeanNameGenerator(makeBeanNameGenerator(
					beanNameGeneratorName, readerContext));

		String beanGeneratorName = element.getAttribute("bean-generator");
		BeanDefinitionGenerator beanDefinitionGenerator = makeBeanGenerator(
				beanGeneratorName, readerContext);
		beanDefinitionGenerator.setAutoWire(autowire);

		if (element.hasAttribute("parent-bean-name"))
			beanDefinitionGenerator.setParentBeanName(element
					.getAttribute("parent-bean-name"));

		aridBeanCreator.setBeanDefinitionGenerator(beanDefinitionGenerator);

		if (element.hasAttribute("package-scanner")) {
			aridBeanCreator.setPackageScanner(makePackageScanner(element.getAttribute("package-scanner"), readerContext));
		}
		aridBeanCreator.createBeans(element, packageName, pattern);
		return null;
	}

	private PackageScanner makePackageScanner(String packageScannerName,
			XmlReaderContext readerContext) {
		if (packageScannerName != null && !packageScannerName.trim().equals(""))
			try {
				return (PackageScanner) Class.forName(
						packageScannerName).newInstance();
			} catch (InstantiationException e) {
				fatal(e, readerContext);
				return null;
			} catch (IllegalAccessException e) {
				fatal(e, readerContext);
				return null;
			} catch (ClassNotFoundException e) {
				fatal(e, readerContext);
				return null;
			}
			else {
				return new ConcreteClassPackageScanner();
			}
	}

	private BeanDefinitionGenerator makeBeanGenerator(String beanGeneratorName,
			XmlReaderContext readerContext) {
		if (beanGeneratorName != null && !beanGeneratorName.trim().equals(""))
			try {
				return (BeanDefinitionGenerator) Class.forName(
						beanGeneratorName).newInstance();
			} catch (InstantiationException e) {
				fatal(e, readerContext);
				return null;
			} catch (IllegalAccessException e) {
				fatal(e, readerContext);
				return null;
			} catch (ClassNotFoundException e) {
				fatal(e, readerContext);
				return null;
			}
		else {
			return new DefaultBeanDefinitionGenerator();
		}
	}

	AridBeanNameGenerator makeBeanNameGenerator(String beanNameGeneratorName,
			XmlReaderContext readerContext) {
		try {
			return (AridBeanNameGenerator) Class.forName(beanNameGeneratorName)
					.newInstance();
		} catch (InstantiationException e) {
			fatal(e, readerContext);
			return null;
		} catch (IllegalAccessException e) {
			fatal(e, readerContext);
			return null;
		} catch (ClassNotFoundException e) {
			fatal(e, readerContext);
			return null;
		}
	}

	private void fatal(Throwable e, XmlReaderContext readerContext) {
		readerContext.fatal(e.getMessage(), null, e);
	}
}
