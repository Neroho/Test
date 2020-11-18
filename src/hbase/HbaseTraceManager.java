///**
// *
// */
//package hbase;
//
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import com.google.gson.*;
//import org.apache.hadoop.hbase.client.Connection;
//import org.apache.hadoop.hbase.filter.FilterList;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * @author juyf
// */
//public class HbaseTraceManager {
//
//    private static final Logger LOG = LoggerFactory.getLogger(HbaseTraceManager.class);
//
//    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//
//    private static Map<String, String> serviceIdMap = new ConcurrentHashMap<String, String>();
//
//    private static volatile boolean loaded = false;
//
//    public HbaseTraceManager() {
//        if (!loaded) {
//            loadServiceIdMap();
//            loaded = true;
//        }
//    }
//
//    public void loadServiceIdMap() {
//        String sql = "SELECT csf.SERVICE_ID, csf.SERVICE_CODE FROM CSF_SRV_SERVICE_INFO csf ";
//        List<Map<String, Object>> list = DBUtil.getBomcTemplate().queryForList(sql);
//        for (Map<String, Object> item : list) {
//            StringBuffer buf = new StringBuffer("0000000000000");
//            String id = item.get("SERVICE_ID") + "";
//            buf.replace(13 - id.length(), 13, id);
//            serviceIdMap.put(item.get("SERVICE_CODE") + "", buf.toString());
//        }
//    }
//
//    public List<RowBean> recieveAndSaveHbase(String json) {
//        List<RowBean> beans = getBeanfromJsonArray(json);
//        return recieveAndSaveHbase(beans);
//    }
//
//    public List<RowBean> recieveAndSaveHbase(List<RowBean> beans) {
//        if (beans.size() < 1) {
//            return beans;
//        }
//
//        Connection connpool = HBaseDBFactory.getHBaseConnection();
//        for (RowBean bean : beans) {
//            AbstractTraceRowBean rowbean = (AbstractTraceRowBean) bean;
//            //每个对象都生成rowkey.
//            rowbean.generateAndSetRowKey();
//            rowbean.generateCostTime();//重算耗时
//        }
//
//        this.saveTraceIndexDB(connpool, beans);//把索引表建起来。
//        //保存日志入表
//        HBaseUtil.saveRowBean(connpool, beans);
//        return beans;
//    }
//
//    /**
//     * 保存索引
//     *
//     * @param connpool
//     * @param beans
//     */
//    public void saveTraceIndexDB(Connection connpool, List<RowBean> beans) {
//        final ArrayList<RowBean> indexkeyBeans = new ArrayList<RowBean>();
//        final ArrayList<RowBean> aoIndexkeyBeans = new ArrayList<RowBean>();//能力开放索引
//        for (final RowBean rowBean : beans) {
//            final AbstractTraceRowBean abstractTraceRowBean = (AbstractTraceRowBean) rowBean;
//            if (rowBean instanceof AccessBean) {
//                AccessBean accessBean = (AccessBean) rowBean;
//                AccessIndexRowBean accessIndexRowBean = new AccessIndexRowBean();
//                accessIndexRowBean.setTraceId(accessBean.getTraceId());
//                accessIndexRowBean.setAppId(accessBean.getAppId());
//                accessIndexRowBean.setUserId(accessBean.getUid());
//                accessIndexRowBean.setRowkey(accessBean.getAbilityCode() + accessBean.getStartTime() + accessBean.getTraceId().substring(0, 5));
//                accessIndexRowBean.setStartTime(accessBean.getStartTime());
//                aoIndexkeyBeans.add(accessIndexRowBean);
//            } else if (rowBean instanceof ServiceRouteBean) {
//                ServiceRouteBean serviceRouteBean = (ServiceRouteBean) rowBean;
//                SRIndexRowBean srIndexRowBean = new SRIndexRowBean();
//                srIndexRowBean.setStartTime(serviceRouteBean.getStartTime());
//                srIndexRowBean.setTraceId(serviceRouteBean.getTraceId());
//                srIndexRowBean.setCenterCode(serviceRouteBean.getCenterCode());
//                srIndexRowBean.setRowkey(serviceRouteBean.getServiceCode() + serviceRouteBean.getStartTime() + serviceRouteBean.getTraceId().substring(0, 5));
//                srIndexRowBean.setId(serviceRouteBean.getId());
//                aoIndexkeyBeans.add(srIndexRowBean);
//            }
//            //判断是否头部
////            else if (StringUtil.isEmpty(abstractTraceRowBean.getParentId())) {
//            else if ("ESB".equalsIgnoreCase(abstractTraceRowBean.getTableName())) {
//                final String s = abstractTraceRowBean.getSuccess() ? "1" : ErrorCodeManager.getErrorIndex(this.getRetCode(rowBean));
//                final ESBServiceIndexRowBean esbServiceIndexRowBean = new ESBServiceIndexRowBean();
//                esbServiceIndexRowBean.setTraceId(abstractTraceRowBean.getTraceId());
//                esbServiceIndexRowBean.setRowkey(HbaseTraceManager.serviceIdMap.get(abstractTraceRowBean.getServiceName()) + s + abstractTraceRowBean.getStartTime());
//                indexkeyBeans.add(esbServiceIndexRowBean);
//                if (StringUtil.isEmpty(this.getSn(rowBean))) {
//                    continue;
//                }
//                final ESBSNIndexRowBean esbsnIndexRowBean = new ESBSNIndexRowBean();
//                esbsnIndexRowBean.setTraceId(abstractTraceRowBean.getTraceId());
//                esbsnIndexRowBean.setServiceName(abstractTraceRowBean.getServiceName());
//                esbsnIndexRowBean.setRowkey(this.getSn(rowBean) + s + abstractTraceRowBean.getStartTime());
//                indexkeyBeans.add(esbsnIndexRowBean);
//            }
//        }
//
//        HBaseUtil.saveRowBean(connpool, indexkeyBeans);
//        if (aoIndexkeyBeans.size() > 0) {
//            HBaseUtil.saveIndexRowBean(connpool, aoIndexkeyBeans);
//        }
//    }
//
//    public static List<RowBean> fromJsonArray(String data) {
//        List<RowBean> list = new ArrayList<RowBean>();
//        JsonParser s = new JsonParser();
//        JsonElement jsonE = s.parse(data);
//        JsonArray array = jsonE.getAsJsonArray();
//        Iterator<JsonElement> it = array.iterator();
//        for (; it.hasNext(); ) {
//            JsonElement e = it.next();
//            JsonObject jo = e.getAsJsonObject();
//            if (jo.get("serviceName").getAsString().endsWith(".heartbeat")) {
//                continue;
//            }
//            RowBean o = gson.fromJson(e, getTableClassByName(jo.get("probeType").getAsString()));
//            if (o instanceof AbstractTraceRowBean) {
//                if (((AbstractTraceRowBean) o).getTraceId() == null) {
//                    continue;
//                }
//            }
//            list.add(o);
//        }
//        return list;
//    }
//
//    public static List<RowBean> getBeanfromJsonArray(String json) {
////		String type = ConvertUtil.getJsonArrayFirstRowFieldValue(json, "probeType");
////		Class<? extends RowBean> clazz = getTableClassByName(type);
////		List<Object> beans= ConvertUtil.fromJsonArray(json, clazz);
////		List<RowBean> outlist=new ArrayList<RowBean>();
////		for(Object b:beans){
////			RowBean bean = (RowBean) b;
////			outlist.add(bean);
////		}
////		return outlist;
//
//        return fromJsonArray(json);
//    }
////    rowbean.getServiceName().endsWith(".heartbeat")
//
//    /**
//     * 同上，返回表名，其实没有必要，有bean就有表名了。
//     *
//     * @param json
//     * @param outlist
//     * @return
//     */
//    public static String getBeanfromJsonArray(String json, List<RowBean> outlist) {
//        String type = ConvertUtil.getJsonArrayFirstRowFieldValue(json, "probeType");
//        Class<? extends RowBean> clazz = getTableClassByName(type);
//        List<Object> beans = ConvertUtil.fromJsonArray(json, clazz);
//        for (Object b : beans) {
//            RowBean bean = (RowBean) b;
//            outlist.add(bean);
//        }
//
//        return type;
//    }
//
//
//    /***
//     * 取时间周期时间段内的主服务层日志表，然后拿去画图
//     **/
//    public List<SRVRowBean> getSRVBeanByTimePeroid(String peroid) {
//        List<SRVRowBean> rowBeans = new ArrayList<SRVRowBean>();
//        Connection connpool = HBaseDBFactory.getHBaseConnection();
//        SRVRowBean sRVRowBean = new SRVRowBean();
//
//        FilterList filterList = new FilterList();
//        HFilterHelper.makeKeyPrefixFilter(filterList, peroid);//前缀过滤器，以这个时间搓为周期的记录全取出来
//        List<? extends RowBean> beans = HBaseUtil.scanRowBean(connpool, sRVRowBean.getTableName(), filterList, null, SRVRowBean.class);
//        for (RowBean bean : beans) {
//            sRVRowBean = (SRVRowBean) bean;
//            rowBeans.add(sRVRowBean);
//        }
//
//        return rowBeans;
//    }
//
//    /**
//     * 简单的用周期取，只看key前缀即可
//     *
//     * @param peroid YYYYMMddHHmmss 时间格式，不是long
//     * @return
//     */
//    public static List<WEBRowBean> getWEBRowBeanByTimePeroid(String peroid) {
//        List<WEBRowBean> rowBeans = new ArrayList<WEBRowBean>();
//        Connection connpool = HBaseDBFactory.getHBaseConnection();
//        WEBRowBean sWEBRowBean = new WEBRowBean();
//
//        FilterList filterList = new FilterList();
//        HFilterHelper.makeKeyPrefixFilter(filterList, peroid);//前缀过滤器，以这个时间搓为周期的记录全取出来
//        List<? extends RowBean> beans = HBaseUtil.scanRowBean(connpool, sWEBRowBean.getTableName(), filterList, null, WEBRowBean.class);
//        for (RowBean bean : beans) {
//            sWEBRowBean = (WEBRowBean) bean;
//            rowBeans.add(sWEBRowBean);
//        }
//
//        return rowBeans;
//    }
//
//    /**
//     * 得到trace_id在库中的所有对象
//     *
//     * @param traceId
//     * @return
//     */
//    public static List<AbstractTraceRowBean> getBeansByTraceId(String traceId) {
//        List<AbstractTraceRowBean> traceBeans = new ArrayList<AbstractTraceRowBean>();
//        IndexTraceRowBean indexBean = new IndexTraceRowBean();
//        Connection connpool = HBaseDBFactory.getHBaseConnection();
//        FilterList filterList = new FilterList();
//        HFilterHelper.makeKeyPrefixFilter(filterList, traceId);//前缀过滤器，以这个时间搓为周期的记录全取出来
//        String startRow = traceId + ",00000000";
//        String stopRow = traceId + ",zzzzzzzz";
//        List<RowBean> beans = new ArrayList<RowBean>();
//        try {
//            int i = HBaseUtil.pageRowBean(beans, startRow, stopRow, connpool, indexBean.getTableName(), filterList, null, 1, HBaseUtil.PN_ROWLIMIT, IndexTraceRowBean.class);
//            System.out.println("IndexTrace size:" + i + " traceId:" + traceId);
////			List<? extends RowBean> beans =  HBaseUtil.scanRowBean(connpool, indexBean.getTableName(), filterList, null, IndexTraceRowBean.class);
//            for (RowBean bean : beans) {
//                indexBean = (IndexTraceRowBean) bean;
//                RowBean rbean = HBaseUtil.getRowBean(connpool, indexBean.getDetailTableName(), indexBean.getDetailTableRowkey(), getTableClassByName(indexBean.getDetailTableName()));
//                AbstractTraceRowBean abean = (AbstractTraceRowBean) rbean;
//
//                traceBeans.add(abean);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return traceBeans;
//    }
//
//    private String getRetCode(final RowBean rowBean) {
//        try {
//            return (String) rowBean.getClass().getMethod("getRetCode", (Class<?>[]) new Class[0]).invoke(rowBean, new Object[0]);
//        } catch (Exception ex) {
//            return "";
//        }
//    }
//
//    private String getSn(final RowBean rowBean) {
//        try {
//            return (String) rowBean.getClass().getMethod("getSn", (Class<?>[]) new Class[0]).invoke(rowBean, new Object[0]);
//        } catch (Exception ex) {
//            return "";
//        }
//    }
//
//}
