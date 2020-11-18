package cn.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class SearchFile {
	
	public static void main(String[] args) {
		SearchFile searchFile = new SearchFile();
		Scanner scanner = new Scanner(System.in);
		System.out.println("请输入你想要查找的文件名:");
		String scFile = scanner.nextLine();
		System.out.println(scFile);
		File[] roots = File.listRoots();//获取所有盘符
		for (File file : roots) {
			System.out.println(file.getAbsolutePath());
		}
		System.out.println("请输入你想要查找文件的磁盘:");
		for (File file : roots) {
			System.out.println(file.getAbsolutePath().substring(0, 1).toLowerCase()+ " for "+file.getAbsolutePath());
		}

		String scRoot = scanner.nextLine();
		if(scRoot != null){
			int finded = 0;
			for (File file : roots) {
				if(scRoot.toLowerCase().equals(file.getAbsolutePath().substring(0, 1).toLowerCase())){
					scRoot = file.getAbsolutePath();
					System.out.println("将从"+scRoot+"进行查找");
					finded = 1;
				}
			}
			if(finded == 0){
				System.out.println("输入的磁盘有误！将退出!");
			}
		}else{
			System.out.println("输入的磁盘有误！将退出!");
		}
		String path = searchFile.doSearch(scFile,scRoot);
	}

	private String doSearch(String scFile, String scRoot) {
		File file = new File(scRoot);
		File[] files = file.listFiles();
		List<String> list = new ArrayList<String>();
		if(files != null){
			for (File file2 : files) {
				if(file2.isFile())
				{
					//这一段是测试在console里面打印输出所有扫描到的文件，仅作调试使用。
//					try
//					{
//						System.out.println("查找中:" + file2.getCanonicalPath());
//					} catch (IOException e1)
//					{
//						e1.printStackTrace();
//					}
					
					//若有需要，可以同时传入参数，限制文件的名字。或者用FileFilter也行。
//					if(f.getName().substring(f.getName().length()-3).equals("xml"))
					
					//使用str1.indexOf(str2),只要文件名中包含str2就可以了。
					//用于模糊查找。
					try {
						if(file2.getCanonicalPath().indexOf(scFile) >= 0 && file2.getCanonicalPath().indexOf(".exe") >= 0)
						{
								System.out.println( file2.getCanonicalPath());
								list.add(file2.getCanonicalPath());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if(file2.isDirectory())
				{
//					System.out.println(file2.getName());
					doSearch(scFile, file2.getAbsolutePath());
				}
			}
			if(list.size() > 0){
			 Runtime rn = Runtime.getRuntime();  
		     for (String string : list) {
		    	 Process p = null;  
			     try {  
			         p = rn.exec(string);  
			     } catch (Exception e) {  
			         System.out.println("Error my exec ");  
			     }
			}
			 
			}
		}
		return null;
	}
}
