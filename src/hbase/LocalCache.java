package hbase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 虚拟机内部缓存，保证不重复而已，不要放多少数据，内存有限。
 * 主要缓存是否已经统计过这个周期。
 * 一年8760个小时，就算缓存一年， *1000个服务，也没有多少。就不清内存了
 * @author juyf
 * @version 从LocalCacheUtil简化来的，单纯的 Map套map。
 */
public class LocalCache {
	//全局的唯一序号
	private  AtomicInteger uuid_seq=new AtomicInteger(0);
	
	/**
	 * 获得全局唯一序号，1001开头一直到int最大值-100，
	 * @return
	 */
	public  String getUuidSeq(int length){
		double out = Math.pow(10,length);
		 if(uuid_seq.get() > ( Integer.MAX_VALUE - 1000)){
		 	 uuid_seq.set(0);//从头开始再计数
		 }//不担心多线程设置，因为get和Set是不会冲突的。
		 long sout = (long) (out + uuid_seq.addAndGet(1));//.incrementAndGet());
		 String str = "" + sout;
		 return str.substring(str.length()-length);
	}
    /**
     * 两层map关系，跟Redis Hashset结构类似，可以用redis替换，但没有必要。
     */
    private  Map<String,Map<String,Object>> localcache = new HashMap<String,Map<String,Object>>();
    private  Object lock = new Object();//访问要加锁，否则线程不安全
    /**
     * 线程不安全的，可以只做内部变量用。 不能出现在static 类里。
     */
	public Map<String, Map<String, Object>> getLocalcache() {
		return localcache;
	}
    /**
     * 是否存在
     * @param first
     * @param second
     * @return
     */
    public  boolean existKey(String first,String second){
		synchronized (lock) {
	    	if(localcache.containsKey(first)){
	    		Map<String,Object> right = localcache.get(first);
	    		return right.containsKey(second);
	    	}
    	}
    	return false;
    }
    
    public  Object getCachedObject(String first,String second){
    	synchronized (lock) {
	    	if(localcache.containsKey(first)){
	    		Map<String,Object> right = localcache.get(first);
	    		return right.get(second);
	    	}
    	}
    	return null;
    }
    public  void putCacheObject(String first,String second,Object value){
    	synchronized (lock) {
			if (!localcache.containsKey(first)) {
				Map<String, Object>  map =new HashMap<String, Object>();
				localcache.put(first, map);				
			}	
			Map<String, Object> right = localcache.get(first);
			right.put(second, value);
    	}

    }
    public  void clearCacheAll(){
    	synchronized (lock) {
		    localcache.clear();			
    	}

    }
    public  void clearCacheObject(String first){
    	synchronized (lock) {
			Map<String, Object> right = localcache.get(first);
			if(right!=null ) right.clear();
    	}

    }
   public  final String Key_cacheTTL="_cacheTTL";//cache创建时间。
   /**cache 的生存期检查，超过时间了全部清空
    * @deprecated 没有保留TTL信息，就没法清空了，为了编译通过保留这函数。
    * */
	public  void checkCacheTTL(String first,long ttl){
		synchronized (lock) {			
			Object a = getCachedObject(first, Key_cacheTTL);
			if (a != null) {
				long cachettl = (Long) a;
				if (System.currentTimeMillis() > (cachettl + ttl)) {
					clearCacheObject(first);
				}
			}			
  	}
	}

}
