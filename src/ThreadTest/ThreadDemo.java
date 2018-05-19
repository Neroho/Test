/**
 * 
 */
package ThreadTest;

import ThreadTest.interruptedTest.SleepInterrupt;

/**
 * @author Nero
 * @创建日期:2016-8-21
 */
public class ThreadDemo extends Thread {
	private  int ticket = 5;
    public void run(){
        for (int i=0;i<10;i++)
        {
            if(ticket > 0){
                System.out.println("ticket = " +  ticket--);
            }
        }
        
        flag+=1;
    }
	private static int flag=0;
	public ThreadDemo(int flag) {
		super();
		this.flag = flag;
	}
	 public static void main(String[] args) throws InterruptedException{
	        new ThreadDemo(flag).start();
	        new ThreadDemo(flag).start();
	        new ThreadDemo(flag).start();
	        new ThreadDemo(flag).start();
	        new ThreadDemo(flag).start();
	        while(flag<5)
	        {
	        	Thread.sleep(3000);
	        }
	        System.out.println("main");
	    }
}
