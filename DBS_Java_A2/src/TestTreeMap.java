import java.util.ArrayList;
import java.util.TreeMap;

public class TestTreeMap {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TreeMap<Integer, String> tree_map = new TreeMap<Integer,String>();
		tree_map.put(100, "Geeks");
	    tree_map.put(5, "4");
	    tree_map.put(20, "Geeks");
	    tree_map.put(25, "Welcomes");
	    tree_map.put(3, "You");
	    tree_map.put(3, "You1");
	    
	    ArrayList<Integer> list = new ArrayList<Integer>();
	    list.add(1);
	    list.add(3);
	    System.out.println(list.size());
	    
	    System.out.println("TreeMap: " + tree_map);
	    System.out.println(tree_map.firstEntry().getValue());
	}

}
