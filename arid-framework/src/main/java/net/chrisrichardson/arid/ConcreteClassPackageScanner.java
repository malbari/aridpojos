package net.chrisrichardson.arid;


public class ConcreteClassPackageScanner extends AbstractPackageScanner implements
		PackageScanner {

	@Override
	protected boolean isMatch(Class<?> type) {
		return isConcreteClass(type);
	}

}
