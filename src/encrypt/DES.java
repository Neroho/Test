package encrypt;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class DES {
	private static String src="This's DES";
	public static void jdkDES(){
		try {
			//init private key
			KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
			keyGenerator.init(56);
			SecretKey secretKey=keyGenerator.generateKey();
			byte[] byteKey=secretKey.getEncoded();
			
			DESKeySpec desKey=new DESKeySpec(byteKey);
			SecretKeyFactory factory=SecretKeyFactory.getInstance("DES");
			Key convertSecretKey = factory.generateSecret(desKey);
			
			Cipher cipher=Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
			byte[] result=cipher.doFinal(src.getBytes());
			System.out.println("jdk DES:"+Hex.encodeHexString(result));
			
			cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
			result=cipher.doFinal(result);
			System.out.println("jdk DES:"+new String(result));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void bcDES(){
		try {
			Security.addProvider(new BouncyCastleProvider());
			//init private key
			KeyGenerator keyGenerator = KeyGenerator.getInstance("DES","BC");
			keyGenerator.getProvider();
			keyGenerator.init(56);
			SecretKey secretKey=keyGenerator.generateKey();
			byte[] byteKey=secretKey.getEncoded();
			
			DESKeySpec desKey=new DESKeySpec(byteKey);
			SecretKeyFactory factory=SecretKeyFactory.getInstance("DES");
			Key convertSecretKey = factory.generateSecret(desKey);
			
			Cipher cipher=Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
			byte[] result=cipher.doFinal(src.getBytes());
			System.out.println("bc DES:"+Hex.encodeHexString(result));
			
			cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
			result=cipher.doFinal(result);
			System.out.println("bc DES:"+new String(result));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		jdkDES();
		bcDES();
		
	}
}
