package bptree;

import java.util.ArrayList;

public class BpTree {
	private IndexNode root;
	
	public IndexNode getRoot() {
		return root;
	}

	public void setRoot(IndexNode root) {
		this.root = root;
	}

	public Node searchForLeafNode(int index, Node node) {
		
		ArrayList<Integer> entryList = node.getEntryList();
		ArrayList<Node> pointerList = ((IndexNode) node).getPointerList();
		Node currentNode = null;
		
		for (int i = 0; i < entryList.size(); i++) {
			if (index < entryList.get(i)) {
				currentNode = pointerList.get(i);
				break;
			}
		}
		
		if (currentNode == null) {
			currentNode = pointerList.get(pointerList.size()-1);
		}
		
		if (currentNode instanceof LeafNode) {
			return currentNode;
		}
		
		return searchForLeafNode(index, currentNode);
	}
}
