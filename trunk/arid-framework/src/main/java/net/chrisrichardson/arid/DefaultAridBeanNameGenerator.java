package net.chrisrichardson.arid;

/**
 * Lower cases the first letter and strips off the Impl prefix (if there is one)
 * 
 * @author cer
 *
 */

public class DefaultAridBeanNameGenerator implements AridBeanNameGenerator {
	public String getBeanName(Class beanClass) {
		String simpleName = beanClass.getSimpleName();
		simpleName = simpleName.substring(0,1).toLowerCase() + simpleName.substring(1);
		if (simpleName.endsWith("Impl"))
			return simpleName.substring(0, simpleName.length() - 4);
		else
			return simpleName;
	}
}
