package movida.bruno.dataStructures;

import java.util.Arrays;

import movida.bruno.functions.Comparator;

public class ABR<T> implements DataStructure<T>{
	private class Node{
		private T element;
		private Node parent;
		private Node left;
		private Node right;
		
		public Node(T element, Node parent) {
			this.element = element;
			this.parent = parent;
			this.left = null;
			this.right = null;
		}
	}

	private Node root;
	private Comparator<T> c;
	private int elemCount;
	
	public ABR(Comparator<T> c) {
		this.root = null;
		this.c = c;
		this.elemCount = 0;
	}
	
	public int getElemCount() {
		return this.elemCount;
	}
	
	public T find(T element) {
		Node s = search(element);
		if (s == null) {
			return null;
		}
		else {
			return s.element;
		}
	}
	
	public Node search(T element) {
		Node iter = root;
		while (iter != null) {
			if (c.compare(element, iter.element) == 0) {
				return iter;
			}
			
			if (c.compare(element, iter.element) < 0) {
				iter = iter.left;
			}
			else {
				iter = iter.right;
			}
		}
		return null;
	}
	
	public Node predecessor(Node node) {
		if (node.left == null) {
			return null;
		}
		
		Node iter = node.left;
		while (iter.right != null) {
			iter = iter.right;
		}
		return iter;
	}
	
	public boolean addElement(T element) {
		Node s = search(element);
		if (s != null) {
			s.element = element;
			return false;
		}
		elemCount++;
		
		if (root == null) {
			root = new Node(element, null);
			return true;
		}
		
		Node iter = root;
		while (true) {
			if (c.compare(element, iter.element) < 0) {
				if (iter.left != null) {
					iter = iter.left;
				}
				else {
					iter.left = new Node(element, iter);
					return true;
				}
			}
			else {
				if (iter.right != null) {
					iter = iter.right;
				}
				else {
					iter.right = new Node(element, iter);
					return true;
				}
			}
		}
	}
	
	public boolean deleteElement(T element) {
		Node s = search(element);
		if (s == null) {
			return false;
		}
		
		if (c.compare(element, root.element) == 0) {
			if (root.left == null && root.right == null) {
				root = null;
				elemCount--;
			}
			else if (root.left == null && root.right != null) {
				root.right.parent = null;
				root = root.right;
				elemCount--;
			}
			else if (root.left != null && root.right == null) {
				root.left.parent = null;
				root = root.left;
				elemCount--;
			}
			else {
				Node pred = predecessor(root);
				deleteElement(pred.element);
				root.element = pred.element;
			}
			
			return true;
		}
		
		boolean left = (c.compare(s.element, s.parent.element) < 0);
		if (s.left == null && s.right == null) {
			if (left) {
				s.parent.left = null;
			}
			else {
				s.parent.right = null;
			}
			elemCount--;
		}
		else if (s.left != null && s.right == null) {
			if (left) {
				s.left.parent = s.parent;
				s.parent.left = s.left;
			}
			else {
				s.left.parent = s.parent;
				s.parent.right = s.left;
			}
			elemCount--;
		}
		else if (s.left == null && s.right != null) {
			if (left) {
				s.right.parent = s.parent;
				s.parent.left = s.right;
			}
			else {
				s.right.parent = s.parent;
				s.parent.right = s.right;
			}
			elemCount--;
		}
		else {
			Node pred = predecessor(s);
			deleteElement(pred.element);
			s.element = pred.element;
		}
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public T[] getArray(T[] dummy) {
		T[] array = (T[]) Arrays.copyOf(new Object[elemCount], elemCount, dummy.getClass());
		getArrayHelper(root, array, 0);
		return array;
	}
	
	public int getArrayHelper(Node node, T[] array, int index) {
		if (node == null) {
			return index;
		}
		index = getArrayHelper(node.left, array, index);
		array[index] = node.element;
		index++;
		index = getArrayHelper(node.right, array, index);
		return index;
		
	}
	
	public void removeAllElements() {
		root = null;
		elemCount = 0;
	}
}