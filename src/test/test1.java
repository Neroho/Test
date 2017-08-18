/**
 * 
 */
package test;

/**
 * @author Nero
 * @创建日期:2016-8-5
 */
public class test1 {
	public void play()
	{
		System.out.println("test1---play");
	}
	
	public static class test2 extends test1
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
	
	public static void main(String[] args) {
		test1 t = new test1();
		test1 test = new test2();
		test2 t1=new test2();
		t.play();
		test.play();
		t1.play();
		t1.work();
		
		long acc_codes=811121;
		long acc_code=Long.valueOf(String.valueOf(acc_codes).subSequence(1, String.valueOf(acc_codes).length()-1).toString());
		System.out.println(acc_code);
		
	}
}