package hbase;

import java.lang.reflect.Method;
import java.util.Comparator;


/**
 *  泛型的排序类，可以依据指定属性字段，对类型实例排序。属性字段类型为基础数据类型或Comparator接口的实现类。
 *  Comparator<RowBean> comp = new RowComparator<RowBean>(CmOrigin.class, "origin_id", "desc");
    Collections.sort(list, comp);
 * */
public class RowComparator<T> implements Comparator<T> {
	
	private Class<? extends T> beanclass = null;
	private boolean sortOrder = true;// 默认asc
	private Class<? extends Comparable>  comparableClass = null;//参数类型
	//private Method setMethod = null;
	private Method getMethod = null;
    private String column = null;

	/**
	 * 排序类，字段名，ASC=true，DESC=false
	 * @param beanclass
	 * @param column
	 * @param order  默认asc , false:desc
	 */
	public RowComparator(Class<? extends T> beanclass,String column, boolean... order) {
		this.beanclass = beanclass;
		this.column = column;		
		if (order.length > 0) {
			sortOrder = order[0] ;//!"desc".equals(order);
		}
		init(column);
	}
	
	/**
	 * 检查排序参数类型，获得取数据的函数
	 * @param column_name
	 */
	private void init(String column_name) {
		try {
			String getmethod = "get" + Character.toUpperCase(column_name.charAt(0))
					+ column_name.substring(1);// get函数名
			
			getMethod = beanclass.getDeclaredMethod(getmethod);			
            if(getMethod == null){ return ;}
			// 遍历class,找出set函数
			String method_name = "set" + Character.toUpperCase(column_name.charAt(0))
					+ column_name.substring(1);

			Method[] mds = beanclass.getMethods();
			for (Method md : mds) {
				if (md.getName().equals(method_name)) {
					//setMethod = md;
					Class[] tpcc = md.getParameterTypes();
					if (tpcc.length == 1) {
						String type = tpcc[0].getName(); // parameter type
						//System.out.println("type:"+type);
						if (  tpcc[0].isPrimitive() ){//是否基础类型
							comparableClass =  tpcc[0];
						} else {
							try {
								tpcc[0].asSubclass(Comparable.class);//是否实现Comparable接口
								comparableClass = tpcc[0];
 					     	} catch(Exception e) {
 					     		throw new RuntimeException("不支持的数据类型,Can't Comparable Parameter Type:" + type,e);								
							}
						}
					}
					break;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(T o1, T o2) {
		int ret = 0;
		if (comparableClass == null) {
			throw new RuntimeException("数据类型不支持Comparable:" + column);
		}
		try {			
			Object v1 = getMethod.invoke(o1, new Object[0]);//返回值
			Object v2 = getMethod.invoke(o2, new Object[0]);			
			Comparable c1= (Comparable) v1;
			ret = c1.compareTo(v2);
		} catch (Exception e) {			
			throw new RuntimeException(e.getMessage(),e);
		}				
		return sortOrder ? ret : ret * -1;//true从小到大顺序，false倒序
	}
	

}
