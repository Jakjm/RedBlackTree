import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class TreeTest {
	RedBlackTree<Integer> set;
	@BeforeEach
	public void before() {
		set = new RedBlackTree<Integer>();
	}
	@Test
	public void addTest() {
		assertSame(set.add(2),true);
		assertSame(set.add(2),false);
	}
	@Test
	public void clearTest() {
		set.add(2);
		set.add(5);
		set.add(7);
		set.add(1);
		assertEquals(set.toString(),"[1, 2, 5, 7]");
		assertSame(set.size(),4);
		
		set.clear();
		assertEquals(set.toString(),"[]");
		assertSame(set.size(),0);
	}
	//@Test
	public void test1() {
		String nums = "";
		int newNum = 0;
		while(set.size() < 20) {
			while(set.contains(newNum))newNum = (int)(Math.random() * 20);
			nums += newNum + " ";
			set.add(newNum);
		}
		while(set.size() > 10) {
			newNum = (int)(Math.random() * 20);
			set.remove(newNum);
		}
		System.out.println("\n");
		try {
			set.printTree();
			System.out.println(set.toString());
		}
		catch(StackOverflowError e) {
			System.out.println("\nStack overflow!!!!" + nums);
		}
	}
	@RepeatedTest(10)
	public void test2() {
		assertSame(set.size(),0);
		assertEquals(set.toString(),"[]");
		assertSame(set.contains(0),false);
		assertSame(set.contains(1),false);
		assertSame(set.contains(2),false);
		
		set.add(1);
		assertSame(set.size(),1);
		assertEquals(set.toString(),"[1]");
		assertSame(set.contains(0),false);
		assertSame(set.contains(1),true);
		assertSame(set.contains(2),false);
		
		set.add(2);
		assertSame(set.size(),2);
		assertEquals(set.toString(),"[1, 2]");
		assertSame(set.contains(0),false);
		assertSame(set.contains(1),true);
		assertSame(set.contains(2),true);
		
		set.add(0);
		assertSame(set.size(),3);
		assertEquals(set.toString(),"[0, 1, 2]");
		assertSame(set.contains(0),true);
		assertSame(set.contains(1),true);
		assertSame(set.contains(2),true);
		
		set.add(10);
		assertSame(set.size(),4);
		assertEquals(set.toString(), "[0, 1, 2, 10]");
		assertSame(set.contains(0),true);
		assertSame(set.contains(1),true);
		assertSame(set.contains(2),true);
		assertSame(set.contains(10),true);
		
		set.remove(1);
		assertSame(set.size(),3);
		assertEquals(set.toString(),"[0, 2, 10]");
		assertSame(set.contains(1),false);
		assertSame(set.contains(0),true);
		assertSame(set.contains(2),true);
		assertSame(set.contains(10),true);
	}
	//@Test
	public void remove1() {
		System.out.println("Test One ~~~~\n");
		for(int i = 1;i < 8;i++) {
			set.add(i);
		}
		set.printTree();
		set.remove(3);
		set.printTree();
		set.remove(1);
		set.printTree();
		set.remove(2);
		set.printTree();
		
		//Breaking here. Need to handle root case for rotation cases. 
		set.remove(6);
		set.printTree();
		set.remove(4);
		set.printTree();
		set.remove(5);
		set.printTree();
		System.out.println();
	}
	//@Test
	public void remove2() {
		System.out.println("Test Two ~~~~\n");
		for(int i = 8;i > 0;i--) {
			set.add(i);
		}
		set.printTree();
		set.remove(3);
		set.printTree();
		set.remove(7);
		set.printTree();
		set.remove(8);
		set.printTree();
		set.remove(6);
		set.printTree();
		set.remove(4);
		set.printTree();
		set.remove(5);
		set.printTree();
		set.remove(2);
		set.printTree();
		set.remove(1);
		set.printTree();
	}
	@RepeatedTest(10)
	public void strainTest() {
		int testSize = 100000;
		int insertItem = 0;
		//Adding the number of items...
		while(set.size() < testSize) {
			while(set.contains(insertItem))insertItem = (int)(Math.random() * 2 * testSize);
			set.add(insertItem);
		}
		//Removing items until the tree size is 10. 
		while(set.size() > 10) {
			set.remove(set.rootItem());
		}
		set.printTree();
	}
}
