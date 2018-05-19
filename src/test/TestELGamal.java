package test;

import java.math.BigInteger;
import java.lang.Math;
import java.util.Random;
import java.io.*;

public class TestELGamal {
	private static BigInteger x,y,g,p,k,c1; 
	private static int len=128;
	
	 public static BigInteger getPrime(int bitLenth) {
	        BigInteger p = BigInteger.probablePrime(bitLenth, new Random());
	        while(!p.isProbablePrime(6)) {
	            p.nextProbablePrime();
	        }
	        return p;
	    }
	 public TestELGamal produce(){
		 TestELGamal testELGamal=new TestELGamal();
		 p = testELGamal.getPrime(len);
		 g = new BigInteger(len,new Random());
		 x = new BigInteger(len,new Random());
		 y = g.modPow(x,p);
		 k = testELGamal.getPrime(len+1);
		 c1 = g.modPow(k,p);
		 System.out.println("produce:p = " + p + " g = " + g + " x = " + x + " k = " + k + " c1 = " + c1);
		 return testELGamal;
	 }
	 public static String deSerializeMsg (String string) {
	        String str = null;
	        byte [] b = new byte[string.length()];
	        for (int i = 0, j = 0; i <= string.length() - 3; i+=3,j++) {
	            b[j] = (byte)(Integer.parseInt(string.substring(i, i+3)) - 256);
	        }
	        str = new String(b);
	        return str;
	    }

	    //b ≡ y^k M ( mod p )
	    public static BigInteger encrypt (BigInteger m, BigInteger y, BigInteger k, BigInteger p) {
	        BigInteger b = null;
	        //BigInteger mi = new BigInteger(m.getBytes());
	        b = m.multiply(y.modPow(k,p)).mod(p);
	        return b;
	    }

	    //M ≡ b / a^x ( mod p )
	    public static BigInteger decrypt (BigInteger b, BigInteger a, BigInteger x, BigInteger p) {
	        BigInteger m = b.multiply(a.modPow(x.negate(),p)).mod(p);
	        return m;
	    }
	    public static String serializeMsg (String str) {
	        byte[] b = null;
	        StringBuffer sb  = new StringBuffer();
	        b = str.getBytes();
	        for(byte bb : b){
	            sb.append(((int)bb + 256));
	        }
	        return sb.toString();
	    }
	    
	    public  void encryptFile(TestELGamal Test,File file,File file2){
	       
	    	try {
	    		
	            InputStream is = new FileInputStream(file);
	            OutputStream os = new FileOutputStream(file2);
	            byte[] bytes1 = new byte[16];
	            while (is.read(bytes1)>0) {	            	
	            	BigInteger mb=new BigInteger(serializeMsg(bytes1.toString()));
	            	BigInteger c2 = Test.encrypt(mb, Test.produce().y, Test.produce().k, Test.produce().p);
	                byte[] de =c2.toString().getBytes();
	                System.out.println("c2:"+c2.toString());
	                os.write(de);
	            }
	            is.close();
	            os.close();
	           /* System.out.println("解密数据完成时间:"+time.format(day));
	            System.out.println("私钥:"+keyPair.getPrivate());*/
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    	public void decryptFile(TestELGamal Test,File file,File file2){
	    	try{
	    		
	    		InputStream is = new FileInputStream(file);
	            OutputStream os = new FileOutputStream(file2);
	            byte[] bytes1 = new byte[64];
	            while (is.read(bytes1) > 0) {
	            	//System.out.println(bytes1.toString());
	            	
	            	//BigInteger c2=new BigInteger(deSerializeMsg(bytes1.toString()));
	            	BigInteger md=Test.decrypt(new BigInteger(bytes1), Test.produce().c1,Test.produce().x,Test.produce().p);
	            	System.out.println(deSerializeMsg((md.toString())));
	            	byte[] de =deSerializeMsg((md.toString())).getBytes();
	            	System.out.println("md:"+md.toString());
		            os.write(de,0,de.length);
	            }
	            is.close();
	            os.close();
	    	  } catch (Exception e) {
		            e.printStackTrace();
		        }
	    	
	    }
	    public static void main (String [] args) {
	        File file = new File(
	                "D:\\endata.txt");
	        File newFile = new File(
	                "D:\\miwen.txt");
	        
	        File file1 = new File(
	                "D:\\dedata.txt");
	        TestELGamal t=new TestELGamal();
	    	t.produce();
	    	t.encryptFile(t, file, newFile);
            t.decryptFile(t, newFile, file1);
	    }	
}
