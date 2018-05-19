package hbase;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;

//import com.ai.aif.log4x.jstorm.common.DateTimeUtil;

public class HBaseSqlUtil {

	protected static String getColumnQualifier(String p){
		//String family = null;
		String qualifier=null;
		int idx = p.indexOf(":");
		if (idx > 0 && idx < p.length() - 1) {
			//family = p.substring(0, idx);
			qualifier = p.substring(idx + 1);
		}
		return qualifier;
	}
	/**
	 * 汇总统计个数，统计字段值出现的次数
	 * keyFeild,至少2个字段名，如果只统计1个字段，就输入2个相同的字段名。格式："base:staffid","base:menu_id"
	 * beanClass,用于确定字段值类型，否则转换不过来。
	 * 返回：List<Map<String,String>>  Map<字段名，值> 统计值字段名："COUNT"
	 * 注意少量的数据可以这么统计。超过PN_ROWLIMIT不能统计了。
	 * 
	 * 输出例子 [{"COUNT":10,"hostIp":"10.3.2.131","serviceName":"OI_CheckOpLock"},{"COUNT":521,"hostIp":"10.3.2.131","serviceName":"OI_GetOperatorList"},{}]
	 */
	public static List<Map<String,Object>> groupCount(Connection connpool,String tableName,
			 Filter filter,Class<? extends RowBean> beanClass,String[] keyColumn,String startRow,String endRow) throws IOException  {
		if (keyColumn == null || keyColumn.length < 2)
			return null;
		String[] keyFeild = new String[keyColumn.length];//只留字段名不需要列族
		for (int i = 0; i < keyColumn.length; i++) {
			keyFeild[i] = getColumnQualifier(keyColumn[i]);
		}
		LocalCache  cache= new LocalCache();		
	 	//------
	    Table  table =  connpool.getTable(HBaseUtil.tableName_valueOf(tableName));
		Scan scan = new Scan();		
		if (filter != null) {			
			scan.setFilter(filter);			
		}	
		if(keyFeild.length > 0){
			HBaseUtil.addSelectColumn(scan,keyColumn);//只要输出这几个关键字段就够了。
			scan.setReversed(true);//无论取值范围都是反过来输出
		}
		if (startRow != null && !"".equals(startRow)) {
			scan.setStartRow(Bytes.toBytes(startRow));
		} else {
			//scan.setStartRow(Bytes.toBytes(DateTimeUtil.formatDate(new Date(),
					//"yyyyMM") + "01000000"));
		}
		if (endRow != null && !"".equals(endRow)) {
			scan.setStopRow(Bytes.toBytes(endRow));
		}else{
			//scan.setStopRow(Bytes.toBytes(DateTimeUtil.formatDate(new Date(),"yyyyMMdd")+"235959"));
		}
		
		System.out.println("==scan.getStartRow:"+Bytes.toString(scan.getStartRow()));		
		
		int size = 0;	
		int limit = HBaseUtil.PN_ROWLIMIT;
		List<RowBean>  rowlist = new ArrayList<RowBean>();
		
		int beginindex=0;//不跳过行
		
		ResultScanner rssn = null;
		try {
			rssn = table.getScanner(scan);
			Result r = null;
			while ((r = rssn.next()) != null) {
				KeyValue[] kvw = r.raw();
				if (kvw.length > 0) {
					Map<String, Object> rowmap = new HashMap<String, Object>();
					for (KeyValue kv : kvw) {
						mapKeyValue(rowmap, beanClass,
								Bytes.toString(kv.getQualifier()),
								kv.getValue());
					}
					addCount(cache, rowmap, keyFeild);// 一行一行记录累计
				}
			}
			
		} catch (Exception e) {		    
			throw new RuntimeException(e.getMessage(),e);
		} finally{
			if (rssn != null) {
				rssn.close();
			}			
		}			
		return outCountResult(cache,keyFeild);	
	}

    //把统计结果放cache里
	protected static void addCount(LocalCache  cache,Map<String, Object> rowmap,String[] keyFeild){
		if (rowmap == null || keyFeild == null	|| rowmap.size() < keyFeild.length) {
			return;
		}		
		String firstKey = keyFeild[0];
		String firstValue = rowmap.get(firstKey).toString();
		String secondValue = "";
		String value = null;
		for (int i = 1; i < keyFeild.length; i++) {
			value = rowmap.get(keyFeild[i]).toString();//不存在null
			if(value.isEmpty()) value="null";//空串 split会跳过，不能空串。			
			secondValue.concat((secondValue.length() > 0 ? ";" : "")).concat(value);			
		}
	
		if(!cache.existKey(firstValue, secondValue)){
			cache.putCacheObject(firstValue, secondValue, new AtomicLong(1) );	
			System.out.println("put=" + firstValue+"=="+ secondValue);	
		}else {
			AtomicLong aint = (AtomicLong) cache.getCachedObject(
					firstValue, secondValue);
			long a = aint.addAndGet(1);
			//System.out.println("count=" + a);				
		}		
	}
	
	protected static List<Map<String, Object>> outCountResult(
			LocalCache cache, String[] keyFeild) {
		 List<Map<String, Object>>  resultList = new ArrayList<Map<String, Object>>();
		 Map<String, Object> resultMap = null;
	     Map<String, Map<String, Object>> map =	cache.getLocalcache();
	   
		for (String firstvalue : map.keySet()) {
//			if(firstvalue.startsWith("SQL@")){
			Map<String, Object> vmap = map.get(firstvalue);
			for (String secondvalue : vmap.keySet()) {
				resultMap = new HashMap<String, Object>();
				resultMap.put(keyFeild[0], firstvalue);
				String[] sarry = secondvalue.split(";");
				for (int i = 0; i < sarry.length; i++) {
					resultMap.put(keyFeild[i + 1], sarry[i]);
				}
				resultMap.put("COUNT", ((AtomicLong)vmap.get(secondvalue)).get() );
				resultList.add(resultMap);
			}
		//	}
		}	     	
		return resultList;
	}

	public static Map<String,String> groupSum(Connection connpool,
			String tableName, String[] keyFeild,String valueFeild,Filter filter,String startRow,String endRow){
		return null;
	}

	/***把k-v转成map,注意参考bean里的字段的数据类型*/
	public static void mapKeyValue(Map<String, Object> rowmap, Class<? extends RowBean> beanClass,
			String attr_name, byte[] value) {
		if (rowmap==null || attr_name == null || attr_name.length() < 1 || value == null) {
			return ;
		}	
		if(value.length == 0){
			rowmap.put(attr_name, "null");
			return ;
		}
		String method_name = "set" + Character.toUpperCase(attr_name.charAt(0))
				+ attr_name.substring(1);		
		try {
			Method[] mds = beanClass.getMethods();
			for (Method md : mds) {
				if (md.getName().equals(method_name)) {
					Class[] tpcc = md.getParameterTypes();
					if (tpcc.length == 1) {
						String type = tpcc[0].getName(); // parameter type
						if (type.equals("java.lang.String")) {
							rowmap.put(attr_name,  Bytes.toString(value));
						} else if (type.equals("int")
								|| type.equals("java.lang.Integer")) {
							rowmap.put(attr_name,  new Integer( Bytes.toInt(value)) );
						} else if (type.equals("long")
								|| type.equals("java.lang.Long")) {
							rowmap.put(attr_name,   new Long( Bytes.toLong(value)) );
						} else if (type.equals("char") 
								|| type.equals("java.lang.Character")) {
							String  ch = Bytes.toString(value);
							if(ch!=null && ch.length() > 0){
								rowmap.put(attr_name,  new Character( ch.charAt(0)) );
							}
						} else if (type.equals("boolean")
								|| type.equals("java.lang.Boolean")) {
							rowmap.put(attr_name,  Boolean.valueOf( Bytes.toBoolean(value)) );
						} else if (type.equals("double")
								|| type.equals("java.lang.Double")) {
							rowmap.put(attr_name,  Double.valueOf(Bytes.toDouble(value)));	
						} else if (type.equals("float")
								|| type.equals("java.lang.Float")) {
							rowmap.put(attr_name,  Float.valueOf(Bytes.toFloat(value)));							 
						} else if (type.equals("java.util.Date")) {
							Date date = new Date();//DateTimeUtil.parseDate(Bytes.toString(value));
							if (date != null)
								rowmap.put(attr_name,  date );
						} else {
							System.out.println("RowBean不支持的数据类型set:" + type);
						}
 					}
					break;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(),e);
		}
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
