package net.chrisrichardson.arid.domain;

import java.io.Serializable;

public interface GenericDao<T, U extends Serializable> {

	public void add(T object);
	public T findById(U pk);
	public T findReferenceById(U pk);
	public T findRequiredById(U pk);
	public T merge(T object);
	public Class getType();
}