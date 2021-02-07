package movida.bruno.functions;

public interface Condition<T1, T2> {
	
	public boolean match(T1 o1, T2 o2);
}
