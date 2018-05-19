package hbase;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * hbase表对象与RowBean对象实体对应。
 * hbase表记录json"\"total\":" + count + ",\"rows\":[]");
 *
 * @author juyf
 * @date 2012-3-15 9:45:32
 */
public class HBaseUtil {
    private static Logger log = LoggerFactory.getLogger(HBaseUtil.class);
    //	public final static String PN_ERRORCODE = "errorcode";
//	public final static String PN_ERRORINFO = "errorinfo";
//	public final static String PN_ROWS =  "rows";
//	public final static String PN_TOTAL = "total";//数组大小
//	public final static String PN_ROWKEY =  "rowkey"; 
//	public final static String PN_ROWDATA = "data"; //行数据
    public static int PN_ROWLIMIT = 9000;
    private static String PN_NAMESPACE = "default";

//    private static Map<String, List<Put>> cache = new HashMap<>();

    private static Map<String, Table> tableCache = new ConcurrentHashMap<String, Table>();
    private static FlushCommitTimer timer;

    //Start a timer when using hbase
    static{
        int timeWait = 60000;
//        try {
//            timeWait = Integer.valueOf(System.getProperty("hbase.flush.period"));
//        }catch (NumberFormatException e){
//            DebugUtil.log_debug(e, "hbase.flush.period is not a correct number"+System.getProperty("hbase.flush.period"));
//        }
        timer = new HBaseUtil.FlushCommitTimer(timeWait);
        //Start thread
        new Thread(timer).start();

    }

    public static TableName tableName_valueOf(String tablename) {
        String ns_table = null;
        if (tablename != null && tablename.indexOf(":") > 0) {
            ns_table = tablename;
        } else {
            if (PN_NAMESPACE == null || PN_NAMESPACE.length() == 0) {
                PN_NAMESPACE = "default";
            }
            ns_table = PN_NAMESPACE + ":" + tablename;
        }
        System.out.println("PN_NAMESPACE ====" + ns_table);
        return TableName.valueOf(ns_table);
    }

    /**
     * @return the pN_ROWLIMIT
     */
    public int getPN_ROWLIMIT() {
        return PN_ROWLIMIT;
    }


    /**
     * @param pN_ROWLIMIT the pN_ROWLIMIT to set
     */
    public void setPN_ROWLIMIT(int pN_ROWLIMIT) {
        PN_ROWLIMIT = pN_ROWLIMIT;
    }


    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();


    /**
     * 保存许多对象，必须同一个表的对象
     *
     * @param connpool
     * @param beans
     */
    public final static void saveRowBean(Connection connpool, List<RowBean> beans) {
        Table table = null;
        try {
            long start = System.currentTimeMillis();
            for (RowBean bean : beans) {
                Put put = new Put(Bytes.toBytes(bean.getRowkey()));
                put.setWriteToWAL(false);
//                List<Object[]> list = HBaseUtil.invokeGetMethod(bean);
//                long ts = bean instanceof AbstractTraceRowBean ? ((AbstractTraceRowBean) bean).getStartTime() : System.currentTimeMillis();
//                for (Object[] kv : list) {
//                    String col = (String) kv[0];
//                    String family = bean.getFamilyByColumn(col);
//                    put.addImmutable(Bytes.toBytes(family), Bytes.toBytes(col), ts, (byte[]) kv[1]);
//                }
                long ts = System.currentTimeMillis();
                String s = gson.toJson(bean);
                //log.error("=========> input"+s);
                put.addImmutable(Bytes.toBytes(bean.getFamilyByColumn("")), Bytes.toBytes("raw"),
                        ts, (byte[]) Bytes.toBytes(s));
                put.setDurability(Durability.SKIP_WAL);
//                List<Put> puts = cache.get(bean.getTableName());
//                if(puts == null){
//                    puts = new ArrayList<>();
//                    cache.put(bean.getTableName(), puts);
//                }
//                puts.add(put);


                table = tableCache.get(bean.getTableName());
                if (table == null) {
                    table = connpool.getTable(tableName_valueOf(bean.getTableName()));
                    ((HTable) table).setAutoFlushTo(false);
                    tableCache.put(bean.getTableName(), table);
                }
                table.put(put);
            }

            if(System.currentTimeMillis() - start > 500){
                log.error("saveRowBean cost :" + (System.currentTimeMillis() - start));
            }

//            for(Map.Entry<String,List<Put>> entry : cache.entrySet()){
//                if(entry.getValue().size() > 50){
//                    table = connpool.getTable(tableName_valueOf(entry.getKey()));
//                    table.put(entry.getValue());
//                    System.out.println("===========保存成功===="+entry.getKey()+"===="+entry.getValue().size());
//                    entry.getValue().clear();
//                }
//
//            }

        } catch (IOException e) {
            e.printStackTrace();
//            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        } finally {
//			if(table !=null){
//				try {
//					table.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//					log.error(e.getMessage(),e);
//					throw new RuntimeException(e.getMessage(),e);
//				}
//			}
        }
    }

    /**
     * @return the pN_NAMESPACE
     */
    public String getPN_NAMESPACE() {
        return PN_NAMESPACE;
    }


    /**
     * @param pN_NAMESPACE the pN_NAMESPACE to set
     */
    public void setPN_NAMESPACE(String pN_NAMESPACE) {
        PN_NAMESPACE = pN_NAMESPACE;
    }

    /**
     * @Author
     */
    static class FlushCommitTimer implements Runnable {
        private final int DEFAULT_PERIOD = 10000;

        private int mPeriod; // time wait for flushCommit in milisecond
        private boolean stop = false;

        /**
         * Constructor
         *
         * @param period time wait for flushCommit in milisecond, default is 10000
         */
        public FlushCommitTimer(int period) {
            if (period <= 0)
                this.mPeriod = DEFAULT_PERIOD;
            else
                this.mPeriod = period;

        }

        /**
         * Stop the timer
         */
        public void stop(){
            this.stop = true;
        }

        public void run() {
            while (!stop) {
                try {
                    //wait
//                    Utils.sleep(mPeriod);
                    //flush all the table in circle
                    Collection<Table> tables = tableCache.values();

                    for (Table table : tables) {
                        ((HTable) table).flushCommits();
                    }
                } catch (Exception e) {
                	e.printStackTrace();
                }
            }
        }
    }

}
