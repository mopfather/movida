package movida.bruno.dataStructures;

import movida.bruno.functions.Comparator;

public class MinHeap<T> {
	private T[] elements; 
	private Comparator<T> c;
	private int size;
	
	public MinHeap(T[] array, Comparator<T> comp) {
		this.elements = array;
		this.c = comp;
		this.size = array.length;
		heapify();
	}
	
	public void heapify() {
		for (int i = (size - 2)/2; i >= 0; i--) {
			fixHeap(i);
		}
	}

	public void fixHeap(int i) {
		while (2*i + 1 < size) {
			int min = 2*i + 1;
			if (2*i + 2 < size && c.compare(elements[2*i + 2], elements[min]) < 0) {
				min = 2*i + 2;
			}
			
			if (c.compare(elements[i], elements[min]) < 0) {
				return;
			}
			else {
				T temp = elements[i];
				elements[i] = elements[min];
				elements[min] = temp;
				i = min;
			}
		}
	}
	
	public T extract() {
		T min = elements[0];
		elements[0] = elements[size - 1];
		size--;
		fixHeap(0);
		return min;
	}
}

