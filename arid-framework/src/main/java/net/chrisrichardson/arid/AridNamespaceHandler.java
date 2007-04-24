package net.chrisrichardson.arid;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class AridNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser("define-beans",
				new AridBeansDefinitionParser());

	}

}
