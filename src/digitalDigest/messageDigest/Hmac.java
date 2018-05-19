package digitalDigest.messageDigest;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;

public class Hmac {
private static String src="This's HMAC";
	
	public static void jdkHmacMD5(){
		try {
			KeyGenerator keyGenerator=KeyGenerator.getInstance("HmacMD5");
			SecretKey secretKey=keyGenerator.generateKey();
			byte[] key=secretKey.getEncoded();
//			byte[] key=Hex.decadeHex(new char[]('h','e','l','l','o','w',' ','w','o','r','l','d'));
			SecretKey restoreSecretKey =new SecretKeySpec(key,"HmacMD5");
			Mac mac=Mac.getInstance(restoreSecretKey.getAlgorithm());
			mac.init(restoreSecretKey);
			byte[] hmacMD5Bytes=mac.doFinal(src.getBytes());
			System.out.println("jdk hmacmd5:"+Hex.encodeHexString(hmacMD5Bytes));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void bcHmacMD5(){
		HMac hmac = new HMac(new MD5Digest());
		hmac.init(new KeyParameter(org.bouncycastle.util.encoders.Hex.decode("hello world")));
		hmac.update(src.getBytes(),0,src.getBytes().length);
		
		byte[] hmacMD5Bytes = new byte[hmac.getMacSize()];
		hmac.doFinal(hmacMD5Bytes, 0);
		
		System.out.println("bc hmacmd5:"+Hex.encodeHexString(hmacMD5Bytes));
		
	}
	
	public static void main(String[] args) {
		jdkHmacMD5();
		bcHmacMD5();
	}
}
