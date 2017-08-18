/**
 * 
 */
package ThreadTest;

import ThreadTest.interruptedTest.SleepInterrupt;

/**
 * @author Nero
 * @创建日期:2016-8-21
 */
class MyThread extends Thread {
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
    private int flag=0;
	public MyThread(int flag) {
		super();
		this.flag = flag;
	}
   
}
 
public class ThreadDemo{
	private static int flag=0;
    public static void main(String[] args) throws InterruptedException{
        new MyThread(flag).start();
        new MyThread(flag).start();
        new MyThread(flag).start();
        while(flag<5)
        {
        	Thread.sleep(10);
        }
        System.out.println("main");
    }
}
