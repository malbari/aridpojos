package net.chrisrichardson.arid.logging;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.easymock.EasyMock.expect;

import net.chrisrichardson.arid.logging.InvocationLogger;

import org.apache.commons.logging.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;

import junit.framework.TestCase;

public class InvocationLoggerTests extends TestCase {

	private ProceedingJoinPoint jp;
	private Log log;
	private MyService target;
	private Signature signature;
	private String methodName = "foo";
	private Exception e = new NullPointerException();
	private String expectedMethodName = MyServiceImpl.class.getName()
			+ ".foo()";
	private InvocationLogger invocationLogger;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		jp = createMock(ProceedingJoinPoint.class);
		log = createMock(Log.class);
		target = new MyServiceImpl();
		signature = createMock(Signature.class);
		invocationLogger = new InvocationLogger(log, jp);

		expect(jp.getTarget()).andReturn(target);
		expect(jp.getSignature()).andReturn(signature);
		expect(signature.getName()).andReturn(methodName);
	}

	private void verifyMocks() {
		verify(jp);
		verify(log);
		verify(signature);
	}

	private void replayMocks() {
		replay(jp);
		replay(log);
		replay(signature);
	}

	public void test_enteringAndLeaving() {
		log.debug("Entering: " + expectedMethodName);
		log.debug("Leaving: " + expectedMethodName);

		replayMocks();

		invocationLogger.entering();
		invocationLogger.leaving();

		verifyMocks();
	}

	public void test_enteringAndException() {
		log.debug("Entering: " + expectedMethodName);
		log.error("Exception thrown: " + expectedMethodName, e);

		replayMocks();

		invocationLogger.entering();
		invocationLogger.exceptionThrown(e);

		verifyMocks();
	}

	public void test_exception() {
		log.error("Exception thrown: " + expectedMethodName, e);

		replayMocks();

		invocationLogger.exceptionThrown(e);

		verifyMocks();
	}

}
