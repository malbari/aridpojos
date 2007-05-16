package net.chrisrichardson.arid;


public abstract class AbstractBeanDefinitionGenerator implements BeanDefinitionGenerator {

	protected int autowire;
	protected String parentBeanName;

	public void setAutoWire(int autowire) {
		this.autowire = autowire;

	}

	public void setParentBeanName(String parentBeanName) {
		this.parentBeanName = parentBeanName;
	}

}
