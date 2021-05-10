package bptree;

import java.util.ArrayList;

public abstract class Node {
	private Node parent;
	private ArrayList<Integer> entryList;
	
	public Node() {
		this.entryList = new ArrayList<Integer>();
	}
	
	public ArrayList<Integer> getEntryList() {
		return entryList;
	}
	
	public void setEntryList(ArrayList<Integer> entryList) {
		this.entryList = entryList;
	}

	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
}
