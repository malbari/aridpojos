package net.chrisrichardson.arid;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;

import org.springframework.core.io.Resource;

public abstract class AbstractPackageScanner implements PackageScanner {

	public List<Class> getClasses(AridBeanCreator creator, String packageName) {
		TypeAccumulator result = new TypeAccumulator();
		try {
			String packagePart = packageName.replace('.', '/');
			String classPattern = "classpath*:/" + packagePart + "/**/*.class";
			Resource[] resources = creator.rl.getResources(classPattern);
			for (int i = 0; i < resources.length; i++) {
				Resource resource = resources[i];
				String fileName = resource.getURL().toString();
				String className = fileName.substring(
						fileName.indexOf(packagePart),
						fileName.length() - ".class".length())
						.replace('/', '.');
				Class<?> type = Class.forName(className);
				if (isMatch(type))
					result.add(type);
			}
		} catch (IOException e) {
			creator.fatal(e);
			return null;
		} catch (ClassNotFoundException e) {
			creator.fatal(e);
			return null;
		}
		return result.asList();
	}

	protected boolean isConcreteClass(Class<?> type) {
		return !isInterface(type) && !isAbstract(type);
	}

	protected boolean isInterface(Class<?> type) {
		return Modifier.isInterface(type.getModifiers());
	}

	protected boolean isAbstract(Class<?> type) {
		return Modifier.isAbstract(type.getModifiers());
	}

	protected abstract boolean isMatch(Class<?> type);

}
