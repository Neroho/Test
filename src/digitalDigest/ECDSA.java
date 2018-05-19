package digitalDigest;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Hex;


public class ECDSA {
	private static String src="This's ECDSA";
	public static void jdkECDSA(){
		try {
			//init private key
			KeyPairGenerator keyPairGenerator= KeyPairGenerator.getInstance("EC");
			keyPairGenerator.initialize(256);
			KeyPair keyPair=keyPairGenerator.generateKeyPair();
			ECPublicKey ecPublicKey=(ECPublicKey)keyPair.getPublic();
			ECPrivateKey ecPrivateKey=(ECPrivateKey)keyPair.getPrivate();
			//sign
			PKCS8EncodedKeySpec pkcs8=new PKCS8EncodedKeySpec(ecPrivateKey.getEncoded());
			KeyFactory keyFactory=KeyFactory.getInstance("EC");
			PrivateKey privateKey=keyFactory.generatePrivate(pkcs8);
			Signature signature=Signature.getInstance("SHA1withECDSA");
			signature.initSign(privateKey);
			signature.update(src.getBytes());
			byte[] result = signature.sign();
			System.out.println("jdk ECDSA:"+Hex.encodeHexString(result));
			
			//verify the sign
			X509EncodedKeySpec x509=new X509EncodedKeySpec(ecPublicKey.getEncoded());
			keyFactory=KeyFactory.getInstance("EC");
			PublicKey publicKey = keyFactory.generatePublic(x509);
			signature=Signature.getInstance("SHA1withECDSA");
			signature.initVerify(publicKey);
			signature.update(src.getBytes());
			boolean flag=signature.verify(result);
			System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		jdkECDSA();
	}
}
