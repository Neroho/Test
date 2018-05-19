package encrypt;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class PBE {
	public static void main(String[] args) {
		jdkPBE();
		bcPBE();
	}
	private static String src="This's PEB";
	private static void bcPBE() {
		try {
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static void jdkPBE() {
		try {
			
			SecureRandom random=new SecureRandom();
			byte[] salt=random.generateSeed(8);
			String password = "timliu";
			PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray()); 
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWITHMD5andDES");
			Key key = factory.generateSecret(pbeKeySpec);
			
									
			// 加密
			PBEParameterSpec pbeParameterSpac = new PBEParameterSpec(salt, 100);//盐和迭代次数
			Cipher cipher = Cipher.getInstance("PBEWITHMD5andDES");
			cipher.init(Cipher.ENCRYPT_MODE, key, pbeParameterSpac);
			byte[] result = cipher.doFinal(src.getBytes());
			System.out.println("jdk pbe encrypt:" + Hex.encodeHexString(result));
			
			// 解密
			cipher.init(Cipher.DECRYPT_MODE, key, pbeParameterSpac);
			result = cipher.doFinal(result);
			System.out.println("jdk pbe decrypt:" + new String(result));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
