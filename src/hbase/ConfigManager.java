package hbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author tanghong
 * @date 2016-08-03
 * @since 1.0.0
 */
public class ConfigManager {

    private static volatile boolean zkModelInited = false;

    private static volatile boolean inited = false;

    public static final String USERNAME_CLIENT_KERBEROS_PRINCIPAL = "username.client.kerberos.principal";

    public static final String USERNAME_CLIENT_KEYTAB_FILE = "username.client.keytab.file";

    private static Properties properties = new Properties();

    public static void loadZKModel() {
        if (!zkModelInited) {
            init();
//            ZKModelLoader.loadZKModel(properties);
            zkModelInited = true;
        }
    }

    /**
     * 配置加载,默认使用类路径下的config.properties,也可以通过启动参数:-Dconfig.properties.file来指定配置文件位置
     */
    public static void init() {
        if(!inited) {
            String configFile = System.getProperty("config.properties.file");
            try {
                if (configFile != null && new File(configFile).exists()) {
                    InputStream in = new FileInputStream(configFile);
                    properties.load(in);
                    in.close();
                } else {
                    configFile = "classpath:/config.properties";
                    properties.load(ConfigManager.class.getResourceAsStream("/config.properties"));
                }
            } catch (Exception e) {
                throw new IllegalStateException("error loading config file at " + configFile, e);
            }
        }
    }

    public static String getProperty(String key, String defVal) {
        if(!inited){
            init();
        }
        return properties.getProperty(key, defVal);
    }

    public static String getProperty(String key) {
        return properties.getProperty(key, null);
    }
    public static Boolean isHBaseSasl() {
        Boolean isSecurity = false;
        final String CFG = getProperty("hbase.zookeeper.sasl", "false");
        if ("TRUE".equalsIgnoreCase(CFG)) {
            isSecurity = true;
        }
        return isSecurity;
    }
}
