package com.lab_programming.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class HashList<E> implements List<E> {
	private HashMap<Integer, E> backing = new HashMap<Integer, E>();
	private int size;
	@Override
	public boolean add(E value) {
		// TODO Auto-generated method stub
		backing.put(size++, value);
		return true;
	}

	@Override
	public void add(int index, E value) {
		shift(index,1);
		backing.put(index, value);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for(E value: c) backing.put(size++, value);
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		shift(index,c.size());
		for(E value: c) backing.put(index++, value);
		return true;
	}

	@Override
	public void clear() {
		backing.clear();
	}

	@Override
	public boolean contains(Object o) {
		return backing.values().contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return backing.values().containsAll(c);
	}

	@Override
	public E get(int index) {
		return backing.get(index);
	}

	@Override
	public int indexOf(Object o) {
		for(int i = 0; i < size; i++){
			if(o==null ? get(i)==null : o.equals(get(i))) return i;
		}
		return -1;
	}

	@Override
	public boolean isEmpty() {
		return backing.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return backing.values().iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		for(int i = size-1; i >= 0; i++){
			if(o==null ? get(i)==null : o.equals(get(i))) return i;
		}
		return -1;
	}

	@Override
	public ListIterator<E> listIterator() {
		//TODO: implement me
		return null;
	}

	@Override
	public ListIterator<E> listIterator(int arg0) {
		//TODO: implement me
		return null;
	}

	@Override
	public boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E remove(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E set(int arg0, E arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<E> subList(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// OTHER METHODS HERE

	private void shift(int index, int i) {
		//TODO: Implement me
	}
	
}
