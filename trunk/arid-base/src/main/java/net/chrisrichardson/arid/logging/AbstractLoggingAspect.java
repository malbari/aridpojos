package net.chrisrichardson.arid.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

public abstract class AbstractLoggingAspect {

	private Log logger;

	protected abstract void loggableOperation();

	public AbstractLoggingAspect() {
		super();
	}

	@Around("loggableOperation()")
	public Object logIt(ProceedingJoinPoint jp) throws Throwable {
		Log logger = getLogger(jp);
		if (logger.isDebugEnabled()) {
			InvocationLogger invocationLogger = makeInvocationLogger(jp, logger);
			invocationLogger.entering();
			try {
				Object result = jp.proceed();
				invocationLogger.leaving();
				return result;
			} catch (Throwable e) {
				invocationLogger.exceptionThrown(e);
				throw e;
			}
		} else {
			try {
				return jp.proceed();
			} catch (Throwable e) {
				InvocationLogger invocationLogger = makeInvocationLogger(jp, logger);
				invocationLogger.exceptionThrown(e);
				throw e;
			}
		}
	}

	protected InvocationLogger makeInvocationLogger(ProceedingJoinPoint jp, Log logger) {
		return new InvocationLogger(logger, jp);
	}

	protected synchronized Log getLogger(ProceedingJoinPoint jp) {
		if (logger == null) {
			Class<? extends Object> targetClass = jp.getTarget().getClass();
			logger = LogFactory.getLog(targetClass);
		}
		return logger;
	}

}