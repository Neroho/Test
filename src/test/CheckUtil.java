package test;

import java.security.MessageDigest;
import java.util.Arrays;

public class CheckUtil {
	private static final String token="imooc";
	public static boolean checkSignatur(String signature, String timestamp, String nocne) {
		// TODO Auto-generated method stub
		String arr[]=new String[]{token,timestamp,nocne};
		Arrays.sort(arr);
		StringBuffer sb=new StringBuffer();
		for (String string : arr) {
			sb.append(string);
		}
		String temp = getSha1(sb.toString());
		
		
		return temp.equals(signature);
	}
	
	public static String getSha1(String str){
		if(str == null || str.length()==0){
			return null;
		}
		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
		
		try{
			MessageDigest md=MessageDigest.getInstance("SHA");
			md.update(str.getBytes("UTF-8"));
			
			byte[] mdtmp = md.digest();
			int j = mdtmp.length;
			char buf[] = new char[j * 2];
			int k = 0;
			for (byte c : mdtmp) {
				buf[k++] = hexDigits[c >>> 4 & 0xf];
				buf[k++] = hexDigits[c & 0xf];
			}
			return new String(buf);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
