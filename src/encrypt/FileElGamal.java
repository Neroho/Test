package encrypt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.DHParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.sun.jmx.snmp.Timestamp;

public class FileElGamal {
	private Cipher cipher2 =null;
	private Cipher cipher5 =null;
	
	public void init(){
		try {
			// 加入对BouncyCastle支持  
			Security.addProvider(new BouncyCastleProvider());
			
			// 1.初始化发送方密钥
			AlgorithmParameterGenerator algorithmParameterGenerator = AlgorithmParameterGenerator.getInstance("Elgamal");
			// 初始化参数生成器
			algorithmParameterGenerator.init(288);
			// 生成算法参数
			AlgorithmParameters algorithmParameters = algorithmParameterGenerator.generateParameters();
			// 构建参数材料
			DHParameterSpec dhParameterSpec = (DHParameterSpec)algorithmParameters.getParameterSpec(DHParameterSpec.class);
			// 实例化密钥生成器
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("Elgamal");	
			// 初始化密钥对生成器  
			keyPairGenerator.initialize(dhParameterSpec, new SecureRandom());
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			// 公钥
			PublicKey elGamalPublicKey = keyPair.getPublic();
			// 私钥 
			PrivateKey elGamalPrivateKey = keyPair.getPrivate();
//			System.out.println("Public Key:"+Base64.encodeBase64String(elGamalPublicKey.getEncoded()));
//			System.out.println("Private Key:"+Base64.encodeBase64String(elGamalPrivateKey.getEncoded()));
			
			// 2.私钥解密、公钥加密 ---- 加密
			// 初始化公钥  
	        // 密钥材料转换
			X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(elGamalPublicKey.getEncoded());
			// 实例化密钥工厂
			KeyFactory keyFactory2 = KeyFactory.getInstance("Elgamal");
			// 产生公钥
			PublicKey publicKey2 = keyFactory2.generatePublic(x509EncodedKeySpec2);
			// 数据加密 
			// Cipher cipher2 = Cipher.getInstance("Elgamal");
			cipher2 = Cipher.getInstance(keyFactory2.getAlgorithm()); 
			cipher2.init(Cipher.ENCRYPT_MODE, publicKey2);
//			System.out.println("私钥加密、公钥解密 ---- 加密:" + Base64.encodeBase64String(result2));
			
			// 3.私钥解密、公钥加密 ---- 解密
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(elGamalPrivateKey.getEncoded());
			KeyFactory keyFactory5 = KeyFactory.getInstance("Elgamal");
			PrivateKey privateKey5 = keyFactory5.generatePrivate(pkcs8EncodedKeySpec5);
//			Cipher cipher5 = Cipher.getInstance("Elgamal");
			cipher5 = Cipher.getInstance(keyFactory5.getAlgorithm()); 
			cipher5.init(Cipher.DECRYPT_MODE, privateKey5);
//			System.out.println("Elgamal 私钥加密、公钥解密 ---- 解密:" + Base64.encodeBase64String(result5));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void bcElGamal(byte[] src,OutputStream os1,OutputStream os2){
		try {
			
			byte[] result2 = cipher2.doFinal(src);
			os1.write(Base64.encodeBase64String(result2).getBytes());
			
			byte[] result5 = cipher5.doFinal(result2);
			os2.write(result5);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void bcElGamalEncry(byte[] src,OutputStream os1){
		try {
			
			byte[] result2 = cipher2.doFinal(src);
			os1.write(Base64.encodeBase64String(result2).getBytes());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void bcElGamalDecry(byte[] src,OutputStream os2){
		try {
			
			byte[] result5 = cipher5.doFinal(src);
			os2.write(result5);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		String localChartSet = System.getProperty("file.encoding");   
	    System.out.println("localChartSet>>>>"+localChartSet);   //查看本地默认字符集  
		 File file = new File(
	                "D:\\endata.txt");
	     File encry = new File(
	                "D:\\middata.txt");
	        
	     File decry = new File(
	                "D:\\dedata.txt");
	     InputStream is;
	     FileElGamal fileElGamal = new FileElGamal();
	     fileElGamal.init();
	     try {
	    	 InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
	    	 BufferedReader br = new BufferedReader(isr);
	    	 OutputStream os1 = new FileOutputStream(encry, true);
	          OutputStream os2 = new FileOutputStream(decry, true);
			is = new FileInputStream(file);
			byte[] bytes1 = new byte[32];
			long start = System.currentTimeMillis();
			Date startDate = new Date();
			while (is.read(bytes1) > 0) {
				System.out.println(Base64.encodeBase64String(bytes1));
				fileElGamal.bcElGamalEncry(bytes1,os1);
				bytes1 = new byte[32];
			}
			long mid = System.currentTimeMillis();
			Date midDate = new Date();
			
			String s = null;
	 		br = new BufferedReader(new FileReader(encry));
	 		while((s = br.readLine())!=null){//使用readLine方法，一次读一行
				System.out.println(s);
	 			fileElGamal.bcElGamalDecry(s.getBytes(),os2);
	 		 }
	 		br.close();
	 		long end = System.currentTimeMillis();
	 		Date endDate = new Date();
	 		
	 		System.out.println("start time :" + startDate.toString());
			System.out.println("mid time :" + midDate.toString());
			System.out.println("end time :" + endDate.toString());
			
			System.out.println("cost time :" + (mid-start)/1000);
			System.out.println("cost time :" + (end-mid)/1000);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
