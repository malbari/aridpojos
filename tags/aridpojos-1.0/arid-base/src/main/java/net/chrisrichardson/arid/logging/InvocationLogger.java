package net.chrisrichardson.arid.logging;

import org.apache.commons.logging.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;

public class InvocationLogger {

	private final Log log;
	private final ProceedingJoinPoint jp;
	private String methodName;
	
	public InvocationLogger(Log log, ProceedingJoinPoint jp) {
		this.log = log;
		this.jp = jp;
	}

	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	public void entering() {
		this.methodName = makeMethodName();
		log.debug("Entering: " + methodName);
		
	}

	public void leaving() {
		log.debug("Leaving: " + methodName);
	}

	public void exceptionThrown(Throwable e) {
		if (methodName == null) {
			this.methodName = makeMethodName();
		}
		log.error("Exception thrown: " + methodName, e);
	}
	
	private String makeMethodName() {
		Object target = jp.getTarget();
		Signature signature = jp.getSignature();
		return target.getClass().getName() + "." + signature.getName() + "()";
	}



}
