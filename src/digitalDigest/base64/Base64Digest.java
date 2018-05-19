package digitalDigest.base64;


import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Digest {
	private static String src="This's base64Disgest";
	public static void jdkBase64(){
		try {
			BASE64Encoder encoder=new BASE64Encoder();
			String encode=encoder.encode(src.getBytes());
			System.out.println("jdk base64 encode:"+encode);
			
			BASE64Decoder decoder=new BASE64Decoder();
			System.out.println("jdk base64 decode:"+new String(decoder.decodeBuffer(encode)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void ccBase64(){
		byte[] encodeBytes=Base64.encodeBase64(src.getBytes());
		System.out.println("cc base64 encode:"+new String(encodeBytes));
		
		byte[] decodeBytes=Base64.decodeBase64(encodeBytes);
		System.out.println("cc base64 decode:"+new String(decodeBytes));

	}
	public static void bcBase64(){
		byte[] encodeBytes=org.bouncycastle.util.encoders.Base64.encode(src.getBytes());
		byte[] decodeBytes=org.bouncycastle.util.encoders.Base64.decode(encodeBytes);
		System.out.println("bc base64 encode:"+new String(encodeBytes));
		System.out.println("bc base64 decode:"+new String(decodeBytes));
		
	}
	public static void main(String[] args) {
		jdkBase64();
		ccBase64();
		bcBase64();
	}
}
