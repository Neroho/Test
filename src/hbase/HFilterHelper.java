/**
 * 
 */
package hbase;


import java.util.List;

import org.apache.hadoop.hbase.filter.ColumnPaginationFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;


/**
 * DDL,DML 操作HBase;建表,删表,修改表结构。
 * @author juyf
 * @date 2012/4/1
 */
public class HFilterHelper {
   
	/**
	 * 为了方便写代码。如果没有条件只是一个空list，查询返回空。
	 * @return
	 */
	public static FilterList newFilterList(){
		return new FilterList();
	}
	/**
	 * 获取Key前坠相同的
	 * @param out
	 * @param fromkey
	 * @param tokey
	 */
	public static void makeKeyPrefixFilter(FilterList filterlist, String keyPrefix){
		PrefixFilter filter = new PrefixFilter(Bytes.toBytes(keyPrefix));
		filterlist.addFilter(filter);		
	}
	/**
	字段值比较
	enum CompareOp {    
    LESS,小于   
    LESS_OR_EQUAL, 字段值小于等于<= 
    EQUAL,    
    NOT_EQUAL,不等于 
    GREATER_OR_EQUAL,  字段值大于等于>=  
    GREATER,    
    NO_OP,
   }
   **/
	public static void makeColumnValueCompareOp(FilterList filterlist,CompareOp cp, String family,String column ,String value){
		SingleColumnValueFilter filter = new SingleColumnValueFilter(
				Bytes.toBytes(family), Bytes.toBytes(column), cp,
				Bytes.toBytes(value));		
			
		filterlist.addFilter(filter);		
	}
	
	/**
	字段值比较
	enum CompareOp {    
    LESS,小于   
    LESS_OR_EQUAL, 字段值小于等于<= 
    EQUAL,    
    NOT_EQUAL,不等于 
    GREATER_OR_EQUAL,  字段值大于等于>=  
    GREATER,    
    NO_OP,
   }
   **/
	public static void makeColumnValueCompareOpOfBoolean(FilterList filterlist,CompareOp cp, String family,String column ,String value){
		SingleColumnValueFilter filter = new SingleColumnValueFilter(
				Bytes.toBytes(family), Bytes.toBytes(column), cp,
				Bytes.toBytes(Boolean.parseBoolean(value)));		
			
		filterlist.addFilter(filter);		
	}
	
	/**
	 * 字段值范围between    from  <=  value   < to 
	 * @param outlist
	 * @param column_name
	 * @param fromvalue
	 * @param tovalue
	 */
	public static void makeColumnValueBetween(FilterList filterlist, String family,String column ,String fromvalue ,String tovalue){
		SingleColumnValueFilter filter1 = new SingleColumnValueFilter(
				Bytes.toBytes(family), Bytes.toBytes(column), CompareOp.GREATER_OR_EQUAL,
				Bytes.toBytes(fromvalue));	
		SingleColumnValueFilter filter2 = new SingleColumnValueFilter(
				Bytes.toBytes(family), Bytes.toBytes(column), CompareOp.LESS,
				Bytes.toBytes(tovalue));	
		filterlist.addFilter(filter1);	
		filterlist.addFilter(filter2);
	}
	
	public static void makeFilterAND(FilterList filterlist,	List<Filter> listForFilters) {
		Filter filter = new FilterList(FilterList.Operator.MUST_PASS_ALL,
				listForFilters);
		filterlist.addFilter(filter);
	}

	public static void makeFilterOR(FilterList filterlist,List<Filter> listForFilters) {
		Filter filter = new FilterList(FilterList.Operator.MUST_PASS_ONE,
				listForFilters);
		filterlist.addFilter(filter);
	}
	
	/**
	 * 在匹配的row中，从offset开始取limit条；跟分页的区别在于它没有总条数。
	 * @param out
	 * @param fromkey
	 * @param tokey
	 * @deprecated  有错
	 */
	public static void makeCountGet(FilterList filterlist, int limit, int offset) {
		ColumnPaginationFilter filter = new ColumnPaginationFilter(limit,
				offset);
		filterlist.addFilter(filter);
	}


}
