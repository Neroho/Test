package hbase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.security.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.Properties;



/**
 * hbase连接池
 * @author juyf
 * @since 2012/03/14
 */
@Component
public class HBaseDBFactory {
	private static HBaseDBFactory dbbean = null;//单实例
	private static Log log = LogFactory.getLog(HBaseDBFactory.class);

	private Boolean isInited = false;

    private Configuration configuration;
    private static Connection connection ;
    private Admin  baseadmin;  
    private static String hbase_zookeeper_quorum="20.26.23.38,20.26.23.39,20.26.23.40";
    private static String hbase_zookeeper_property_clientPort = "24002";
    private static String zookeeper_znode_parent = "/hbase";
   /**
	 * @return the zookeeper_znode_parent
	 */
	public  String getZookeeper_znode_parent() {
		return zookeeper_znode_parent;
	}

	/**
	 * @param zookeeper_znode_parent the zookeeper_znode_parent to set
	 */
	public void setZookeeper_znode_parent(String zookeeper_znode_parent) {
		HBaseDBFactory.zookeeper_znode_parent = zookeeper_znode_parent;
	}

	/**spring初始化时使用*/
    public HBaseDBFactory() {
    	//创建对象时就初始化initDBBean();
		//ConfigManager.loadZKModel();
	}
    
	//**在ConfigManager初始化后执行*//*
	protected  static void initDBBean() {
		try {				
			if (dbbean != null) {
				dbbean.destroy();
			}	
			dbbean = new HBaseDBFactory();
			dbbean.init(null);//SysConfigManager.getProperties());	
			
		} catch (Exception e) {
			log.fatal("initDBBean:",e);
			throw new RuntimeException("initDBBean:",e);
		} 
	}
		
	public synchronized void init(Properties properties) {
		log.info("SysDBBean init start!");
		System.out.println("-----------SysDBBean init start! -----------------------");
		this.configuration =  HBaseConfiguration.create();  //可能创建不了
		StringBuilder hbaseZk = new StringBuilder();
		for(String zk : ConfigManager.getProperty("hbase.zookeeper","").split(",")){
			if(hbaseZk.length() > 0){
				hbaseZk.append(",");
			}
			hbaseZk.append(zk.split(":")[0]);
			this.hbase_zookeeper_property_clientPort = zk.split(":")[1];
		}
		this.hbase_zookeeper_quorum = hbaseZk.toString();

		configuration.set("hbase.zookeeper.property.clientPort", this.hbase_zookeeper_property_clientPort);
		configuration.set("hbase.zookeeper.quorum",this.hbase_zookeeper_quorum);
		configuration.set("hbase.client.max.perregion.tasks","10");//1
		configuration.set("hbase.client.max.perserver.tasks","20");//2
		configuration.set("hbase.client.max.total.tasks","150");//100
		configuration.set("hbase.client.write.buffer","10485760");//10485760  缓存设置为6M
		System.out.println("-----------SysDBBean init start! end-----------------------");
		try {
			if(ConfigManager.isHBaseSasl()) {
				String keytabFile = ConfigManager.getProperty("hbase.zookeeper.user.keytab", "keytabFile");
				String principal = ConfigManager.getProperty("hbase.zookeeper.client.principal", "principal");
				String krb5_conf = ConfigManager.getProperty("hbase.zookeeper.java.security.krb5.conf", "krb5.conf");

				System.setProperty("java.security.krb5.conf", krb5_conf);
				configuration.set(ConfigManager.USERNAME_CLIENT_KEYTAB_FILE, keytabFile);
				configuration.set(ConfigManager.USERNAME_CLIENT_KERBEROS_PRINCIPAL, principal);
				User.login(configuration, ConfigManager.USERNAME_CLIENT_KEYTAB_FILE,
						ConfigManager.USERNAME_CLIENT_KERBEROS_PRINCIPAL, InetAddress.getLocalHost().getCanonicalHostName());
			}
			connection = ConnectionFactory.createConnection(configuration);
			System.out.println("-----------createConnection end-----------------------");
			this.baseadmin  = connection.getAdmin();
		} catch (Exception e) {
			e.printStackTrace();
			log.fatal(e.getMessage(),e);
			log.error(e.getLocalizedMessage(), e);
			throw new RuntimeException(e.getMessage(),e);
		}

		isInited = true;
		log.info("HBaseDBFactory init ok!");
	}

	public void destroy() {		
		try {
			if (connection != null) {
				connection.close();
			}
			if (baseadmin != null) {
				baseadmin.close();
			}
			
		} catch (Exception e) {			
			log.fatal(e.getMessage(), e);
		}
	}

	public static Configuration getConfiguration() {
		if (dbbean == null || !dbbean.isInited || dbbean.configuration == null) {
			initDBBean();
		}
		return dbbean.configuration;
	}

	/**
	 * HBase TablePool 
	 * @return
	 * @throws IOException 
	 */
	public static Connection getHBaseConnection()  {
		if (dbbean == null || !dbbean.isInited || dbbean.configuration == null) {
			initDBBean();
		}
       if( connection!=null){
    	return connection;  
       }
		Configuration	conf = getConfiguration();
		
		Connection conn = null;
		try {
			conn = ConnectionFactory.createConnection(conf);			
		} catch (IOException e) {
			log.fatal(e.getMessage(),e);
			throw new RuntimeException(e.getMessage(),e);
		}
		return conn;
		//return dbbean.connection;
	}
	
	public static  Admin getHBaseAdmin(){
		if (dbbean == null || !dbbean.isInited || dbbean.baseadmin == null) {
			initDBBean();
		}
		return dbbean.baseadmin;
	}
	
			
	/**
	 * 格式化时间为yyyyMMddHHmmss
	 * @return
	 */
	public static String sysdate() {
		return null;//DateTimeUtil.formatDate(new Date());
	}

	/**
	 * @return the hbase_zookeeper_quorum
	 */
	public String getHbase_zookeeper_quorum() {
		return hbase_zookeeper_quorum;
	}

	/**
	 * @param hbase_zookeeper_quorum the hbase_zookeeper_quorum to set
	 */
	public void setHbase_zookeeper_quorum(String hbase_zookeeper_quorum) {
		this.hbase_zookeeper_quorum = hbase_zookeeper_quorum;
	}

	/**
	 * @return the hbase_zookeeper_property_clientPort
	 */
	public String getHbase_zookeeper_property_clientPort() {
		return hbase_zookeeper_property_clientPort;
	}

	/**
	 * @param hbase_zookeeper_property_clientPort the hbase_zookeeper_property_clientPort to set
	 */
	public void setHbase_zookeeper_property_clientPort(
			String hbase_zookeeper_property_clientPort) {
		this.hbase_zookeeper_property_clientPort = hbase_zookeeper_property_clientPort;
	}
}
