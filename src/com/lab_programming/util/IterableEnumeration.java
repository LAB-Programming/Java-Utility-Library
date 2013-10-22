package com.lab_programming.util;

import java.util.Enumeration;
import java.util.Iterator;

public class IterableEnumeration<T> implements Iterator<T>, Iterable<T> {
	private Enumeration<T> e;
	
	public IterableEnumeration(Enumeration<T> e){
		this.e = e;
	}
	
	@Override
	public boolean hasNext() {
		return e.hasMoreElements();
	}

	@Override
	public T next() {
		return e.nextElement();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}

}
