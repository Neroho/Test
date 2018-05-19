package hbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 抽象的表记录，实现getTableName()，getColumnFamilyMatadata()和equals()
 * 
 * @author juyf  
 * @version 1.0
 * Date  2012-2-13 9:45:32
 */
public abstract class RowBean   implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4096730789757672898L;

	/**得到表名可以用*/
	public abstract String getTableName();
	
	private static final String DEFAULT_COLUMN_FAMILY="base";
	
	/**每个列族对应的列，列名必须与RowBean的属性名一致；
	 * 数据类型存取要一致，否则读数据错，long存入，String读出是乱码,
	 * abstract */
	public Map<String,String[]> getColumnFamilyMetadata(){		 
		return new HashMap<String,String[]>();//默认是base,所以可以不写，总会有一个列族。
	};
//	{ 例如：  把列族和列名关系定义好，这样就知道字段保存在哪个列族里。
//	HashMap hm = new HashMap();
//	hm.put("base", new String[]{"probetype","id","parentid","traceid","starttime","endtime","costtime","msgtime","logtime"});
//	hm.put("info", new String[]{"bizid","success","servername","ip"});
//	hm.put("ext", new String[]{"sn","inmodecode","errmsg"});				
//    return hm;
//	}
	public String getFamilyByColumn(String Column){
//		String  family = null;
//		Map<String,String[]> metadata = this.getColumnFamilyMetadata();
//		if (metadata != null){
//			for (String fm : metadata.keySet()) {
//				String[] cols = metadata.get(fm);
//				for (String col : cols) {
//					if (Column.equalsIgnoreCase(col)) {
//						family = fm;
//						break;//应该可以直接return;
//					}
//				}
//				if (family != null) {
//					break;
//				}
//			}
//		}
//		return family != null ? family : DEFAULT_COLUMN_FAMILY;
		return DEFAULT_COLUMN_FAMILY;
		
		
	};
	
	/**主键*/
	private String rowkey = "";
	
	/**获得主键*/
	public String getRowkey() {
		return rowkey;
	}

	/** 设置 */
	public void setRowkey(String _rowKey) {
		rowkey = _rowKey;
	}

	/** 对比主键 */
	public boolean equals(Object obj) {
		if(!this.getClass().equals(obj.getClass())){
			return false;
		}
		return this.rowkey.equals(((RowBean)obj).getRowkey());
	}
	
	/** 生成 并设置RowKey*/
	public String generateAndSetRowKey(){
		return getRowkey();// 默认什么都不变
	}
	
	public static List<RowBean> fromJsonArray(String json,Class<? extends RowBean > clz){
		List<Object> beans= null;//ConvertUtil.fromJsonArray(json, clz);
		List<RowBean> outlist = new ArrayList<RowBean>();
		for(Object b:beans){
			RowBean bean =  (RowBean) b;
			outlist.add(bean);
		}
		return outlist;
	}
	
}

