package movida.bruno.algorithms;
import movida.bruno.functions.Comparator;

public class SelectionSort {

	public static <T> void sort(T[] array, Comparator<T> c, boolean ascending) {
		for (int i = 0; i < array.length; i++) {
			int selectIndex = i;
			for (int j = i + 1; j < array.length; j++) {
				if (ascending) {
					if (c.compare(array[j], array[selectIndex]) < 0) {
						selectIndex = j;
					}
				}
				else {
					if (c.compare(array[j], array[selectIndex]) > 0) {
						selectIndex = j;
					}
				}
			}
			
			T temp = array[i];
			array[i] = array[selectIndex];
			array[selectIndex] = temp;
		}
	}
}
