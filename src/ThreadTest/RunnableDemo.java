/**
 * 
 */
package ThreadTest;

/**
 * @author Nero
 * @创建日期:2016-8-21
 */
public class RunnableDemo {

	public static void main(String[] args) {
		MyThread1 myThread1 = new MyThread1();
		new Thread(myThread1).start();
		new Thread(myThread1).start();
		new Thread(myThread1).start();
		System.out.println("main");
	}
}

class MyThread1 implements Runnable
{
	private int tickets = 5;
	public void run()
	{
		for(int i =0;i < 10;i++)
		{
			if(tickets > 0)
				System.out.println("MyThread1 sold ticket "+ tickets--);
		}
	}
}