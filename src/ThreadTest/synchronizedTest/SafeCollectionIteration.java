/**
 * 
 */
package ThreadTest.synchronizedTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/**
 * @author Nero
 * @创建日期:2016-8-21
 */
public class SafeCollectionIteration {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
		List wordList = Collections.synchronizedList(new ArrayList());
		wordList.add("Iterators");
		wordList.add("require");
	    wordList.add("special");
	    wordList.add("handling");
	    
	    synchronized(wordList)
	    {
	    	Iterator it = wordList.iterator();
	    	while(it.hasNext())
	    	{
	    		String s = (String) it.next();
	    		System.out.println("found string: " + s + ", length=" + s.length());
	    	}
	    }
	    
	}
}
