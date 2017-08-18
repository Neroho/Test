/**
 * 
 */
package algorithm;


import java.util.Scanner;

/**
 * @author Nero
 * @创建日期:2016-11-1
 */
public class QuickSort {
	
	
	public static void main(String[] args){
		QuickSort qs = new QuickSort();
		while(true)
		{
			int[] arr;
			int i =0;
			System.out.println("请输入连串数字，以','隔开,输入'quit'退出:");
			Scanner sc = new Scanner(System.in);
			String inputString = sc.next().toString();
			if(inputString.equals("quit"))
			{	
				System.out.println("quit successful");
				break;
			}
			String[] StringArray = inputString.split(",");
			arr = new int[StringArray.length];
			for (String string : StringArray) {
				arr[i] = Integer.parseInt(string);
				i++;
			}
			System.out.print("排序前: ");
			for(i = 0;i < arr.length-1;i++)
				System.out.print(arr[i]+" ");
			System.out.println(arr[arr.length-1]);
			qs.quick_sort(arr,0,arr.length-1);
			System.out.print("排序后: ");
			for(i = 0;i < arr.length-1;i++)
				System.out.print(arr[i]+" ");
			System.out.println(arr[arr.length-1]);
		}
	}
	
	public void quick_sort(int[] arr,int l,int e)
	{
		int i = l,j = e,x = arr[l];
		if(l < e)
		{
			while(i < j){
			while(i < j && x <= arr[j])//从右到左找出第一个小于x的数
				j--;
			if(i < j)
				arr[i++] = arr[j];
			while(i < j && x > arr[i])//从左到右找出第一个大于x的数
				i++;
			if(i < j)
				arr[j--] = arr[i];
			}
			arr[i] = x;
			quick_sort(arr,l,i-1);
			quick_sort(arr,i+1,e);
		}
	}
}
