import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import bptree.BpTree;
import bptree.IndexNode;
import bptree.LeafNode;
import bptree.Node;
import bptree.Record;

public class dbqueryA2 {
	
	private final static int END_OF_PAGE_BYTE_SIZE = 5;
	private final static int ENTRY_SIZE = 6;
	private final static float TO_SECOND = 100F;

	public static void main(String[] args) {
		if (args.length == 2) {
			
			System.out.println("Argument one = " + args[0]);
	        System.out.println("Argument two = " + args[1]);
	        
	        int totalMatch = 0;
	        Record record;
			ArrayList<Record> recordList;
			TreeMap<Integer, ArrayList<Record>> treeMap = new TreeMap<Integer, ArrayList<Record>>();
			
			System.out.println("Reading the records from heapfile." + args[1] + " ... (1/3)");
			long startTimeRead = System.currentTimeMillis();
			
			try {
				
				FileInputStream fileIs = new FileInputStream("/home/ec2-user/heap." + args[1]);
				
//				For local uses
//				FileInputStream fileIs = new FileInputStream("heap." + args[1]);
				
				DataInputStream is = new DataInputStream(fileIs);
				
				int remainingBytes = is.available();
				int id;
				String dateTime = "";
				int year;
				String month;
				int mdate;
				String day;
				int time;
				int sensorID;
				String sensorName;
				int hourlyCounts;
				
				
				boolean isChecking = false;
													
				while(remainingBytes > END_OF_PAGE_BYTE_SIZE) {
					
					//Add a lock to check if the next information is dateTime or end of page
					if(!isChecking) {
						dateTime = is.readUTF();
					}
					
					id = is.readInt();
					year = is.readInt();
					month = is.readUTF();
					mdate = is.readInt();
					day = is.readUTF();
					time = is.readInt();
					sensorID = is.readInt();
					sensorName = is.readUTF();
					hourlyCounts = is.readInt();
					
///////				Put all records into a TreeMap structure to sort all the records by Sensor_ID (1/3)	
					
					record = new Record(id, dateTime, year, month, mdate, day, time, sensorID, sensorName, hourlyCounts);
					
					if (treeMap.containsKey(record.getSensorID())) {						
						for (Map.Entry<Integer, ArrayList<Record>> e : treeMap.entrySet()) {
							if (e.getKey() == record.getSensorID()) {
								recordList = e.getValue();
								recordList.add(record);
				            	treeMap.put(e.getKey(), recordList);
				            }
						}
					} else {
						recordList = new ArrayList<Record>();
						recordList.add(record);
						treeMap.put(record.getSensorID(), recordList);
					}				
					
///////				
					
					remainingBytes = is.available();
					
					if (remainingBytes > END_OF_PAGE_BYTE_SIZE) {
						
						String checkIfEndOfPage = is.readUTF();
						isChecking = true;
						
						if(checkIfEndOfPage.indexOf('$') != -1) {
							remainingBytes += checkIfEndOfPage.length();
							isChecking = false;
						} else {
							dateTime = checkIfEndOfPage;
						}
					}
									
				}		
				
				is.close();
			
			} catch (FileNotFoundException e1) {
				System.err.println("Cannot find file: heap." + args[1]);
			} catch (EOFException e1) {
				e1.printStackTrace();
				System.err.println(e1);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			long endTimeRead = System.currentTimeMillis();
			float durationRead = (endTimeRead - startTimeRead) / TO_SECOND;
			
///////		Construct B Plus tree (2/3)
			
			System.out.println("Building up B plus tree using Sensor_ID ... (2/3)");
			long startTimeBpTree = System.currentTimeMillis();
			
			BpTree tree = new BpTree();
			LeafNode currentLeaf = new LeafNode();
			
			//For further iteration
			ArrayList<LeafNode> leafNodeList = new ArrayList<LeafNode>();
			
			for (Map.Entry<Integer, ArrayList<Record>> e : treeMap.entrySet()) {
				currentLeaf.getEntryList().add(e.getKey());
				
				//ArrayList<ArrayList<Record>> 2D array list
				currentLeaf.getRecords().add(e.getValue());
				
				if (currentLeaf.getEntryList().size() == ENTRY_SIZE) {
					LeafNode newNode = new LeafNode();
					
					currentLeaf.setNodeRight(newNode);
					newNode.setNodeLeft(currentLeaf);
		
					leafNodeList.add(currentLeaf);
					currentLeaf = newNode;
					currentLeaf.getEntryList().add(e.getKey());
					currentLeaf.getRecords().add(e.getValue());
				}
			}
			
			if (!currentLeaf.getEntryList().isEmpty()) {
				//Last leaf is not full, add the last leaf
				leafNodeList.add(currentLeaf); 
			} else {
				currentLeaf.getNodeLeft().setNodeRight(null);
			}
			
			IndexNode currentIndex = new IndexNode();
			Node currentParentIndex;
			
			tree.setRoot(currentIndex);
			
			//start with the 2nd leaf node
			for(int i = 1; i < leafNodeList.size(); i++) {
				LeafNode leafToBeAddedAsEntry = leafNodeList.get(i);
				
				//set parent for the node
				LeafNode leafToBeAddedAsPointer = leafNodeList.get(i-1);
				leafToBeAddedAsPointer.setParent(currentIndex);
				
				//add to the parent (index node) the 1st entry of the leaf
				currentIndex.getEntryList().add(leafToBeAddedAsEntry.getEntryList().get(0));
				
				//add previous leaf to pointerList
				currentIndex.getPointerList().add(leafToBeAddedAsPointer);
				
				if (currentIndex.getEntryList().size() == ENTRY_SIZE) {
					//Check if there is a parent
					if (currentIndex.getParent() != null) {
						//Check if parent is full
						if (currentIndex.getParent().getEntryList().size() == ENTRY_SIZE) {
							IndexNode parentNode = new IndexNode();
							IndexNode secondNode = new IndexNode();
							
							tree.setRoot(parentNode);
							currentParentIndex = currentIndex.getParent();
							
							parentNode.getEntryList().add(currentParentIndex.getEntryList().get(ENTRY_SIZE - 1));
							
							currentParentIndex.setParent(parentNode);
							parentNode.getPointerList().add(currentParentIndex);
							
							parentNode.getPointerList().add(secondNode);
							secondNode.setParent(parentNode);
							
							currentParentIndex.getEntryList().remove(ENTRY_SIZE - 1);
							currentParentIndex = secondNode;
							
						} else {
							//add to parent entry list
							Node parent = currentIndex.getParent();
							IndexNode secondNode = new IndexNode();
							parent.getEntryList().add(currentIndex.getEntryList().get(ENTRY_SIZE - 1));
							
							((IndexNode) parent).getPointerList().add(secondNode);
							secondNode.setParent(parent);
							
							currentIndex.getEntryList().remove(ENTRY_SIZE - 1);
							currentIndex = secondNode;
						}
					} else {
						//If parent is null, create new parent
						IndexNode parentNode = new IndexNode();
						IndexNode secondNode = new IndexNode();
						
						tree.setRoot(parentNode);
						
						parentNode.getEntryList().add(currentIndex.getEntryList().get(ENTRY_SIZE - 1));
		
						currentIndex.setParent(parentNode);
						parentNode.getPointerList().add(currentIndex);
						
						parentNode.getPointerList().add(secondNode);
						secondNode.setParent(parentNode);
						
						currentIndex.getEntryList().remove(ENTRY_SIZE - 1);
						currentIndex = secondNode;
					}		
				}
			}
			
			long endTimeBpTree = System.currentTimeMillis();
			float durationBpTree = (endTimeBpTree - startTimeBpTree) / TO_SECOND;
				
///////		Query the result from input (3/3)
			
			System.out.println("Searching for: " + args[0] + " ... (3/3)");
			long startTime = System.currentTimeMillis();
			
			int selectedSensorId = Integer.parseInt(args[0].substring(0,2));
			String selectedDateTime = args[0].substring(2);
			
			Node selectedLeaf = tree.searchForLeafNode(selectedSensorId,tree.getRoot());
			ArrayList<Record> selectedRecordList = null;
			for (int i = 0; i < selectedLeaf.getEntryList().size(); i++) {
				if (selectedLeaf.getEntryList().get(i) == selectedSensorId) {
					selectedRecordList = ((LeafNode) selectedLeaf).getRecords().get(i);
					break;
				}
			}
			
			if (selectedRecordList == null) {
				System.out.println("Not included");
			} else {
				for (Record i : selectedRecordList) {
					//check condition, update totalMatch
					if(i.getDateTime().contains(selectedDateTime)) {
						totalMatch++;
						System.out.println("UUID: " + i.getId() + " | " + "DateTime: " + i.getDateTime() + " | " + "Year: " + i.getYear() + " | " + "Month: " + i.getMonth() + " | " + "MDate: " + i.getMdate() + " | " + "Day: " + i.getDay() + " | " + "Time: " + i.getTime() + " | " + "Sensor_ID: " + i.getSensorID() + " | " + "Sensor_Name: " + i.getSensorName() + " | " + "Hourly Counts: " + i.getHourlyCounts());
					}
				}
			}
			
			long endTime = System.currentTimeMillis();
			float duration = (endTime - startTime) / TO_SECOND;
			
			System.out.println("Found total " + totalMatch + " record(s)");
			System.out.println("Time taken for reading records from heap file in seconds: " + durationRead);
			System.out.println("Time taken for creating B Plus Tree in seconds: " + durationBpTree);
			System.out.println("Time taken for query in seconds: " + duration);
			
		} else {
			System.err.println("Please provide the following command: java dbquery \"[SensorID_DateTime]\" [pagesize]");
			System.exit(0);
		}
	}

}
