package movida.bruno.dataStructures;

public class Counter<T> {

	private T element;
	private int count;
	
	public Counter(T element) {
		this.element = element;
		count = 1;
	}
	
	public void increaseCount() {
		count++;
	}
	
	public void decreaseCount() {
		count--;
	}
	
	public int getCount() {
		return count;
	}
	
	public T getElement() {
		return element;
	}
}
