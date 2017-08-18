/**
 * 
 */
package test;
import java.util.*;
import java.util.Map.Entry;
/**
 * @author Nero
 * @创建日期:2016-8-10
 */
public class MapDemo {
	public static void main(String[] args) {
		Map<String,Integer> m1 = new HashMap<String,Integer>();
		Map<String,Integer> m2 = new TreeMap<String,Integer>();
		m1.put("one", new Integer(1));
		m1.put("two", new Integer(2));
		m1.put("three",new Integer(3));
		m2.put("A", new Integer(1));
		m2.put("B",new Integer(2));
		System.out.println(m1.size());
		System.out.println(m1.containsKey("one"));
		System.out.println(m2.size());
		System.out.println(m2.containsValue(new Integer(1)));
		if(m1.containsKey("two") == m2.containsKey("B"))
		{
			System.out.println(((Integer) m1.get("two")).intValue());
		}
		Map<String,Integer> m3= new HashMap<String,Integer>(m1);
		m3.putAll(m2);
		System.out.println(m3.size());
		Iterator<Entry<String, Integer>> it = m3.entrySet().iterator();
		long t1 = System.currentTimeMillis();
		while(it.hasNext())
		{
			Map.Entry<String,Integer> entry = (Map.Entry<String,Integer>) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			System.out.println(key + " " + value);
		}
		long t2 = System.currentTimeMillis();
		long t3 = t2 - t1;
		System.out.println(t3);
		
	}
}
