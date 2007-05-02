package net.chrisrichardson.arid;

public class ImplementationSuffixStrippingNameGenerator implements
		AridBeanNameGenerator {

	public String getBeanName(Class beanClass) {
		String simpleName = beanClass.getSimpleName();
		simpleName = simpleName.substring(0,1).toLowerCase() + simpleName.substring(1);
		if (simpleName.endsWith("Implementation"))
			return simpleName.substring(0, simpleName.length() - "Implementation".length());
		else
			return simpleName;
	}

}
