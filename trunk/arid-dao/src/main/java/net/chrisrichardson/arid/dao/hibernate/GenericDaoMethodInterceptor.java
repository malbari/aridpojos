/**
 * 
 */
package net.chrisrichardson.arid.dao.hibernate;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

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
			DaoMethodInvocation invocation = getInvocation(method.getName());
			boolean resultRequired = method.getName().startsWith("findRequired");
			List result = invocation.invoke(args);
			if (Collection.class.isAssignableFrom(method.getReturnType())) {
				if (resultRequired && result.isEmpty())
					throw new IncorrectResultSizeDataAccessException(-1, 0);
				return result;
			} else {
				if (resultRequired)
					return DataAccessUtils.requiredUniqueResult(result);
				else
					return DataAccessUtils.uniqueResult(result);
			}
		}
	}

	private DaoMethodInvocation getInvocation(String methodName) {
		DaoMethodInvocation invocation = invocationMap.get(methodName);
		if (invocation != null)
			return invocation;

		invocation = makeInvocation(methodName);
		
		invocationMap.put(methodName, invocation);
		 
		return invocation;
	}

	private DaoMethodInvocation makeInvocation(String methodName) {
		if (isNamedQuery(methodName)) {
			return new NamedQueryInvocation(hibernateTemplate, entityClass,
					methodName);
		} else
			return new VirtualMethodInvocation(hibernateTemplate, entityClass,
					methodName);
	}

	private boolean isNamedQuery(final String methodName) {
		return (Boolean) hibernateTemplate.execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				try {
					session.getNamedQuery(entityClass.getName() + "."
							+ methodName);
					return true;
				} catch (HibernateException e) {
					return false;
				}
			}
		});
	}

}