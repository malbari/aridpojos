package net.chrisrichardson.arid.logging;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import net.chrisrichardson.arid.logging.AbstractLoggingAspect;
import net.chrisrichardson.arid.logging.InvocationLogger;

import org.apache.commons.logging.Log;
import org.aspectj.lang.ProceedingJoinPoint;

public class AbstractLoggingAspectTests extends TestCase {

	private InvocationLogger invocationLogger;
	private MyLoggingAspect loggingAspect;
	private ProceedingJoinPoint jp;
	private MyService target;
	private String aResult;
	private Log logger;
	private String aResult2;
	private NullPointerException exception = new NullPointerException();

	class MyLoggingAspect extends AbstractLoggingAspect {
		@Override
		protected InvocationLogger makeInvocationLogger(ProceedingJoinPoint jp,
				Log logger) {
			return invocationLogger;
		}

		@Override
		protected synchronized Log getLogger(ProceedingJoinPoint jp) {
			assertNotNull(super.getLogger(jp));
			return logger;
		}

		@Override
		protected void loggableOperation() {
		}
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		invocationLogger = createMock(InvocationLogger.class);
		jp = createMock(ProceedingJoinPoint.class);
		logger = createMock(Log.class);

		loggingAspect = new MyLoggingAspect();
		target = createMock(MyService.class);
		aResult = "TheResult";
		aResult2 = "TheResult2";
	}

	private void verifyMocks() {
		verify(invocationLogger);
		verify(jp);
		verify(logger);
		verify(target);
	}

	private void replayMocks() {
		replay(jp);
		replay(invocationLogger);
		replay(logger);
		replay(target);
	}

	public void testLogit_firstTime() throws Throwable {

		expect(jp.getTarget()).andReturn(target);
		expect(jp.proceed()).andReturn(aResult);
		expect(logger.isDebugEnabled()).andReturn(true);
		invocationLogger.entering();
		invocationLogger.leaving();

		replayMocks();

		Object result = loggingAspect.logIt(jp);

		assertEquals(aResult, result);
		verifyMocks();
	}

	public void testLogit_twice() throws Throwable {

		expect(jp.getTarget()).andReturn(target);
		expect(jp.proceed()).andReturn(aResult).andReturn(aResult2);
		expect(logger.isDebugEnabled()).andReturn(true).andReturn(true);
		invocationLogger.entering();
		expectLastCall().times(2);
		invocationLogger.leaving();
		expectLastCall().times(2);

		replayMocks();

		assertEquals(aResult, loggingAspect.logIt(jp));
		assertEquals(aResult2, loggingAspect.logIt(jp));
		verifyMocks();
	}

	public void testLogit_targetThrowsException() throws Throwable {

		expect(jp.getTarget()).andReturn(target);
		expect(jp.proceed()).andStubThrow(exception);

		expect(logger.isDebugEnabled()).andReturn(true);
		invocationLogger.entering();
		invocationLogger.exceptionThrown(exception);

		replayMocks();

		try {
			loggingAspect.logIt(jp);
			fail("expected exception");
		} catch (Exception e) {
			assertEquals(exception, e);
		}

		verifyMocks();
	}

	public void testLogit_noDebugging() throws Throwable {

		expect(jp.getTarget()).andReturn(target);
		expect(jp.proceed()).andReturn(aResult);
		expect(logger.isDebugEnabled()).andReturn(false);

		replayMocks();

		assertEquals(aResult, loggingAspect.logIt(jp));
		verifyMocks();
	}

	public void testLogit_noDebugging_targetThrowsException() throws Throwable {

		expect(jp.getTarget()).andReturn(target);
		expect(jp.proceed()).andStubThrow(exception);

		expect(logger.isDebugEnabled()).andReturn(false);
		invocationLogger.exceptionThrown(exception);

		replayMocks();

		try {
			loggingAspect.logIt(jp);
			fail("expected exception");
		} catch (Exception e) {
			assertEquals(exception, e);
		}

		verifyMocks();
	}
}
