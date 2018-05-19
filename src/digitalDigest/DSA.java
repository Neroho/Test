package digitalDigest;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Hex;

import sun.security.provider.DSAPrivateKey;
import sun.security.provider.DSAPublicKey;

public class DSA {
	private static String src="This's DSA";
	public static void jdkDSA(){
		KeyPairGenerator keyPairGenerator;
		try {
			//init private key
			keyPairGenerator = KeyPairGenerator.getInstance("DSA");
			keyPairGenerator.initialize(512);
			KeyPair keyPair=keyPairGenerator.generateKeyPair();
			DSAPublicKey dsaPublicKey=(DSAPublicKey)keyPair.getPublic();
			DSAPrivateKey dsaPrivateKey=(DSAPrivateKey)keyPair.getPrivate();
			//sign
			PKCS8EncodedKeySpec pkcs8=new PKCS8EncodedKeySpec(dsaPrivateKey.getEncoded());
			KeyFactory keyFactory=KeyFactory.getInstance("DSA");
			PrivateKey privateKey=keyFactory.generatePrivate(pkcs8);
			Signature signature=Signature.getInstance("SHA1withDSA");
			signature.initSign(privateKey);
			signature.update(src.getBytes());
			byte[] result = signature.sign();
			System.out.println("jdk DSA:"+Hex.encodeHexString(result));
			
			//verify the sign
			X509EncodedKeySpec x509=new X509EncodedKeySpec(dsaPublicKey.getEncoded());
			keyFactory=KeyFactory.getInstance("DSA");
			PublicKey publicKey = keyFactory.generatePublic(x509);
			signature=Signature.getInstance("SHA1withDSA");
			signature.initVerify(publicKey);
			signature.update(src.getBytes());
			boolean flag=signature.verify(result);
			System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		jdkDSA();
	}
}
