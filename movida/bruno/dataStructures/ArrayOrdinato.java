package movida.bruno.dataStructures;

import java.util.Arrays;

import movida.bruno.functions.Comparator;

public class ArrayOrdinato<T> implements DataStructure<T> {
	private int size;
	private int elemCount;
	private Object[] elements;
	private Comparator<T> c;
	
	public ArrayOrdinato(Comparator<T> c) {
		this.size = 1;
		this.elements = new Object[size];
		this.elemCount = 0;
		this.c = c;
	}
	
	@SuppressWarnings("unchecked")
	private T elementAt(int index) {
		return (T) elements[index];
	}
	
	public int getElemCount() {
		return this.elemCount;
	}
	
	public T find(T element) {
		int s = search(element);
		if (s == -1) {
			return null;
		}
		else {
			return elementAt(s);
		}
	}
	
	public int search(T element) {
		int l = 0;
		int h = elemCount - 1;
		while (l <= h) {
			int m = (l + h)/2;
			if (c.compare(element, elementAt(m)) < 0) {
				h = m - 1;
			}
			else if (c.compare(element, elementAt(m)) > 0) {
				l = m + 1;
			}
			else {
				return m;
			}
		}
		return -1;
	}
	
	public int findAddIndex(T element) {
		int l = 0;
		int h = elemCount - 1;
		while (l <= h) {
			if (c.compare(element, elementAt(l)) < 0) {
				return l;
			}
			
			int m = (l + h)/2;
			if (c.compare(element, elementAt(m)) < 0) {
				h = m - 1;
			}
			else {
				l = m + 1;
			}
		}
		return l;
	}
	
	public boolean addElement(T element) {
		int s = search(element);
		if (s != -1) {
			elements[s] = element;
			return false;
		}

		int i = findAddIndex(element);
		if (elemCount + 1 > size) {
			size = size*2;
			Object[] newElements = new Object[size];
			System.arraycopy(elements, 0, newElements, 0, i);
			newElements[i] = element;
			System.arraycopy(elements, i , newElements, i + 1, elemCount - i);
			elements = newElements;
		}
		else {
			System.arraycopy(elements, i, elements, i + 1, elemCount - i);
			elements[i] = element;
		}

		elemCount++;
		return true;
	}
	
	public boolean deleteElement(T element) {
		int i = search(element);
		if (i == -1) {
			return false;
		}
		
		if (elemCount - 1 < size/2) {
			size = size/2;
			Object[] newElements = new Object[size];
			System.arraycopy(elements, 0, newElements, 0, i);
			System.arraycopy(elements, i + 1, newElements, i, elemCount - i - 1);
			elements = newElements;
		}
		else {
			System.arraycopy(elements, i + 1, elements, i, elemCount - i - 1);
		}
		
		elemCount--;
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public T[] getArray(T[] dummy) {
		return (T[]) Arrays.copyOf(elements, elemCount, dummy.getClass());
	}
	
	public void removeAllElements() {
		elements = new Object[1];
		size = 1;
		elemCount = 0;
	}
}
