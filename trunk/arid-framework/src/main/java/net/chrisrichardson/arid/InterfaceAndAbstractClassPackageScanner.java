package net.chrisrichardson.arid;

import java.util.Iterator;
import java.util.List;

/**
 * Scans for abstract classes and interfaces and returns the most derived types
 */
public class InterfaceAndAbstractClassPackageScanner extends
		AbstractPackageScanner {

	@Override
	protected boolean isMatch(Class<?> type) {
		return !isConcreteClass(type);
	}

	@Override
	protected List<Class> addNewType(List<Class> result, Class type) {
		if (removeSupertypes(result, type))
			return super.addNewType(result, type);
		else
			return result;
	}

	private boolean removeSupertypes(List<Class> result, Class type) {
		for (Iterator<Class> it = result.iterator(); it.hasNext();) {
			Class<?> existingClass = it.next();
			if (existingClass.isAssignableFrom(type)) {
				it.remove();
			} else if (type.isAssignableFrom(existingClass)) {
				return false;
			}
		}
		return true;
	}

}
