/**
 * 
 */
package algorithm;

import java.util.Scanner;

/**
 * @author Nero
 * @创建日期:2016-12-21
 */
public class ShellSort {
	
	public int[] swap(int x,int y)
	{
		int a=x;
		x=y;
		y=a;
		int[] arr={x,y};
		return arr;
	}
	
	public void shellsort1(int[] arr ,int n){
		int[] arr1=new int[n];
		for(int a=0;a<n;a++)
		{
			arr1[a]=arr[a];
		}
		int j,gap;
		
		for(gap=n/2;gap>0;gap/=2)
		{
			for(j=gap;j<n;j+=gap)
				if(arr1[j]<arr1[j-gap])
				{
					int temp=arr1[j];
					int k=j-gap;
					while(k>0&&arr1[k]>temp)
					{
						arr1[k+gap]=arr1[k];
						k-=gap;
					}
					arr1[k+gap]=temp;
				}
		}
		System.out.print("sort3 sort:");
		for(int k =0;k<n-1;k++)
			System.out.print(arr1[k]+" ");
		System.out.println(arr1[n-1]);
	}
	
	public void shellsort2(int[] arr ,int n){
		int[] arr1=new int[n];
		int[] arr2 = new int[2];;
		for(int a=0;a<n;a++)
		{
			arr1[a]=arr[a];
		}
		int i,j,gap;
		for(gap=n/2;gap>0;gap/=2)
			for(i=gap;i<n;i++)
				for(j=i-gap;j>=0&&arr1[j]>arr1[j+gap];j-=gap)
				{
					arr2=swap(arr1[j], arr1[j+ gap]);
					arr1[j]=arr2[0];
					arr1[j+ gap]=arr2[1];
				}
		System.out.print("sort2 sort:");
		for(int k =0;k<n-1;k++)
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
						
			System.err.println("第一种排序方式耗时："+(end1-start1));
			System.err.println("第二种排序方式耗时："+(end2-start2));
		}
	}
}
