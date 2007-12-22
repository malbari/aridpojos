package net.chrisrichardson.arid;


public class InterfacePackageScanner extends AbstractPackageScanner {

	@Override
	protected boolean isMatch(Class<?> type) {
		return isInterface(type);
	}

}
