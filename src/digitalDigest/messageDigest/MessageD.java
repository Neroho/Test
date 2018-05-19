package digitalDigest.messageDigest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Iterator;

import digitalDigest.RSA;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD4Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class MessageD {
	private static String src="that's interesting!";
	
	public static void jdkMD5(String str){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5Bytes=md.digest((src+str).getBytes());
			System.out.println("MD5:"+Hex.encodeHexString(md5Bytes));
		} catch (Exception e) {
		}
	}
	
	public static void jdkMD2(){
		try {
			MessageDigest md = MessageDigest.getInstance("MD2");
			byte[] md2Bytes=md.digest(src.getBytes());
			System.out.println("MD2:"+Hex.encodeHexString(md2Bytes));
		} catch (Exception e) {
		}
	}
	
	public static void bcMD4(){
		Security.addProvider(new BouncyCastleProvider());
		try {
			MessageDigest md =MessageDigest.getInstance("MD4");
			byte[] md5Bytes=md.digest(src.getBytes());
			System.out.println("MD4:"+Hex.encodeHexString(md5Bytes));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		Digest digest = new MD4Digest();
		digest.update(src.getBytes(), 0, src.getBytes().length);
		byte[] md4Bytes= new byte[digest.getDigestSize()];
		digest.doFinal(md4Bytes, 0);
		System.out.println("BC MD4:"+Hex.encodeHexString(md4Bytes));
	}
	
public static void bcMD5(){
		Digest digest = new MD5Digest();
		digest.update(src.getBytes(), 0, src.getBytes().length);
		byte[] md5Bytes= new byte[digest.getDigestSize()];
		digest.doFinal(md5Bytes, 0);
		System.out.println("BC MD5:"+Hex.encodeHexString(md5Bytes));
	}


public static void ccMD5(){
	System.out.println("ccMD5:"+DigestUtils.md5Hex(src.getBytes()));
	System.out.println("ccMD2:"+DigestUtils.md2Hex(src.getBytes()));
}
	public static void main(String[] args) {
//		ccMD5();
//		jdkMD2();
//		bcMD4();
//		jdkMD5();
//		bcMD5();
		String str="abcdefg";
		for (int i=0;i<=10;i++) {
			str+=i;
			jdkMD5(str);
		}
	}
	
}
