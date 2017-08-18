/**
 * 
 */
package ThreadTest.suspendedTest;

/**
 * @author Nero
 * @鍒涘缓鏃ユ湡:2016-8-13
 */
public class DeprecatedSuspendResume implements Runnable {

	 //volatile鍏抽敭瀛楋紝琛ㄧず璇ュ彉閲忓彲鑳藉湪琚竴涓嚎绋嬩娇鐢ㄧ殑鍚屾椂锛岃鍙︿竴涓嚎绋嬩慨鏀�
    private volatile int firstVal;
    private volatile int secondVal;
 
    //鍒ゆ柇浜岃�鏄惁鐩哥瓑
    public boolean areValuesEqual(){
        return ( firstVal == secondVal);
    }
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		  try{
	            firstVal = 0;
	            secondVal = 0;
	            workMethod();
	        }catch(InterruptedException x){
	            System.out.println("interrupted while in workMethod()");
	        }
	}
	
	public void workMethod() throws InterruptedException
	{
		int val =1;
		while(true)
		{
			stepOne(val);
			stepTwo(val);
			val++;
			Thread.sleep(200);//鍐嶆寰幆閽变紤鐪�00姣
		}
	}
	
	  //璧嬪�鍚庯紝浼戠湢300姣锛屼粠鑰屼娇绾跨▼鏈夋満浼氬湪stepOne鎿嶄綔鍜宻tepTwo鎿嶄綔涔嬮棿琚寕璧�
	 private void stepOne(int newVal) throws InterruptedException
	 {
		 firstVal = newVal;
		 Thread.sleep(300);  //妯℃嫙闀挎椂闂磋繍琛岀殑鎯呭喌
	 }
	 
	  private void stepTwo(int newVal)
	  {
		  secondVal = newVal;
	  }
	  
	  @SuppressWarnings("deprecation")
	public static void main(String[] args) {
		  DeprecatedSuspendResume dsr = new DeprecatedSuspendResume();
		  Thread t1 = new Thread(dsr);
		  t1.start();
		  
		//浼戠湢1绉掞紝璁╁叾浠栫嚎绋嬫湁鏈轰細鑾峰緱鎵ц
		  try 
		  {
			  Thread.sleep(1000);
		  } 
	      catch(InterruptedException x)
	      { }
		  for (int i = 0; i < 10; i++)
		  {
	          //鎸傝捣绾跨▼
			  t1.suspend();
	          System.out.println("dsr.areValuesEqual()=" + dsr.areValuesEqual());
	          //鎭㈠绾跨▼
	          t1.resume();
	          try
	          { 
	        	  //绾跨▼闅忔満浼戠湢0~2绉�
	              Thread.sleep((long)(Math.random()*2000.0));
	          }catch(InterruptedException x)
	          {
	                //鐣�
	            }
	        }
	        System.exit(0); //涓柇搴旂敤绋嬪簭
	}
}
