package digitalDigest.messageDigest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;

public class SHA {
	
	private static String src="This's SHA";
	
	public static void jdkSHA1(){
		try {
			MessageDigest md=MessageDigest.getInstance("SHA");
			md.update(src.getBytes());
			System.out.println("jdk sha-1:"+Hex.encodeHexString(md.digest()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void bcSHA1(){
		Digest digest= new SHA1Digest();
		digest.update(src.getBytes(), 0, src.getBytes().length);
		byte[] sha1Bytes=new byte[digest.getDigestSize()];
		digest.doFinal(sha1Bytes, 0);
		System.out.println("bc sha-1:"+Hex.encodeHexString(sha1Bytes));
	}
	
	public static void bcSHA224(){
		Digest digest= new SHA224Digest();
		digest.update(src.getBytes(), 0, src.getBytes().length);
		byte[] sha224Bytes=new byte[digest.getDigestSize()];
		digest.doFinal(sha224Bytes, 0);
		System.out.println("bc sha-224:"+Hex.encodeHexString(sha224Bytes));
	}
	public static void bcSHA256(){
		Digest digest= new SHA256Digest();
		digest.update(src.getBytes(), 0, src.getBytes().length);
		byte[] sha256Bytes=new byte[digest.getDigestSize()];
		digest.doFinal(sha256Bytes, 0);
		System.out.println("bc sha-256:"+Hex.encodeHexString(sha256Bytes));
	}
	public static void bcSHA384(){
		Digest digest= new SHA384Digest();
		digest.update(src.getBytes(), 0, src.getBytes().length);
		byte[] sha384Bytes=new byte[digest.getDigestSize()];
		digest.doFinal(sha384Bytes, 0);
		System.out.println("bc sha-384:"+Hex.encodeHexString(sha384Bytes));
	}
	public static void bcSHA512(){
		Digest digest= new SHA512Digest();
		digest.update(src.getBytes(), 0, src.getBytes().length);
		byte[] sha512Bytes=new byte[digest.getDigestSize()];
		digest.doFinal(sha512Bytes, 0);
		System.out.println("bc sha-512:"+Hex.encodeHexString(sha512Bytes));
	}
	public static void ccSHA1(){
		System.out.println("cc sha-1:"+DigestUtils.sha1Hex(src.getBytes()));
		System.out.println("cc sha-256:"+DigestUtils.sha256Hex(src.getBytes()));
		System.out.println("cc sha-384:"+DigestUtils.sha384Hex(src.getBytes()));
		System.out.println("cc sha-512:"+DigestUtils.sha512Hex(src.getBytes()));
	}
	
	
	public static void main(String[] args) {
		bcSHA1();
		bcSHA224();
		bcSHA256();
		bcSHA384();
		bcSHA512();
		jdkSHA1();
		ccSHA1();
	}
	
	
}
