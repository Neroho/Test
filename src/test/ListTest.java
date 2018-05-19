package test;

import java.util.ArrayList;
import java.util.List;

public class ListTest {
	public static void main(String[] args) {
		List<Integer> a=new ArrayList<Integer>();
		if(a!=null)
			System.out.println("list is not null");
		if(a.size()>0)
			System.out.println("a.size is not 0");
		
	}
}
