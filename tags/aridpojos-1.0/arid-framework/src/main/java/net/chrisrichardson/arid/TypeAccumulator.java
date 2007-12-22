package net.chrisrichardson.arid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TypeAccumulator {

	private List<Class> types = new ArrayList<Class>();
	
	protected void add(Class type) {
		for (Iterator<Class> it = types.iterator(); it.hasNext();) {
			Class existingClass = it.next();
			if (existingClass.isAssignableFrom(type)) {
				it.remove();
			} else if (type.isAssignableFrom(existingClass)) {
				return;
			}
		}
		types.add(type);
	}

	
	public List<Class> asList() {
		return types;
	}

}
