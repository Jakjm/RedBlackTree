import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
/**
 * A4Set - a RB tree implementation for assignment 4.
 * @author jordan
 * @param <Item> - the class of items stored in the tree.
 * @version Wednesday March 27th 2019
 */
public class RedBlackTree<Item extends Comparable<Item>> implements Set<Item>{
	/**Red color constant**/
	private static byte RED = 1;
	/**Black color constant**/
	private static byte BLACK = 0;
	
	/**Right direction constant**/
	private static byte RIGHT = 3;
	
	/**Left direction constant**/
	private static byte LEFT = 4;
	
	
	/**The root of the RB tree */
	private RBNode<Item> root;
	
	/** The current size of the RB tree. */
	private int size;
	
	/**
	 * Creates a new set initialized with no items. 
	 */
	public RedBlackTree() {
		root = null;
		size = 0;
	}
	/**
	 * Returns the item contained at the root. 
	 * Used for testing. 
	 * @return the root's item - or null, if the root does not exist. 
	 */
	Item rootItem() {
		if(root != null) {
			return root.item;
		}
		else {
			return null;
		}
	}
	/**
     * Used for testing the structure of the RB tree
     * Recursively prints the tree in a preorder traversal pattern. 
     */
   void printTree(){
	   	System.out.println("Printing tree contents");
    	if(root == null){
    		return;
    	}
    	else{
         printTree(root);
    	}
    }
    /**
     * Used for testing the RB tree structure.
     * Does a preorder traversal of the tree. 
     */
	private void printTree(RBNode <Item> node){
		System.out.println(node.toString());
    	if(node.left != null)printTree(node.left);
    	if(node.right != null)printTree(node.right);
    }
	
	/**
	 * Adds the requested item to the set.
	 * @param item - the item to be added to the set.
	 * @return true - if the item was added. 
	 * @throws RuntimeException, if the item is already contained within the tree. 
	 */
	@Override
	public boolean add(Item item) {
		if(root == null) {
			++size;
			root = new RBNode<Item>(item);
			return true;
		}
		else {
			RBNode<Item> currentNode = root;
			int compareValue;
			QuickStack<Item> stack = new QuickStack<Item>();
			
			/*
			 * Navigating to the correct position.
			 */
			while(currentNode != null) {
				compareValue = item.compareTo(currentNode.item);
				
				//If the new item greater than current node...
				if(compareValue > 0) {
					stack.push(currentNode,RIGHT);
					currentNode = currentNode.right;
				}
				//If new item less than current node...
				else if(compareValue < 0) {
					stack.push(currentNode,LEFT);
					currentNode = currentNode.left;
				}
				else {
					//If the item already exists in the list we return false.
					return false; 
				}
			}
			
			/*
			 *Creating the new node and inserting it. 
			 */
			currentNode = new RBNode<Item>(item);
			
			//Getting the parent and the direction off the stack.
			byte parentDirection = stack.getDirection();
			RBNode<Item> parent = stack.pop();
			
			//Putting the current node into the correct spot.
			if(parentDirection == RIGHT) {
				parent.right = currentNode;
			}
			else {
				parent.left = currentNode;
			}
			
			
			/*
			 *~~~ Now performing RB tree maintenance ~~~
			 */
			currentNode.color = RED;

			//We have to restructure if the parent is not black. 
			while(parent.color == RED) {
				byte grandparentDirection = stack.getDirection();
				RBNode<Item> grandparent = stack.pop();
				
				//Getting the uncle.
				if(grandparentDirection == RIGHT) {
					RBNode<Item> uncle = grandparent.left;
					
					//If the uncle is black, we restructure. 
					if((uncle == null || uncle.color == BLACK)) {
						//Right-left rotation
						if(parentDirection == LEFT) {
							
							//color the nodes.
							grandparent.color = RED;
							currentNode.color = BLACK;
							parent.color = RED;
							
							//Passing on the children of the current node so it can become the new grandparent.
							parent.left = currentNode.right;
							grandparent.right = currentNode.left;
							
							//Making the current node the new grandparent.
							currentNode.left = grandparent;
							currentNode.right = parent;
							
							//Resetting root, if necessary.
							if(root == grandparent) {
								root = currentNode;
							}
							//Otherwise, resetting the top to the child of the great-grandparent.
							else {
								byte direction = stack.getDirection();
								RBNode<Item> ancestor = stack.pop();
								if(direction == RIGHT)ancestor.right = currentNode;
								else ancestor.left = currentNode;
							}
						}
						//Right-right rotation
						else {
							//color the nodes.
							grandparent.color = RED;
							parent.color = BLACK;
							
							//Rotating parent to the top above child and grandparent.
							grandparent.right = parent.left;
							
							//Making the parent the new grandparent.
							parent.left = grandparent;
							
							//Resetting root, if necessary.
							if(root == grandparent) {
								root = parent;
							}
							//Otherwise, resetting the top to the child of the great-grandparent.
							else {
								byte direction = stack.getDirection();
								RBNode<Item> ancestor = stack.pop();
								//Ancestor sets the parent in the place of the old child.
								if(direction == RIGHT) {
									ancestor.right = parent;
								}
								else {
									ancestor.left = parent;
								}
							}
						}
						break;
					}
					
					//If the uncle is red, we perform recoloring.
					else {
						//Recolor the nodes.
						parent.color = BLACK;
						uncle.color = BLACK;
						
						//If the grandparent is the root or stack size is zero...
						if(grandparent == root || stack.size == 0) {
							break;
						}
						grandparent.color = RED;
						
						//Reset for moving upwards.
						currentNode = grandparent;
						parentDirection = stack.getDirection();
						parent = stack.pop();
						continue;
					}
				}
				else {
					RBNode<Item> uncle = grandparent.right;
					if(uncle == null || uncle.color == BLACK) {
						//Left-right rotation
						if(parentDirection == RIGHT) {
							
							//Color nodes.
							grandparent.color = RED;
							currentNode.color = BLACK;
							parent.color = RED;
							
							//Passing on the children of the current node so it can become the new grandparent.
							parent.right = currentNode.left;
							grandparent.left = currentNode.right;
							
							//Making the current node the new grandparent.
							currentNode.right = grandparent;
							currentNode.left = parent;
							
							//Resetting root, if necessary.
							if(root == grandparent) {
								root = currentNode;
							}
							//Otherwise, resetting the top to the child of the great-grandparent.
							else {
								byte direction = stack.getDirection();
								RBNode<Item> ancestor = stack.pop();
								//Ancestor sets the current node in the place of the old child
								if(direction == RIGHT) {
									ancestor.right = currentNode;
								}
								else {
									ancestor.left = currentNode;
								}
							}
						}
						//Left-left rotation
						else {
							//color the nodes.
							grandparent.color = RED;
							parent.color = BLACK;
							
							//Rotating parent to the top above child and grandparent.
							grandparent.left = parent.right;
							parent.right = grandparent;
							
							//Resetting root, if necessary.
							if(root == grandparent) {
								root = parent;
							}
							//Otherwise, resetting the top to the child of the great-grandparent.
							else {
								byte direction = stack.getDirection();
								RBNode<Item> ancestor = stack.pop();
								
								//Ancestor sets the current node in the place of the old child
								if(direction == RIGHT) {
									ancestor.right = parent;
								}
								else {
									ancestor.left = parent;
								}
							}
						}
						break;
					}
					//If the uncle is red, we perform recolouring and move up.
					else {
						//Recolor the nodes.
						parent.color = BLACK;
						uncle.color = BLACK;
						
						//If the grandparent is the root or stack size is zero...
						if(grandparent == root || stack.size == 0) {
							break;
						}
						grandparent.color = RED;
						//Reset for moving upwards.
						currentNode = grandparent;
						parentDirection = stack.getDirection();
						parent = stack.pop();
					}
				}
			}
			//Incrementing the size and returning true.
			size++;
			return true;
		}	
	}
	/**
	 * Method is not required for this assignment
	 */
	@Override
	public boolean addAll(Collection<? extends Item> arg0) {
		throw notNeeded();
	}

	/**
	 * Clears the set of all items.
	 * The set is then returned to its initialized state.
	 */
	@Override
	public void clear() {
		size = 0;
		root = null;
	}

	/**
	 * Returns whether the given item is contained within the set.
	 * @param item - the item that could be in the set.
	 * @return true, if the item was contained, or false otherwise.
	 */
	@Override
	public boolean contains(Object item) {
		Item removalItem;
//		try {
			removalItem = (Item)item;
//		}
//		catch(ClassCastException e) {
//			return false;
//		}
		
		RBNode<Item> currentNode = root;
		int compareValue;
		while(currentNode != null) {
			compareValue = removalItem.compareTo(currentNode.item);
			
			//If item is greater than the node's item, go right
			if(compareValue > 0) {
				currentNode = currentNode.right;
			}
			
			//If item is less than the node's item, go left.
			else if(compareValue < 0) {
				currentNode = currentNode.left;
			}
			
			//If item is equivalent to the node's item, return true. 
			else {
				return true;
			}
		}
		return false;
	}
	/**
	 * Method is not required for this assignment
	 */
	@Override
	public boolean containsAll(Collection<?> arg0) {
		throw notNeeded();
	}
	/**
	 * Method is not required for this assignment
	 */
	@Override
	public boolean isEmpty() {
		throw notNeeded();
	}
	/**
	 * Method is not required for this assignment
	 */
	@Override
	public Iterator<Item> iterator() {
		throw notNeeded();
	}

	/**
	 * Removes the given item from the Set, if it exists within it.
	 * @param item - the item to be removed. 
	 * @return true - if the item was removed, or false otherwise - like if the item was not in the set. 
	 */
	@Override
	public boolean remove(Object item) {
		Item removalItem;
//		try {
			removalItem = (Item)item;
//		}
//		catch(ClassCastException e) {
//			return false;
//		}
		
		//Immediate return false if we have an empty set.
		if(root == null)return false;
		
		RBNode<Item> currentNode = root;
		int compareValue;
		
		//Stack for storing nodes on the way down.
		QuickStack<Item> stack = new QuickStack<Item>();
		
		/*
		 * Navigating to the node, if it exists.
		 */
		//While the comparison value is nonzero and the current node is not null...
		while(currentNode != null) {
			compareValue = removalItem.compareTo(currentNode.item);
			
			//If the compare value is above zero, go right
			if(compareValue > 0) {
				stack.push(currentNode,RIGHT);
				currentNode = currentNode.right;
			}
			
			//If it is below zero go left...
			else if(compareValue < 0) {
				stack.push(currentNode,LEFT);
				currentNode = currentNode.left;
			}
			
			else {
				break;
			}
		}
		//If item does not exist, we return false.
		if(currentNode == null) {
			return false;
		}
		
		
		
		/*
		 * Node deletion if it exists. 
		 */
		RBNode<Item> removedNode;
		RBNode<Item> replacementNode;
		
		//If the currentNode is missing at least one child...
		if(currentNode.right == null || currentNode.left == null) {
			//System.out.println("One Child Removal");
			removedNode = currentNode;
			/*
			 * Removing the node.
			 */
			//If the left child exists
			if(currentNode.left != null) {
				replacementNode = currentNode.left;
				currentNode = currentNode.left;
			}
			
			//If the right child exists
			else if(currentNode.right != null) {
				replacementNode = currentNode.right;
				currentNode = currentNode.right;
			}
			
			//Otherwise...
			else {
				replacementNode = null;
				currentNode = null;
			}
			
			/*
			 * Resetting the tree.
			 */
			
			//If we travelled down the stack at all, set the parent's child to the current node.
			if(stack.size > 0) {
				//Then the actual removal.
				byte direction = stack.getDirection();
				RBNode<Item> parent = stack.pop();
				
				//Getting the parent to point to the child in the correct direction.
				if(direction == RIGHT) {
					parent.right = currentNode;
				}
				else {
					parent.left = currentNode;
				}
				//Putting parent back on stack for rebalancing. 
				stack.push(parent,direction);
			}
			
			//Otherwise... we must be removing the root! 
			else {
				this.root = currentNode;
				if(currentNode != null)currentNode.color = BLACK;
				--size;
				return true;
			}
		}
		
		//Otherwise, if unfortunately the node has two children
		else {
			RBNode<Item> swapNode = currentNode;
			stack.push(currentNode,RIGHT);
			
			//Get the right node's furthest left child - the item that will be closest to what is being removed.
			currentNode = currentNode.right;
			while(currentNode.left != null) {
				stack.push(currentNode,LEFT);
				currentNode = currentNode.left;
			}
			
			//Swapping items with the replacement node.
			Item swap;
			swap = swapNode.item;
			swapNode.item = currentNode.item;
			currentNode.item = swap;
			
			//If the right child exists, point to it. Otherwise, set to null.
			removedNode = currentNode;
			if(currentNode.right != null) {
				replacementNode = currentNode.right;
				currentNode = currentNode.right;
			}
			else {
				replacementNode = null;
				currentNode = null;
			}
			
			//Now make the parent point to it.
			byte direction = stack.getDirection();
			RBNode<Item> parent = stack.pop();
			
			
			//If meant to point right, or left..
			if(direction == RIGHT) {
				parent.right = currentNode;
			}
			else {
				parent.left = currentNode;
			}
			//Putting parent back on stack for rebalancing. 
			stack.push(parent,direction);
		}
		
		/*
		 * Rebalancing section...
		 */
		//If the removed node isn't red, we'll need to rebalance. Otherwise, we can skip rebalancing entirely! 
		if(removedNode.color != RED) {
			//If the replacement was red, we make the replacement node color black and we're done.
			if(replacementNode != null && replacementNode.color == RED) {
				replacementNode.color = BLACK;
				//System.out.println("Turning red replacement black");
			}
			else {
				boolean doubleBlack = true;
				while(doubleBlack && stack.size > 0) {
					//Getting the parent and the uncle.
					byte direction = stack.getDirection();
					RBNode<Item> parent = stack.pop();
					RBNode<Item> sibling = direction == RIGHT ? parent.left : parent.right;
					
					if(sibling.color == BLACK) {
						//CASE ONE: sibling has a right red child.
						if(sibling.right != null && sibling.right.color == RED) {
							//System.out.println("Case 1 - Rotation");
							RBNode<Item> newParent = null;
							
						    //If direction to the right, sibling to the left. Do a left right rebalance. 
							if(direction == RIGHT) {
								newParent = sibling.right; 
								//Coloring. 
								newParent.color = parent.color;
								parent.color = BLACK; 
								
								//Restructuring.
								sibling.right = newParent.left;
								parent.left = newParent.right; 
								newParent.left = sibling;
								newParent.right = parent;
							}
							
							//If direction to the left, sibling is to the right. Do a right right rebalance. 
							else {
								newParent = sibling; 
								//Coloring. 
								newParent.color = parent.color; 
								parent.color = BLACK;
								newParent.right.color = BLACK;
								
								//Restructuring. 
								parent.right = sibling.left;
								newParent.left = parent; 
							}
							
							//If the parent is the root, we must change the root to the new parent. 
							if(parent == root) {
								root = newParent;
							}
							else {
								//Putting the new parent in its spot.
								byte grandDir = stack.getDirection();
								RBNode<Item> grandparent = stack.pop();
								
								if(grandDir == RIGHT) {
									grandparent.right = newParent;
								}
								else {
									grandparent.left = newParent;
								}
							}
							break;
						}
						//CASE two: sibling has a left red child. 
						else if(sibling.left != null && sibling.left.color == RED) {
							//System.out.println("Case 2 - Rotation");
							RBNode<Item> newParent = null; 
							
							//If the direction is to the left, sibling is to the right. Do a right left rebalance. 
							if(direction == LEFT) {
								newParent = sibling.left;
								
								//Recoloring. 
								newParent.color = parent.color;
								parent.color = BLACK;
								
								//Restructuring. 
								parent.right = newParent.left;
								sibling.left = newParent.right;
								newParent.right = sibling;
								newParent.left = parent;
								
							}
							//If the direction is to the right, sibling is to the left. Do a left left rebalance. 
							else {
								newParent = sibling;
								
								//Recoloring. 
								newParent.color = parent.color;
								parent.color = BLACK; 
								newParent.left.color = BLACK;
								
								//Restructuring. 
								parent.left = newParent.right;
								newParent.right = parent;
							}
							if(parent == root) {
								root = newParent;
							}
							else {
								//Putting the new parent in place. 
								byte grandDir = stack.getDirection();
								RBNode<Item> grandparent = stack.pop();
								if(grandDir == RIGHT) {
									grandparent.right = newParent;
								}
								else {
									grandparent.left = newParent;
								}
								break;
							}
						}
					
						//Case 3: both children black (possibly even null). Perform recoloring. 
						else {
							//System.out.println("Case 3 - Recoloring");
							sibling.color = RED; 
							//If the parent is red, we can recolor without concern. 
							if(parent.color == RED || parent == root) {
								parent.color = BLACK; 
								break;
							}
							//Otherwise, we'll have to move up the tree and continue rebalancing. 
							else {
								continue;
							}
						}
					}
					//Case 4: Sibling is red. 
					else {
						//System.out.println("Case 4 - Transfer");
						
						//Right case
						if(direction == RIGHT) {
							RBNode<Item> newParent = sibling;
							
							//Recoloring.
							newParent.color = BLACK;
							parent.color = RED;
							
							//Restructuring. 
							parent.left = sibling.right; 
							newParent.right = parent; 
							
							//Putting the new parent in place. 
							if(root == parent) {
								root = newParent;
							}
							else {
								byte grandDir = stack.getDirection();
								RBNode<Item> grandparent = stack.pop();
								if(grandDir == RIGHT) {
									grandparent.right = newParent;
									stack.push(grandparent,RIGHT);
								}
								else {
									grandparent.left = newParent;
									stack.push(grandparent,LEFT);
								}
							}
							
							//Preparing for going up. 
							stack.push(newParent, RIGHT);
							stack.push(parent,RIGHT);
							continue;
						}
						//Left case. 
						else {
							RBNode<Item> newParent = sibling; 
							
							//Recoloring. 
							newParent.color = BLACK;
							parent.color = RED;
							
							//Restructuring
							parent.right = newParent.left;
							newParent.left = parent; 
							
							//Putting the new parent in place. 
							if(root == parent) {
								root = newParent;
							}
							else {
								byte grandDir = stack.getDirection();
								RBNode<Item> grandparent = stack.pop();
								if(grandDir == RIGHT) {
									grandparent.right = newParent;
									stack.push(grandparent,RIGHT);
								}
								else {
									grandparent.left = newParent;
									stack.push(grandparent,LEFT);
								}
							}
							
							//Preparing for going up. 
							stack.push(newParent, LEFT);
							stack.push(parent, LEFT);
						}
					}
					
				}
			}
		}
		
		
		
		--size;
		return true;
	}
	/**
	 * Method is not required for this assignment
	 */
	@Override
	public boolean removeAll(Collection<?> arg0) {
		throw notNeeded();
	}
	
	/**
	 * Method is not required for this assignment
	 */
	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw notNeeded();
	}
	/**
	 * Returns a string that has all the elements of the set in order.
	 * As with most of java collections, the first character is a '[' and the last is a ']'
	 * Inside the square braces is an in-order comma separated list of all the elements of the set.
	 * For example, the empty set yields: []
	 * Set containing the numbers 10, 25 and 17 will yield: [10, 25, 17]
	 * @return a string representing the set and its elements.
	 */
	public String toString() {
		StringBuilder setString = new StringBuilder();
		setString.append("[");
		if(root != null)travel(root,setString);
		if(setString.length() > 1) {
			setString = setString.replace(setString.length() - 2,setString.length(),"");
		}
		setString.append("]");
		return setString.toString();
	}
	/**
	 * Does an in-order traversal to build the string containing the elements in order. 
	 * <b> used for the toString method </b> 
	 * @param node - the node that is being traversed.
	 * @param builder - the string builder from the to string method.
	 */
	private void travel(RBNode<Item> node,StringBuilder builder) {
		if(node.left != null) {
			travel(node.left,builder);
		}
		builder.append(node.item.toString() + ", ");
		if(node.right != null) {
			travel(node.right,builder);
		}
	}
	/**
	 * Returns the size of the set - in a 32 bit signed integer.
	 * @return the size of the set.
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Method is not required for this assignment
	 */
	@Override
	public Object[] toArray() {
		throw notNeeded();
	}
	/**
	 * Method is not required for this assignment
	 */
	@Override
	public <T> T[] toArray(T[] arg0) {
		throw notNeeded();
	}
	/**
	 * Throws an exception for methods that are not required for this assignment. 
	 * @return an UnsupportedOperationException.
	 */
	private static RuntimeException notNeeded() {
		throw new UnsupportedOperationException();
	}
	/**
	 * Red-Black-Node class for Assignment 4.
	 * @author jordan
	 * @param <Item> - the class of item stored within the tree set.
	 */
	private static class RBNode<Item>{
		public RBNode<Item> right;
		public RBNode<Item> left;
		public byte color; 
		public Item item;
		public RBNode(Item item) {
			this.item = item;
			color = BLACK;
		}
		public String toString() {
			String leftString = "(none)";
			String rightString = "(none)";
			String colorString;
			if(left != null)leftString = left.item.toString();
			if(right != null)rightString = right.item.toString();
			
			/*
			 * 
			 */
			if(color == BLACK)colorString = "Black";
			else if(color == RED) colorString = "Red";
			else colorString = "DBLBLK";
			
			return String.format("Node: %7s  Color: %10s \t\tLeft: %7s Right: %7s",item, colorString, leftString, rightString);
		}
	}
	/**
	 * Quick single-linked stack used for the RB set. 
	 * @version Wednesday 27th 2019.
	 * @author jordan 
	 * @param <Item> - the class of items that are being stored in the tree.
	 */
	private static class QuickStack<Item>{
		private QuickNode<Item> head;
		public int size = 0;
		public QuickStack() {
			head = null;
		}
		/**
		 * Method for adding items onto the stack.
		 * @param node - the node to be stored on the stack.
		 */
		public void push(RBNode<Item> node,byte direction) {
			if(head == null) {
				head = new QuickNode<Item>(node,direction);
				++size;
			}
			else {
				QuickNode<Item> newNode = new QuickNode<Item>(node,direction);
				newNode.next = head;
				head = newNode;
				++size;
			}
		}

		/**
		 * Returns the direction at which the node on the stack points to its child. 
		 * @return the direction to the child. 
		 */
		public byte getDirection() {
			if(head != null) {
				return head.direction;
			}
			else throw new RuntimeException("Stack is empty.");
		}
		/**
		 * Method for popping a RB node off the stack. <br>
		 * <u>Usually, the <b>getDirection</b> method should be used first to get the parent direction.</u>
		 * @return the RBNode at the tip of the stack. 
		 */
		public RBNode<Item> pop(){
			if(head == null) {
				throw new RuntimeException("Stack is empty.");
			}
			else {
				RBNode<Item> returnNode = head.innerNode;
				head = head.next;
				--size;
				return returnNode;
			}
		}
		/**
		 * Quick node object for the stack.
		 * @author jordan
		 * @param <Item>
		 */
		private static class QuickNode<Item>{
			RBNode<Item> innerNode;
			public byte direction;
			QuickNode<Item> next;
			public QuickNode(RBNode<Item> innerNode,byte direction) {
				this.innerNode = innerNode;
				this.direction = direction;
			}
		}
	}
}
