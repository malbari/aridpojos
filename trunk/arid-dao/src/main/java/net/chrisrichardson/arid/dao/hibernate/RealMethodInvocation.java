package net.chrisrichardson.arid.dao.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RealMethodInvocation implements DaoMethodInvocation {

	private final Object dao;
	private final Method m;

	public RealMethodInvocation(Object dao, Method m) {
		this.dao = dao;
		this.m = m;
	}

	public Object invoke(Object[] args) {
		try {
			return m.invoke(dao, args);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

}
