package test;

public class BinomialQueue<AnyType extends Comparable<? super AnyType>>{
	public BinomialQueue(){}
	public BinomialQueue(AnyType item){}
	
	public void merge(BinomialQueue<AnyType> rhs){}
	
	public void insert(AnyType x){
		merge(new BinomialQueue<AnyType>(x));
	}
	
	public AnyType findMin(){return null;}
	
	public AnyType deleteMin(){return null;}
	
	public boolean isEmpty(){return currentSize==0;}
	public void makeEmpty(){}
	
	
	private static class Node<AnyType>{
		Node(AnyType theElement){this(theElement,null,null);}
		
		Node(AnyType theElement,Node<AnyType> lt,Node<AnyType> nt){element=theElement;leftChild=lt;nextSibling=nt;}
		
		AnyType element;//the data in the node
		Node<AnyType> leftChild;//left child
		Node<AnyType> nextSibling;//right child
	}
	 
	private static final int DEFAULT_TRESS=1;
	private int currentSize;//items in priority queue
	private Node<AnyType> [] theTrees;//an array of tree roots
	
	private void expandTheTrees(int newNumTrees){}
	
	private Node<AnyType> combineTrees(Node<AnyType> t1,Node<AnyType> t2){
		if(t1.element.compareTo(t2.element) > 0){
			return combineTrees(t2,t1);
		}
		t2.nextSibling=t1.leftChild;
		t1.leftChild=t2;
		return t1;
	}
	
	private int capacity(){return (1<<theTrees.length) - 1;}
	
	private int findMinIndex(){return 0;}
	
	
	
	
}
