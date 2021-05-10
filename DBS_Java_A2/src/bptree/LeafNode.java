package bptree;

import java.util.ArrayList;

public class LeafNode extends Node {
	private LeafNode nodeLeft;
	private LeafNode nodeRight;
	private ArrayList<ArrayList<Record>> records;
	
	public LeafNode() {
		super();
		this.records = new ArrayList<ArrayList<Record>>();
	}
	
	public LeafNode getNodeLeft() {
		return nodeLeft;
	}


	public void setNodeLeft(LeafNode nodeLeft) {
		this.nodeLeft = nodeLeft;
	}


	public LeafNode getNodeRight() {
		return nodeRight;
	}


	public void setNodeRight(LeafNode nodeRight) {
		this.nodeRight = nodeRight;
	}


	public ArrayList<ArrayList<Record>> getRecords() {
		return records;
	}
	public void setRecords(ArrayList<ArrayList<Record>> records) {
		this.records = records;
	}
	
}
