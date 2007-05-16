package net.chrisrichardson.arid;

public class AbstractClassPackageScanner extends AbstractPackageScanner {

	@Override
	protected boolean isMatch(Class<?> type) {
		return !isInterface(type) && isAbstract(type);
	}

}
