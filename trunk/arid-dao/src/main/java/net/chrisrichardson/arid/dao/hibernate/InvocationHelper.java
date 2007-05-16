package net.chrisrichardson.arid.dao.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.orm.hibernate3.HibernateTemplate;

public class InvocationHelper {

	private final HibernateTemplate hibernateTemplate;

	private ConcurrentMap<String, DaoMethodInvocation> invocationMap = new ConcurrentHashMap<String, DaoMethodInvocation>();

	private Class entityClass;

	public InvocationHelper(HibernateTemplate hibernateTemplate,
			Class entityClass) {
		this.hibernateTemplate = hibernateTemplate;
		this.entityClass = entityClass;
	}

	private DaoMethodInvocation getInvocation(Object target, String methodName,
			Class[] parameterTypes, Class returnType) {
		DaoMethodInvocation invocation = invocationMap.get(methodName);
		if (invocation != null)
			return invocation;

		try {
			Method m = target.getClass().getMethod(methodName, parameterTypes);
			return new RealMethodInvocation(target, m);
		} catch (NoSuchMethodException e) {
			return new VirtualMethodInvocation(hibernateTemplate, entityClass,
					methodName, returnType);

		}
	}

	public Object invoke(Object target, String methodName,
			Class[] parameterTypes, Class returnType, Object[] args)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		DaoMethodInvocation invocation = getInvocation(target, methodName,
				parameterTypes, returnType);
		return invocation.invoke(args);
	}

}
