package test;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class EncodeTest {
	public static void main(String[] args) throws Exception {
		String localChartSet = System.getProperty("file.encoding");   
	    System.out.println("localChartSet>>>>"+localChartSet);   //查看本地默认字符集  
	      
	    String  str ="aaaa中文的";  
	    byte[] gbkbt = str.getBytes("GB2312");       
	    byte[] utfbt = str.getBytes("utf-8");  
	    byte[] bt = str.getBytes();  
	  
	    String  gbkstr= new String(gbkbt, "GB2312");  //string 与byte[] 转换时字符集要保持一致  
	    String  utfstr= new String(utfbt, "utf-8");
	    str= new String(bt);   
	                 
	    System.out.println("gbkstr>>>>"+gbkstr);  
	    System.out.println("utfstr>>>>"+utfstr);  
	    System.out.println("str>>>>"+str);  

	       
	    gbkstr= new String(gbkbt, "GB2312");   //转换时字符集要保持一致 否则中文会出乱码  
	    utfstr= new String(utfbt, "utf-8");   
	    str= new String(bt);   
	    System.out.println("gbkstr>>>>"+gbkstr);  
	    System.out.println("utfstr>>>>"+utfstr);  
	    System.out.println("str>>>>"+str); 
	    
	    gbkstr= Base64.encodeBase64String(gbkbt);
	    utfstr= Base64.encodeBase64String(utfbt);   
	    str= Base64.encodeBase64String(bt);
	    System.out.println("gbkstr>>>>"+gbkstr);  
	    System.out.println("utfstr>>>>"+utfstr);  
	    System.out.println("str>>>>"+str);
	    
	    
	}

}
