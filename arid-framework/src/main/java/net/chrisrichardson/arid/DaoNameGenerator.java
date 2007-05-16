package net.chrisrichardson.arid;

public class DaoNameGenerator implements AridBeanNameGenerator {

	public String getBeanName(Class beanClass) {
		String simpleName = beanClass.getSimpleName();
		return simpleName.substring(0, 1).toLowerCase()
				+ simpleName.substring(1)
				+ "Dao";
	}
}
