package encrypt;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AES {
	public static void main(String[] args) {
		jdkAES();
		bcAES();
		
	}
	
	private static String src="This's AES";
	
	public static void jdkAES(){
		try {
			KeyGenerator keyGenerator= KeyGenerator.getInstance("AES");
			keyGenerator.init(new SecureRandom());
			SecretKey secretKey=keyGenerator.generateKey();
			byte[] srcBytes=secretKey.getEncoded();
			
			Key key = new SecretKeySpec(srcBytes,"AES");
			
			Cipher cipher =Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result=cipher.doFinal(src.getBytes());
			System.out.println("jdk AES:"+Hex.encodeHexString(result));
			
			cipher.init(Cipher.DECRYPT_MODE,key);
			result=cipher.doFinal(result);
			System.out.println("jdk AES:"+new String(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void bcAES(){
		try {
			Security.addProvider(new BouncyCastleProvider());
			KeyGenerator keyGenerator= KeyGenerator.getInstance("AES","BC");
			keyGenerator.getProvider();
			keyGenerator.init(128);
			SecretKey secretKey=keyGenerator.generateKey();
			byte[] srcBytes=secretKey.getEncoded();
			
			Key key = new SecretKeySpec(srcBytes,"AES");
			
			Cipher cipher =Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result=cipher.doFinal(src.getBytes());
			System.out.println("bc AES:"+Hex.encodeHexString(result));
			
			cipher.init(Cipher.DECRYPT_MODE,key);
			result=cipher.doFinal(result);
			System.out.println("bc AES:"+new String(result));
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
