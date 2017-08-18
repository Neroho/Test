/**
 * 
 */
package ThreadTest.suspendedTest;

import org.apache.log4j.Logger;

/**
 * @author Nero
 * @鍒涘缓鏃ユ湡:2016-8-13
 */
public class AlternateSuspendResume implements Runnable {
	static Logger logger = Logger.getLogger(AlternateSuspendResume.class.getName ());
	private volatile int firstVal;
    private volatile int secondVal;
    //澧炲姞鏍囧織浣嶏紝鐢ㄦ潵瀹炵幇绾跨▼鐨勬寕璧峰拰鎭㈠
    private volatile boolean suspended;
    
  //鍒ゆ柇浜岃�鏄惁鐩哥瓑
    public boolean areValuesEqual(){
    	logger.info(firstVal + "," +secondVal);
        return ( firstVal == secondVal);
    }
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try{
			
			suspended = false;
			firstVal = 0;
			secondVal = 0;
			workMethod();
		}catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
	}
	public void workMethod() throws InterruptedException
	{
		int val = 1;
		while(true)
		{
			//浠呭綋绾跨▼鎸傝捣鏃讹紝鎵嶈繍琛岃繖琛屼唬鐮�
			waitWhileSuspended();
			
			stepOne(val);
			stepTwo(val);
			val++;
			logger.debug("val = "+val);
			
			//浠呭綋绾跨▼鎸傝捣鏃讹紝鎵嶈繍琛岃繖琛屼唬鐮�
			waitWhileSuspended();

			Thread.sleep(200); 
		}
	}
	/**
	 * @param val
	 */
	private void stepTwo(int eval) {
		secondVal = eval;
		logger.debug("secondVal ="+secondVal);
	}
	/**
	 * @param val
	 * @throws InterruptedException 
	 */
	private void stepOne(int eval) throws InterruptedException {
		firstVal = eval;
		logger.debug("firstVal ="+secondVal);
		Thread.sleep(200);
	}
	/**
	 * @throws InterruptedException 
	 * 
	 */
	private void waitWhileSuspended() throws InterruptedException {
		//杩欐槸涓�釜鈥滅箒蹇欑瓑寰呪�鎶�湳鐨勭ず渚嬨�
        //瀹冩槸闈炵瓑寰呮潯浠舵敼鍙樼殑鏈�匠閫斿緞锛屽洜涓哄畠浼氫笉鏂姹傚鐞嗗櫒鍛ㄦ湡鍦版墽琛屾鏌ワ紝 
        //鏇翠匠鐨勬妧鏈槸锛氫娇鐢↗ava鐨勫唴缃�閫氱煡-绛夊緟鈥濇満鍒�
		while(suspended)
			Thread.sleep(200);
	}
	
	public static void main(String[] args) {
		AlternateSuspendResume asr = new AlternateSuspendResume();
		Thread t =new Thread(asr);
		t.start();
		//浼戠湢1绉掞紝璁╁叾浠栫嚎绋嬫湁鏈轰細鑾峰緱鎵ц
		try
		{
			Thread.sleep(1000);
		}catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		for(int i = 0;i < 10;i++)
		{
			asr.suspendRequest();
			//璁╃嚎绋嬫湁鏈轰細娉ㄦ剰鍒版寕璧疯姹�
            //娉ㄦ剰锛氳繖閲屼紤鐪犳椂闂翠竴瀹氳澶т簬stepOne鎿嶄綔瀵筬irstVal璧嬪�鍚庣殑浼戠湢鏃堕棿锛屽嵆200ms锛�
            //鐩殑鏄负浜嗛槻姝㈠湪鎵цasr.areValuesEqual()杩涜姣旇緝鏃�
            //鎭伴�stepOne鎿嶄綔鎵ц瀹岋紝鑰宻tepTwo鎿嶄綔杩樻病鎵ц
			try
			{
				Thread.sleep(350);
			}catch(InterruptedException e)
			{ }
			
			 logger.info("asr.areValuesEqual()=" + 
	                    asr.areValuesEqual());
			 asr.resumeRequest();
			 try { 
	              	//绾跨▼闅忔満浼戠湢0~2绉�
	                Thread.sleep(
	                ( long ) (Math.random() * 2000.0) );
	            } catch ( InterruptedException x ) {
	                //鐣�
	            }
			
		}
		System.exit(0);
	}
	/**
	 * 
	 */
	private void suspendRequest() {
		// TODO Auto-generated method stub
		suspended = true;
	}
	/**
	 * 
	 */
	private void resumeRequest() {
		// TODO Auto-generated method stub
		suspended = false;
	}
}
