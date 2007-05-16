package net.chrisrichardson.arid;

import java.util.List;

public interface PackageScanner {

	public List<Class> getClasses(AridBeanCreator creator, String packageName);

}