package bptree;

import java.util.ArrayList;

public class IndexNode extends Node {
	private Node child;
	private ArrayList<Node> pointerList;
	
	public IndexNode() {
		super();
		this.pointerList = new ArrayList<Node>();
	}

	public Node getChild() {
		return child;
	}
	
	public void setChild(Node child) {
		this.child = child;
	}

	public ArrayList<Node> getPointerList() {
		return pointerList;
	}

	public void setPointerList(ArrayList<Node> pointerList) {
		this.pointerList = pointerList;
	}
	
}
