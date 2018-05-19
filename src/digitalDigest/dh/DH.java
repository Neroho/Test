package digitalDigest.dh;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

import org.apache.commons.codec.binary.Hex;

import com.google.common.base.Objects;


public class DH {
	public static void main(String[] args) {
		jdkDH();
	}
	private static String src = "this is DH";
	public static void jdkDH(){
		KeyPairGenerator keyPairGenerator;
		try {
			//sender key
			keyPairGenerator = KeyPairGenerator.getInstance("DH");
			keyPairGenerator.initialize(512);
			KeyPair senderKeypair=keyPairGenerator.generateKeyPair();
			byte[] senderPublicKeyEnc = senderKeypair.getPublic().getEncoded();//sender public key
			
			//receiver key
			KeyFactory keyFactory=KeyFactory.getInstance("DH");
			X509EncodedKeySpec x509=new X509EncodedKeySpec(senderPublicKeyEnc);
			PublicKey publicKey = keyFactory.generatePublic(x509);
			DHParameterSpec dhParameterSpec = ((DHPublicKey)publicKey).getParams();
			KeyPairGenerator receiverKeyPairGenerator=KeyPairGenerator.getInstance("DH");
			receiverKeyPairGenerator.initialize(dhParameterSpec);
			KeyPair receiverKeypair = receiverKeyPairGenerator.generateKeyPair();
			PrivateKey receiverPrivateKey = receiverKeypair.getPrivate();
			byte[] receiverPublicKeyEnc = receiverKeypair.getPublic().getEncoded();
			//create secret key
			KeyAgreement receiverKeyAgreement = KeyAgreement.getInstance("DH");
			receiverKeyAgreement.init(receiverPrivateKey);
			receiverKeyAgreement.doPhase(publicKey, true);
			SecretKey receiverDesKey = receiverKeyAgreement.generateSecret("DES");
			
			KeyFactory senderKeyFactory = KeyFactory.getInstance("DH");
			x509=new X509EncodedKeySpec(receiverPublicKeyEnc);
			PublicKey senderPublicKey = keyFactory.generatePublic(x509);
			KeyAgreement senderKeyAgreement = KeyAgreement.getInstance("DH");
			senderKeyAgreement.init(senderKeypair.getPrivate());
			senderKeyAgreement.doPhase(senderPublicKey, true);
			SecretKey senderDesKey = senderKeyAgreement.generateSecret("DES");
			if(Objects.equal(receiverDesKey, senderDesKey))
			{
				System.out.println("密钥相同");
			}
			
			//加密
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, senderDesKey);
			byte[] result = cipher.doFinal(src.getBytes());
			System.out.println("jdk dh encrypt:" + Hex.encodeHexString(result));
			
			// 解密
			cipher.init(Cipher.DECRYPT_MODE, receiverDesKey);
			result = cipher.doFinal(result);
			System.out.println("jdk dh decrypt:" + new String(result));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
