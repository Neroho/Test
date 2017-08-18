/**
 * 
 */
package algorithm;

import java.util.Scanner;

/**
 * @author Nero
 * @创建日期:2016-12-9
 */
public class InsertSort {
	public int[] swap(int x,int y)
	{
		int a=x;
		x=y;
		y=a;
		int[] arr={x,y};
		return arr;
	}
	
	public void sort1(int[] arr,int n)
	{
		int[] arr1=new int[n];
		for(int a=0;a<n;a++)
		{
			arr1[a]=arr[a];
		}
		
		int j =0,k=0;
		for(int i = 1;i< n;i++)
		{
			for(j=i-1;j>=0;j--)
				if(arr1[j]<arr1[i])
					break;
			if(j != i-1)
			{
				int temp = arr1[i];
				for( k =i-1;k>j;k--)
				{
					arr1[k+1] =arr1[k];
				}
				arr1[k+1]=temp;
			}
		}
		System.out.print("sort1 sort:");
		for(k =0;k<n-1;k++)
			System.out.print(arr1[k]+" ");
		System.out.println(arr1[n-1]);
	}
	
	public void sort2(int[] arr,int n)
	{
		int[] arr1=new int[n];
		for(int a=0;a<n;a++)
		{
			arr1[a]=arr[a];
		}
		int i,k;
		
		for(i = 1;i< n;i++)
		{
			if(arr1[i]<arr1[i-1])
				{
					int temp = arr1[i];
					for( k =i-1;k>=0&&arr1[k]>temp;k--)
					{
						arr1[k+1] =arr1[k];
					}
					arr1[k+1]=temp;
				}
		}
		
		System.out.print("sort2 sort:");
		for(k =0;k<n-1;k++)
			System.out.print(arr1[k]+" ");
		System.out.println(arr1[n-1]);
	}
	
	public void sort3(int[] arr,int n)
	{
		int[] arr1=new int[n];
		for(int a=0;a<n;a++)
		{
			arr1[a]=arr[a];
		}
		int[] arr2= new int[2];
		int i,k;
		for(i=1;i<n;i++)
		{
			for(k = i - 1; k >= 0 && arr1[k] > arr1[k + 1]; k--)  
			{
				arr2=swap(arr1[k], arr1[k+ 1]);
				arr1[k]=arr2[0];
				arr1[k+ 1]=arr2[1];
			}
		}
		System.out.print("sort3 sort:");
		for(k =0;k<n-1;k++)
			System.out.print(arr1[k]+" ");
		System.out.println(arr1[n-1]);
	}
	
	public static void main(String[] args) {
		InsertSort is = new InsertSort();
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
			long start1 = System.currentTimeMillis();
			is.sort1(arr, arr.length);
			long end1 = System.currentTimeMillis();
			
			System.out.print("排序前: ");
			for(i = 0;i < arr.length-1;i++)
				System.out.print(arr[i]+" ");
			System.out.println(arr[arr.length-1]);
			long start2 = System.currentTimeMillis();
			is.sort2(arr, arr.length);
			long end2 = System.currentTimeMillis();
			
			System.out.print("排序前: ");
			for(i = 0;i < arr.length-1;i++)
				System.out.print(arr[i]+" ");
			System.out.println(arr[arr.length-1]);
			long start3 = System.currentTimeMillis();
			is.sort3(arr, arr.length);
			long end3 = System.currentTimeMillis();
			
			System.err.println("第一种排序方式耗时："+(end1-start1));
			System.err.println("第二种排序方式耗时："+(end2-start2));
			System.err.println("第三种排序方式耗时："+(end3-start3));
		}
	}
}
