package movida.bruno.dataStructures;

public interface DataStructure <T> {
	
	public int getElemCount();
	
	public boolean addElement(T element);
	
	public boolean deleteElement(T element);
	
	public void removeAllElements();
	
	public T find(T element);

	public T[] getArray(T[] dummy);
}
