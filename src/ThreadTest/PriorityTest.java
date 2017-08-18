/**
 * 
 */
package ThreadTest;

/**
 * @author Nero
 * @创建日期:2016-8-10
 */
public class PriorityTest implements Runnable{
	private String threadId = null;
	public static void main(String[] args) {
        Thread thread1 = new Thread(new PriorityTest(1));
        Thread thread2 = new Thread(new PriorityTest(2));
        thread1.setPriority(1);
        thread2.setPriority(10);
        thread1.start();
        thread2.start();
}
	public PriorityTest(int a)
	{
		threadId = "thread"+String.valueOf(a);
	}
	public void run() 
	{
        for(int i=0;i<10;i++)
        {
                 System.out.println(this.threadId+":"+i);
        }
	}
}
