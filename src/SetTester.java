import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class SetTester {
	private static final int START_SIZE = 1000;
	private static final int MULT = 10;
	private static final int END_SIZE = 1000000;
	public static void main(String [] args) {
		RedBlackTree<Integer> a4Set = new RedBlackTree<Integer>();
		TreeSet<Integer> treeSet = new TreeSet<Integer>();
		HashSet<Integer> hashSet = new HashSet<Integer>();
		
		String a4String, treeString, hashString, outString;

		PrintWriter pWriter = null;
		try {
			pWriter = new PrintWriter("testrun.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedWriter writer = new BufferedWriter(pWriter);
		
		for(int size = START_SIZE;size <= END_SIZE;size *= MULT) {
			a4String = String.format("A4Set   %s",testInsertion(a4Set,size));
			a4Set.clear();
			
			treeString = String.format("TreeSet %s",testInsertion(treeSet,size));
			treeSet.clear();
			
			hashString = String.format("HashSet %s",testInsertion(hashSet, size));
			hashSet.clear();
			
			outString = a4String + "\n";
			outString += treeString + "\n";
			outString += hashString + "\n";
			System.out.println(outString);
			try {
				writer.write(outString + "\n");
				writer.flush();
			}
			catch(Exception e) {
				
			}
		}
	
	}
	private static String testInsertion(Set<Integer> set,int numItems) {
		long startTime, endTime; 
		int insertItem = 0;
		
		
		startTime = System.currentTimeMillis();
		while(set.size() < numItems) {
			while(set.contains(insertItem)) {
				insertItem = (int)(Math.random() * (numItems * 10));
			}
			set.add(insertItem);
		}
		endTime = System.currentTimeMillis();
		
		return String.format("%7d items %4d ms",numItems,endTime - startTime);
	}
}
