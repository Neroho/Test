package cn.test;

import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * Description:本体用到康托展开，其公式为：X=an*(n-1)!+a(n-1)*(n-2)!+...ai*(i-1)!+...+a2*1!+a1*0!
 * 
 * @author Nero-ho
 *
 *关于康托展开：
 *有一个数组S=["a","b","c","d"]，它的其中之一个排列是S1=["b","c","d","a"]，现在欲把S1映射成X，需要怎么做呢？按如下步骤走起
 *X=a4∗3!+a3∗2!+a2∗1!+a1∗0!
 *1、首先计算n，n等于数组S的长度，n=4
 *2、再来计算a4=”b”这个元素在数组["b","c","d","a"]中是第几大的元素。”a”是第0大的元素，”b”是第1大的元素，”c”是第2大的元素，”d”是第3大的元素，所以a4=1
 *3、同样a3=”c”这个元素在数组["c","d","a"]中是第几大的元素。”a”是第0大的元素，”c”是第1大的元素，”d”是第2大的元素，所以a3=1
 *4、a2=”d”这个元素在数组["d","a"]中是第几大的元素。”a”是第0大的元素，”d”是第1大的元素，所以a2=1
 *5、a1=”a”这个元素在数组["a"]中是第几大的元素。”a”是第0大的元素，所以a1=0
 *6、所以X(S1)=1\*3!+1\*2!+1\*1!+0\*0!=9
 *7、注意所有的计算都是按照从0开始的，如果[“a”,”b”,”c”,”d”]算为第1个的话，那么将X(S1)+1即为最后的结果
 */
public class Compare1 {
// 3
// abcdefghijkl
// hgebkflacdji
// gfkedhjblcia
	static int charLength=12;//定义字符序列的长度
	
	public static void main(String[] args) {
	Scanner scanner=new Scanner(System.in);	
	while(scanner.hasNextInt()){
		int n=scanner.nextInt();
		String lines[]=new String[n];//存储字符序列的数组
		int res[]=new int[n];//储存结果的数组
		for(int i=0;i<n;i++){
			lines[i]=scanner.next();
			res[i]=calculate(lines[i]);
		}
		for(int i:res){
			System.out.println(i);
		}
	}
	}
	
	//计算某个字符序列的位次
	private static int calculate(String line){
		Set<Character> s=new TreeSet<Character>();
		for(char c:line.toCharArray()){
			s.add(c);
		}
		//存储每一个字符在该序列中是第几大的元素，然后将其值存储到counts数组中
		int counts[]=new int[s.size()];
		char[] chars=line.toCharArray();
		
		for(int i=0;i<chars.length;i++){
			Iterator<Character> iterator=s.iterator();
			int temp=0;
			Character next;
			while(iterator.hasNext()){
				next=iterator.next();
				if(next==chars[i]){
					counts[i]=temp;
					s.remove(next);
					break; 
				}else{
					temp++;
				}
			}
		}
		int sum=1;
		for(int i=0;i<counts.length;i++){
			sum=sum+counts[i]*factorial(charLength-i-1);
		}
		return sum;
	}
	
	//计算阶乘的函数
	private static int  factorial(int n) {
		if(n>1){
			return n*factorial(n-1);
		}else{
			return 1;
		}
		
	}
}
