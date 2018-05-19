package encrypt;

import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class ThreeDES {
	private static String src="This's 3DES";
	public static void jdkDES(){
		try {
			//init private key
			KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
			keyGenerator.init(112);
			SecretKey secretKey=keyGenerator.generateKey();
			byte[] byteKey=secretKey.getEncoded();
			
			DESedeKeySpec desKey=new DESedeKeySpec(byteKey);
			SecretKeyFactory factory=SecretKeyFactory.getInstance("DESede");
			Key convertSecretKey = factory.generateSecret(desKey);
			
			Cipher cipher=Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
			byte[] result=cipher.doFinal(src.getBytes());
			System.out.println("jdk 3DES:"+Hex.encodeHexString(result));
			
			cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
			result=cipher.doFinal(result);
			System.out.println("jdk 3DES:"+new String(result));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void bcDES(){
		try {
			Security.addProvider(new BouncyCastleProvider());
			//init private key
			KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede","BC");
			//keyGenerator.init(128);
			keyGenerator.init(new SecureRandom());
			SecretKey secretKey=keyGenerator.generateKey();
			byte[] byteKey=secretKey.getEncoded();
			
			DESedeKeySpec desKey=new DESedeKeySpec(byteKey);
			SecretKeyFactory factory=SecretKeyFactory.getInstance("DESede");
			Key convertSecretKey = factory.generateSecret(desKey);
			
			Cipher cipher=Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
			byte[] result=cipher.doFinal(src.getBytes());
			System.out.println("bc 3DES:"+Hex.encodeHexString(result));
			
			cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
			result=cipher.doFinal(result);
			System.out.println("bc 3DES:"+new String(result));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		jdkDES();
		bcDES();
		
	}
	
	
}
