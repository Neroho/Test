/**
 * 
 */
package test;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.omg.SendingContext.RunTime;

/**
 * @author Nero
 * @创建日期:2016-8-5
 */
public class Test1 {
	private static Date start=null;
	private static Date end=null;
	
	public void Test1(Date d1,Date d2){
		if(d1.getTime()-d2.getTime()>0){
			throw new NullPointerException();
		}else{
			start = d1;
			end = d2;
		}
		
	}
	
	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public void play()
	{
		System.out.println("test1---play");
	}
	
	public static class Test2 extends Test1
	{
		public void play()
		{
			System.out.println("test2---play");
		}
		public void work()
		{
			System.out.println("test2---work");
		}
	}
	
	public void test1thing(){
		Test1 t = new Test1();
		Test1 test = new Test2();
		Test2 t1=new Test2();
		t.play();
		test.play();
		t1.play();
		t1.work();
		
		long acc_codes=811121;
		long acc_code=Long.valueOf(String.valueOf(acc_codes).subSequence(1, String.valueOf(acc_codes).length()-1).toString());
		System.out.println(acc_code);
		
		t.setServingSize(240);
		t.setServings(8);
		t.setCalories(200);
		t.setSodium(35);
		t.setCaebohydrate(27);
		System.out.println(t.toString());
	}
	
	public void testplusplus(){
		int i=0;
		i=i++;
		System.out.println(i--);
		System.out.println(i++);
		System.out.println(--i);
		System.out.println(++i);
	}
	
	public void testTimes(){
		Date now = new Date();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Date end = new Date();
		System.out.println((end.getTime()-now.getTime())/1000);
		System.out.println("201711221436".substring(8, 10));
		 RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		 try {
			InetAddress addr = InetAddress.getLocalHost();
			System.out.println(addr.getHostName().toString());
			System.out.println(runtimeMXBean.getName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		 
	}
	
	public void forMessages(){
		String cdr="|576|11|1b0806284791fa00|6|20160909111010|20160909113359|13906506862|1|100.66.251.33|100.66.35.212|2152|2152|17713|52176488|571D|B0E2402|CMNET|103|1507862548666|1507862548688||1|9|0|0|10.47.253.160||55669|0|120.204.0.149||80|936|562|6|6|0|0|0|0|11|40|0|0|10|22|262144|1400|1|0|1|3|5|200|22|22|49|short.weixin.qq.com|http://short.weixin.qq.com/mmtls/0b676725||MicroMessenger Client|application/octet-stream|||211|0|||||3|0|22||999|||0|2017101310";
		String[] cdrStr=cdr.split("\\|");
		System.out.println(cdrStr.length);
		//for(int i=0;i<cdrStr.length;i++){
			//System.out.println(cdrStr[i]);
		//}
		Runtime run= Runtime.getRuntime();
		long startTime, endTime;
        long startMem, endMem;
        run.gc();
		startTime = System.currentTimeMillis();
        startMem = run.totalMemory() - run.freeMemory();
        System.out.println( "time: " + (new Date()) );
        System.out.println("used memory: " + startMem);
		Date now =new Date();
		StringBuffer sBuffer = new StringBuffer("菜鸟教程官网：");
		System.out.println(sBuffer);
		sBuffer.delete(0, sBuffer.capacity());
		System.out.println(sBuffer);
		sBuffer.append("1");
		System.out.println(sBuffer);
		for(int i=0;i<1000;i++){
			sBuffer.append(i);
		}
		sBuffer.append(";");
		System.out.println(sBuffer.toString());
		Date end =new Date();
		System.out.println(end.getTime()-now.getTime());
		endTime = System.currentTimeMillis();
        endMem = run.totalMemory() - run.freeMemory();
        System.out.println("time: " + (new Date()) + ", spend time:" + (endTime-startTime));
        System.out.println("used memory: " + endMem + ", added memory:" + (endMem-startMem));
        
        run.gc();
        
        startTime = System.currentTimeMillis();
        startMem = run.totalMemory() - run.freeMemory();
        System.out.println( "time: " + (new Date()) );
        System.out.println("used memory: " + startMem);
		now =new Date();
		String str = "菜鸟教程官网：";
		System.out.println(str);
		str="";
		System.out.println(str);
		str="1";
		for(int i=0;i<1000;i++){
			str=str+i;
		}
		System.out.println(str.hashCode()+":"+str);
		str=str+";";
		System.out.println(str.hashCode()+":"+str);
		end =new Date();
		System.out.println(end.getTime()-now.getTime());
		endTime = System.currentTimeMillis();
        endMem = run.totalMemory() - run.freeMemory();
        System.out.println("time: " + (new Date()) + ", spend time:" + (endTime-startTime));
        System.out.println("used memory: " + endMem + ", added memory:" + (endMem-startMem));
	}
	
	public static void main(String[] args) {
		Test1 test=new Test1();
		//test.test1thing();
		//test.testandand();
		test.testTimes();
		//test.forMessages();
		
	}
	
	private int servingSize=-1;
	private int servings=-1;
	private int calories=0;
	private int fat=0;
	private int sodium=0;
	private int caebohydrate=0;
	
	public Test1(){}

	public void setServingSize(int servingSize) {
		this.servingSize = servingSize;
	}

	public void setServings(int servings) {
		this.servings = servings;
	}

	public void setCalories(int calories) {
		this.calories = calories;
	}

	public void setFat(int fat) {
		this.fat = fat;
	}

	public void setSodium(int sodium) {
		this.sodium = sodium;
	}

	public void setCaebohydrate(int caebohydrate) {
		this.caebohydrate = caebohydrate;
	}

	@Override
	public String toString() {
		return "test1 [servingSize=" + servingSize + ", servings=" + servings + ", calories=" + calories + ", fat="
				+ fat + ", sodium=" + sodium + ", caebohydrate=" + caebohydrate + "]";
	}
	
	
	
}