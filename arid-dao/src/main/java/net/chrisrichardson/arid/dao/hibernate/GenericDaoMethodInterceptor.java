/**
 * 
 */
package net.chrisrichardson.arid.dao.hibernate;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.orm.hibernate3.HibernateTemplate;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class GenericDaoMethodInterceptor implements MethodInterceptor {
	private ConcurrentMap<String, DaoMethodInvocation> invocationMap = new ConcurrentHashMap<String, DaoMethodInvocation>();

	private HibernateTemplate hibernateTemplate;

	private Class entityClass;

	public GenericDaoMethodInterceptor(HibernateTemplate hibernateTemplate,
			Class entityClass) {
		this.hibernateTemplate = hibernateTemplate;
		this.entityClass = entityClass;
	}

	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		if (!Modifier.isAbstract(method.getModifiers())) {
			return proxy.invokeSuper(obj, args);
		} else {
			DaoMethodInvocation invocation = getInvocation(obj, method
					.getName(), method.getParameterTypes(), method
					.getReturnType());
			return invocation.invoke(args);
		}
	}

	private DaoMethodInvocation getInvocation(Object target, String methodName,
			Class[] parameterTypes, Class returnType) {
		DaoMethodInvocation invocation = invocationMap.get(methodName);
		if (invocation != null)
			return invocation;

		return new VirtualMethodInvocation(hibernateTemplate, entityClass,
				methodName, returnType);
	}

}