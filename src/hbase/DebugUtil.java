package hbase;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


public class DebugUtil {
   
    public static boolean level_debug=true;
    public static boolean level_exception=true;
    public static boolean level_thread=true;
    
	/**
	 * @return the level_debug
	 */
	public  boolean isLevel_debug() {
		return level_debug;
	}
	/**
	 * @param level_debug the level_debug to set
	 */
	public  void setLevel_debug(boolean level_debug) {
		DebugUtil.level_debug = level_debug;
	}
	/**
	 * @return the level_exception
	 */
	public  boolean isLevel_exception() {
		return level_exception;
	}
	/**
	 * @param level_exception the level_exception to set
	 */
	public  void setLevel_exception(boolean level_exception) {
		DebugUtil.level_exception = level_exception;
	}
	/**
	 * @return the level_thread
	 */
	public  boolean isLevel_thread() {
		return level_thread;
	}
	/**
	 * @param level_thread the level_thread to set
	 */
	public  void setLevel_thread(boolean level_thread) {
		DebugUtil.level_thread = level_thread;
	}
	
	public static void log_debug(Object e,String msg){
//		if(level_debug){
//			String filename = "/tmp/toptea_debug.out";
//			if(System.getProperty("topo.home")==null){
//				filename = "toptea_debug.out";
//				System.out.println("\n"+DateTimeUtil.formatDate(System.currentTimeMillis()) + (level_thread? " ["+Thread.currentThread().getName()+"]":"" ) + ((e != null)?e.getClass().getName():"null" ) + msg);
//			}
//			log_write(filename, "\n"+DateTimeUtil.formatDate(System.currentTimeMillis()) + (level_thread? " ["+Thread.currentThread().getName()+"]":"" ) + ((e != null)?e.getClass().getName():"null" ) + msg);
//		}
//		log_write("", "\n"+DateTimeUtil.formatDate(System.currentTimeMillis()) + (level_thread? " ["+Thread.currentThread().getName()+"]":"" ) + ((e != null)?e.getClass().getName():"null" ) + msg);
	}
	public static void log_Exception(Exception ex){
		if(level_exception){
			StringWriter sw = new StringWriter();			
			ex.printStackTrace(new PrintWriter(sw));
			
			log_debug(ex,sw.toString());	
		}
	}
	
	public static void log_write(String filename,String loginfo){
//		System.out.println(loginfo);
	}
	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		System.out.println(System.currentTimeMillis());
//		System.out.println(DateTimeUtil.getPeroidWeak(System.currentTimeMillis()));
//	      Date d = DateTimeUtil.parseDate("20160102121211");
//	     Date d2 = DateTimeUtil.parseDate("20150102121211");
//	     System.out.println(DateTimeUtil.formatDate(d2.getTime()));
//	     System.out.println(d.getTime());
	     System.out.println(365*24*60*60*1000);
	     System.out.println(12*30*24*60*60*1000);
//	     System.out.println(DateTimeUtil.getPeroidWeak( d.getTime()));
//	     System.out.println(DateTimeUtil.getPreviousPeroidWeak( d.getTime()));
	} 
		/**
		 * @param args
		 */
		public static void main2(String[] args) {
//		String data = "[{\"traceId\":\"248a399278152166fa313001\"," +
//				"\"id\":\"248a399278152166fa313003\"," +
//				"\"parentId\":\"0\"," +
//				"\"probeType\":\"APP\"," +
//				"\"hostIp\":\"10.185.18.248\"," +
//				"\"appName\":\"DEFAULT_SERVER_NAME1452075361047\"," +
//				"\"serviceName\":\"testService\"," +
//				"\"mainService\":true," +
//				"\"bizid\":\"testBizid\"," +
//				"\"sn\":\"12345678901\"," +
//				"\"sessionId\":\"http://localhost:8080\"," +
//				"\"opId\":\"testOpid\"," +
//				"\"menuId\":\"testMenu\"," +
//				"\"clientIp\":\"127.0.0.1\"," +
//				"\"url\":\"http://localhost:8080\"," +
//				"\"sampleRatio\":\"2\"," +
//				"\"startTime\":1452075361047," +
//				"\"elapsedTime\":0," +
//				"\"msgTime\":1452075379096," +
//				"\"success\":true}]" ;
		
		String data = "[{\"traceId\":\"248a399278152166fa313001\",\"id\":\"248a399278152166fa313003\",\"parentId\":\"0\",\"probeType\":\"APP\",\"hostIp\":\"10.185.18.248\",\"appName\":\"DEFAULT_SERVER_NAME1452075361047\",\"serviceName\":\"testService\",\"mainService\":true,\"bizid\":\"testBizid\",\"sn\":\"12345678901\",\"sessionId\":\"http://localhost:8080\",\"opId\":\"testOpid\",\"menuId\":\"testMenu\",\"clientIp\":\"127.0.0.1\",\"url\":\"http://localhost:8080\",\"sampleRatio\":\"2\",\"startTime\":1452075361047,\"elapsedTime\":0,\"msgTime\":1452075379096,\"success\":true},{\"traceId\":\"248a399278152166fa313008\",\"id\":\"248a399278152166fa313009\",\"parentId\":\"0\",\"probeType\":\"APP\",\"hostIp\":\"10.185.18.248\",\"appName\":\"DEFAULT_SERVER_NAME1452075361047\",\"serviceName\":\"testService\",\"mainService\":true,\"bizid\":\"testBizid\",\"sn\":\"12345678901\",\"sessionId\":\"http://localhost:8080\",\"opId\":\"testOpid\",\"menuId\":\"testMenu\",\"clientIp\":\"127.0.0.1\",\"url\":\"http://localhost:8080\",\"sampleRatio\":\"2\",\"startTime\":1452075361047,\"elapsedTime\":0,\"msgTime\":1452075379096,\"success\":true},{\"traceId\":\"248a399278152166fa313014\",\"id\":\"248a399278152166fa313015\",\"parentId\":\"0\",\"probeType\":\"APP\",\"hostIp\":\"10.185.18.248\",\"appName\":\"DEFAULT_SERVER_NAME1452075361047\",\"serviceName\":\"testService\",\"mainService\":true,\"bizid\":\"testBizid\",\"sn\":\"12345678901\",\"sessionId\":\"http://localhost:8080\",\"opId\":\"testOpid\",\"menuId\":\"testMenu\",\"clientIp\":\"127.0.0.1\",\"url\":\"http://localhost:8080\",\"sampleRatio\":\"2\",\"startTime\":1452075361047,\"elapsedTime\":0,\"msgTime\":1452075379096,\"success\":true},{\"traceId\":\"248a399278152166fa313012\",\"id\":\"248a399278152166fa313013\",\"parentId\":\"0\",\"probeType\":\"APP\",\"hostIp\":\"10.185.18.248\",\"appName\":\"DEFAULT_SERVER_NAME1452075361047\",\"serviceName\":\"testService\",\"mainService\":true,\"bizid\":\"testBizid\",\"sn\":\"12345678901\",\"sessionId\":\"http://localhost:8080\",\"opId\":\"testOpid\",\"menuId\":\"testMenu\",\"clientIp\":\"127.0.0.1\",\"url\":\"http://localhost:8080\",\"sampleRatio\":\"2\",\"startTime\":1452075361047,\"elapsedTime\":0,\"msgTime\":1452075379096,\"success\":true}]";
		
			   System.out.println("---data---"+data);
                
         
        List<RowBean> value = HbaseTraceManager.getBeanfromJsonArray(data);
//        SRVRowBean bean =(SRVRowBean) value.get(2);
//        System.out.println("---1---"+ bean.getBizid());
//        System.out.println("---2---"+ bean.getTraceId());
	}

}
