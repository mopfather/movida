package movida.bruno.algorithms;
import java.util.Arrays;

import movida.bruno.dataStructures.MinHeap;
import movida.bruno.functions.Comparator;

public class HeapSort {
	
	@SuppressWarnings("unchecked")
	public static <T> void sort(T[] array, Comparator<T> c, boolean ascending) {
		int len = 0;
		while (len < array.length && array[len] != null) {
			len++;
		}
		T[] arr = (T[]) Arrays.copyOf(array, len, array.getClass());
		MinHeap<T> heap = new MinHeap<T>(arr, c);
		
		if (ascending) {
			for (int i = 0; i < array.length; i++) {
				array[i] = heap.extract();
			}
		}
		else {
			for (int i = len - 1; i >= 0; i--) {
				array[i] = heap.extract();
			}
		}
	}
}
