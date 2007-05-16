package net.chrisrichardson.arid;

public class PrefixStrippingBeanNameGenerator implements AridBeanNameGenerator {

	public String getBeanName(Class beanClass) {
		String simpleName = beanClass.getSimpleName();
		for (int i = 1 ; i < simpleName.length() ; i++) {
			char c = simpleName.charAt(i);
			if (Character.isUpperCase(c)) {
				return simpleName.substring(i,i+1).toLowerCase() + simpleName.substring(i+1);
			}
		}
		return simpleName;
	}

}
