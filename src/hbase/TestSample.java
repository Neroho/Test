package hbase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HRegionLocation;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.index.ColumnQualifier.ValueType;
import org.apache.hadoop.hbase.index.Constants;
import org.apache.hadoop.hbase.index.IndexSpecification;
import org.apache.hadoop.hbase.index.client.IndexAdmin;
import org.apache.hadoop.hbase.index.coprocessor.master.IndexMasterObserver;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.io.encoding.DataBlockEncoding;
import org.apache.hadoop.hbase.ipc.CoprocessorRpcChannel;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.protobuf.generated.AccessControlProtos;
import org.apache.hadoop.hbase.security.User;
import org.apache.hadoop.hbase.security.access.AccessControlLists;
import org.apache.hadoop.hbase.security.access.Permission;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * HBase Development Instruction Sample Code The sample code uses user information as source data,it introduces how to
 * implement businesss process development using HBase API
 */
public class TestSample {

    Log LOG = LogFactory.getLog(TestSample.class.getName());

    private TableName tableName = null;
    private Configuration conf = null;
    private Connection conn = null;
    public void testSample() {

        try {
            // specify the keytab file and principal
            String keytabFile = "user.keytab";
            String principal = "cx_zxdcx";
            tableName = TableName.valueOf("hbase_sample_table");

            //conf = login(keytabFile, principal);
            String configPath ="C:/Users/41304/Workspaces/Test/conf/";
            add2ClassPath(configPath);
            conf=login(configPath, principal);
            if(conf!=null)
                System.out.println("######### kerberos login success! ###########");
            else
                System.out.println("######### kerberos login failure! ###########");
            conn = ConnectionFactory.createConnection(conf);
            testCreateTable();

            dropTable();
        } catch (Exception e) {
            System.out.println("Failed to login "+e.toString());
            e.printStackTrace();
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (Exception e1) {
                    System.out.println("Failed to close the connection "+ e1.toString());
                    e1.printStackTrace();
                }
            }
        }
    }

    private Configuration login(String confDirPath, String principal) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        if (User.isHBaseSecurityEnabled(conf)) {
            //String confDirPath = System.getProperty("user.dir") + File.separator + "conf" + File.separator;
            //System.setProperty("java.security.auth.login.config", confDirPath + "jaas.conf");//在启动脚本中指定该配置项
            System.setProperty("java.security.krb5.conf", confDirPath + "krb5.conf");
            System.setProperty("java.security.auth.login.config", confDirPath + "jaas.conf");
            System.setProperty("username.client.keytab.file", confDirPath + "user.keytab");
            conf.set("username.client.keytab.file", confDirPath + "user.keytab");
            System.out.println(conf.get("username.client.keytab.file"));
            System.setProperty("hadoop.home.dir", "D:\\Downloads\\hadoop-common-2.2.0-bin-master\\hadoop-common-2.2.0-bin-master");
            //conf.addResource(new Path(confDirPath + "core-site.xml"));
            //conf.addResource(new Path(confDirPath + "hdfs-site.xml"));
            //conf.addResource(new Path(confDirPath + "hbase-site.xml"));
            try {
                System.setProperty("username.client.kerberos.principal", principal);
                conf.set("username.client.kerberos.principal", principal);
                System.out.println(conf.get("username.client.kerberos.principal"));
                System.out.println("*******************************");
                System.out.println("######user.dir:"+System.getProperty("user.dir"));
                System.out.println("######hbase.config.path:" + confDirPath);
                System.out.println("######username.client.kerberos.principal:" + principal);
                System.out.println("######java.security.auth.login.config:" + System.getProperty("java.security.auth.login.config"));
                System.out.println("######username.client.keytab.file:" + conf.get("username.client.keytab.file"));
                System.out.println("######java.security.krb5.conf:" + System.getProperty("java.security.krb5.conf"));
                System.out.println("*******************************");
                User.login(conf, "username.client.keytab.file", "username.client.kerberos.principal",
                        InetAddress.getLocalHost().getCanonicalHostName());
            } catch (IOException e) {
                throw new IOException("kerberos Login failed."+e);
            }
        }
        return conf;
    }
    private void add2ClassPath(String path)throws Exception{
        try {
            File programRootDir = new File(path);
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
            add.setAccessible(true);
            add.invoke(classLoader, programRootDir.toURI().toURL());
            System.out.println("add ["+path+"] to classpath success!");
        }catch (Exception e){
            System.out.println("add ["+path+"] to classpath fail!");
            throw e;
        }
    }

    /**
     * Create user info table
     */
    public void testCreateTable() {
        LOG.info("Entering testCreateTable.");

        // Specify the table descriptor.
        HTableDescriptor htd = new HTableDescriptor(tableName);

        // Set the column family name to info.
        HColumnDescriptor hcd = new HColumnDescriptor("info");

        // Set data encoding methods，HBase provides DIFF,FAST_DIFF,PREFIX
        // and PREFIX_TREE
        hcd.setDataBlockEncoding(DataBlockEncoding.FAST_DIFF);

        // Set compression methods, HBase provides two default compression
        // methods:GZ and SNAPPY
        // GZ has the highest compression rate,but low compression and
        // decompression effeciency,fit for cold data
        // SNAPPY has low compression rate, but high compression and
        // decompression effeciency,fit for hot data.
        // it is advised to use SANPPY
        hcd.setCompressionType(Compression.Algorithm.SNAPPY);

        htd.addFamily(hcd);

        Admin admin = null;
        try {
            // Instantiate an Admin object.
            admin = conn.getAdmin();
            if (!admin.tableExists(tableName)) {
                LOG.info("Creating table...");
                admin.createTable(htd);
                LOG.info(admin.getClusterStatus());
                LOG.info(admin.listNamespaceDescriptors());
                LOG.info("Table created successfully.");
            } else {
                LOG.warn("table already exists");
            }
        } catch (IOException e) {
            LOG.error("Create table failed.");
        } finally {
            if (admin != null) {
                try {
                    // Close the Admin object.
                    admin.close();
                } catch (IOException e) {
                    LOG.error("Failed to close admin ", e);
                }
            }
        }
        LOG.info("Exiting testCreateTable.");
    }

    public void testMultiSplit() {
        LOG.info("Entering testMultiSplit.");

        Table table = null;
        Admin admin = null;
        try {
            admin = conn.getAdmin();

            // initilize a HTable object
            table = conn.getTable(tableName);
            Set<HRegionInfo> regionSet = new HashSet<HRegionInfo>();
            List<HRegionLocation> regionList = conn.getRegionLocator(tableName).getAllRegionLocations();
            for(HRegionLocation hrl : regionList){
                regionSet.add(hrl.getRegionInfo());
            }
            byte[][] sk = new byte[4][];
            sk[0] = "A".getBytes();
            sk[1] = "D".getBytes();
            sk[2] = "F".getBytes();
            sk[3] = "H".getBytes();
            for (HRegionInfo regionInfo : regionSet) {
                ((HBaseAdmin)admin).multiSplit(regionInfo.getRegionName(), sk);
            }
            LOG.info("MultiSplit successfully.");
        } catch (Exception e) {
            LOG.error("MultiSplit failed ", e);
        } finally {
            if (table != null) {
                try {
                    // Close table object
                    table.close();
                } catch (IOException e) {
                    LOG.error("Close table failed ", e);
                }
            }
            if (admin != null) {
                try {
                    // Close the Admin object.
                    admin.close();
                } catch (IOException e) {
                    LOG.error("Close admin failed ", e);
                }
            }
        }
        LOG.info("Exiting testMultiSplit.");
    }

    /**
     * Insert data
     */
    public void testPut() {
        LOG.info("Entering testPut.");

        // Specify the column family name.
        byte[] familyName = Bytes.toBytes("info");
        // Specify the column name.
        byte[][] qualifiers = { Bytes.toBytes("name"), Bytes.toBytes("gender"),
                Bytes.toBytes("age"), Bytes.toBytes("address") };

        Table table = null;
        try {
            // Instantiate an HTable object.
            table = conn.getTable(tableName);
            List<Put> puts = new ArrayList<Put>();
            // Instantiate a Put object.
            Put put = new Put(Bytes.toBytes("012005000201"));
            put.addColumn(familyName, qualifiers[0], Bytes.toBytes("Zhang San"));
            put.addColumn(familyName, qualifiers[1], Bytes.toBytes("Male"));
            put.addColumn(familyName, qualifiers[2], Bytes.toBytes("19"));
            put.addColumn(familyName, qualifiers[3], Bytes.toBytes("Shenzhen, Guangdong"));
            puts.add(put);

            put = new Put(Bytes.toBytes("012005000202"));
            put.addColumn(familyName, qualifiers[0], Bytes.toBytes("Li Wanting"));
            put.addColumn(familyName, qualifiers[1], Bytes.toBytes("Female"));
            put.addColumn(familyName, qualifiers[2], Bytes.toBytes("23"));
            put.addColumn(familyName, qualifiers[3], Bytes.toBytes("Shijiazhuang, Hebei"));
            puts.add(put);

            put = new Put(Bytes.toBytes("012005000203"));
            put.addColumn(familyName, qualifiers[0], Bytes.toBytes("Wang Ming"));
            put.addColumn(familyName, qualifiers[1], Bytes.toBytes("Male"));
            put.addColumn(familyName, qualifiers[2], Bytes.toBytes("26"));
            put.addColumn(familyName, qualifiers[3], Bytes.toBytes("Ningbo, Zhejiang"));
            puts.add(put);

            put = new Put(Bytes.toBytes("012005000204"));
            put.addColumn(familyName, qualifiers[0], Bytes.toBytes("Li Gang"));
            put.addColumn(familyName, qualifiers[1], Bytes.toBytes("Male"));
            put.addColumn(familyName, qualifiers[2], Bytes.toBytes("18"));
            put.addColumn(familyName, qualifiers[3], Bytes.toBytes("Xiangyang, Hubei"));
            puts.add(put);

            put = new Put(Bytes.toBytes("012005000205"));
            put.addColumn(familyName, qualifiers[0], Bytes.toBytes("Zhao Enru"));
            put.addColumn(familyName, qualifiers[1], Bytes.toBytes("Female"));
            put.addColumn(familyName, qualifiers[2], Bytes.toBytes("21"));
            put.addColumn(familyName, qualifiers[3], Bytes.toBytes("Shangrao, Jiangxi"));
            puts.add(put);

            put = new Put(Bytes.toBytes("012005000206"));
            put.addColumn(familyName, qualifiers[0], Bytes.toBytes("Chen Long"));
            put.addColumn(familyName, qualifiers[1], Bytes.toBytes("Male"));
            put.addColumn(familyName, qualifiers[2], Bytes.toBytes("32"));
            put.addColumn(familyName, qualifiers[3], Bytes.toBytes("Zhuzhou, Hunan"));
            puts.add(put);

            put = new Put(Bytes.toBytes("012005000207"));
            put.addColumn(familyName, qualifiers[0], Bytes.toBytes("Zhou Wei"));
            put.addColumn(familyName, qualifiers[1], Bytes.toBytes("Female"));
            put.addColumn(familyName, qualifiers[2], Bytes.toBytes("29"));
            put.addColumn(familyName, qualifiers[3], Bytes.toBytes("Nanyang, Henan"));
            puts.add(put);

            put = new Put(Bytes.toBytes("012005000208"));
            put.addColumn(familyName, qualifiers[0], Bytes.toBytes("Yang Yiwen"));
            put.addColumn(familyName, qualifiers[1], Bytes.toBytes("Female"));
            put.addColumn(familyName, qualifiers[2], Bytes.toBytes("30"));
            put.addColumn(familyName, qualifiers[3], Bytes.toBytes("Kaixian, Chongqing"));
            puts.add(put);

            put = new Put(Bytes.toBytes("012005000209"));
            put.addColumn(familyName, qualifiers[0], Bytes.toBytes("Xu Bing"));
            put.addColumn(familyName, qualifiers[1], Bytes.toBytes("Male"));
            put.addColumn(familyName, qualifiers[2], Bytes.toBytes("26"));
            put.addColumn(familyName, qualifiers[3], Bytes.toBytes("Weinan, Shaanxi"));
            puts.add(put);

            put = new Put(Bytes.toBytes("012005000210"));
            put.addColumn(familyName, qualifiers[0], Bytes.toBytes("Xiao Kai"));
            put.addColumn(familyName, qualifiers[1], Bytes.toBytes("Male"));
            put.addColumn(familyName, qualifiers[2], Bytes.toBytes("25"));
            put.addColumn(familyName, qualifiers[3], Bytes.toBytes("Dalian, Liaoning"));
            puts.add(put);

            // Submit a put request.
            table.put(puts);

            LOG.info("Put successfully.");
        } catch (IOException e) {
            LOG.error("Put failed ", e);
        } finally {
            if (table != null) {
                try {
                    // Close the HTable object.
                    table.close();
                } catch (IOException e) {
                    LOG.error("Close table failed ", e);
                }
            }
        }
        LOG.info("Exiting testPut.");
    }

    public void createIndex() {
        LOG.info("Entering createIndex.");

        String indexName = "index_name";

        // Create index instance
        IndexSpecification iSpec = new IndexSpecification(indexName);

        iSpec.addIndexColumn(new HColumnDescriptor("info"), "name", ValueType.String, 100);

        IndexAdmin iAdmin = null;
        Admin admin = null;
        try {
            // Instantiate IndexAdmin Object
            iAdmin = new IndexAdmin(conf);

            // Create Secondary Index
            iAdmin.addIndex(tableName, iSpec);

            admin = conn.getAdmin();

            //Specify the encryption type of indexed column
            HTableDescriptor htd = admin.getTableDescriptor(tableName);
            admin.disableTable(tableName);
            htd = admin.getTableDescriptor(tableName);
            //Instantiate index column description.
            HColumnDescriptor indexColDesc = new HColumnDescriptor(
                    IndexMasterObserver.DEFAULT_INDEX_COL_DESC);

            //Set the description of index as the HTable description.
            htd.setValue(Constants.INDEX_COL_DESC_BYTES,
                    indexColDesc.toByteArray());
            admin.modifyTable(tableName, htd);
            admin.enableTable(tableName);

            LOG.info("Create index successfully.");

        } catch (IOException e) {
            LOG.error("Create index failed.");
        } finally {
            if (admin != null) {
                try {
                    admin.close();
                } catch (IOException e) {
                    LOG.error("Close admin failed ", e);
                }
            }
            if (iAdmin != null) {
                try {
                    // Close IndexAdmin Object
                    iAdmin.close();
                } catch (IOException e) {
                    LOG.error("Close admin failed ", e);
                }
            }
        }
        LOG.info("Exiting createIndex.");
    }

    /**
     * Scan data by secondary index.
     */
    public void testScanDataByIndex() {
        LOG.info("Entering testScanDataByIndex.");

        Table table = null;
        ResultScanner scanner = null;
        try {
            table = conn.getTable(tableName);

            // Create a filter for indexed column.
            Filter filter = new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("name"),
                    CompareOp.EQUAL, "Li Gang".getBytes());
            Scan scan = new Scan();
            scan.setFilter(filter);
            scanner = table.getScanner(scan);
            LOG.info("Scan indexed data.");

            for (Result result : scanner) {
                for (Cell cell : result.rawCells()) {
                    LOG.info(Bytes.toString(CellUtil.cloneRow(cell)) + ":"
                            + Bytes.toString(CellUtil.cloneFamily(cell)) + ","
                            + Bytes.toString(CellUtil.cloneQualifier(cell)) + ","
                            + Bytes.toString(CellUtil.cloneValue(cell)));
                }
            }
            LOG.info("Scan data by index successfully.");
        } catch (IOException e) {
            LOG.error("Scan data by index failed ", e);
        } finally {
            if (scanner != null) {
                // Close the scanner object.
                scanner.close();
            }
            try {
                if (table != null) {
                    table.close();
                }
            } catch (IOException e) {
                LOG.error("Close table failed ", e);
            }
        }

        LOG.info("Exiting testScanDataByIndex.");
    }

    /**
     * Modify a Table
     */
    public void testModifyTable() {
        LOG.info("Entering testModifyTable.");

        // Specify the column family name.
        byte[] familyName = Bytes.toBytes("education");

        Admin admin = null;
        try {
            // Instantiate an Admin object.
            admin = conn.getAdmin();

            // Obtain the table descriptor.
            HTableDescriptor htd = admin.getTableDescriptor(tableName);

            // Check whether the column family is specified before modification.
            if (!htd.hasFamily(familyName)) {
                // Create the column descriptor.
                HColumnDescriptor hcd = new HColumnDescriptor(familyName);
                htd.addFamily(hcd);

                // Disable the table to get the table offline before modifying
                // the table.
                admin.disableTable(tableName);
                // Submit a modifyTable request.
                admin.modifyTable(tableName, htd);
                // Enable the table to get the table online after modifying the
                // table.
                admin.enableTable(tableName);
            }
            LOG.info("Modify table successfully.");
        } catch (IOException e) {
            LOG.error("Modify table failed ", e);
        } finally {
            if (admin != null) {
                try {
                    // Close the Admin object.
                    admin.close();
                } catch (IOException e) {
                    LOG.error("Close admin failed ", e);
                }
            }
        }
        LOG.info("Exiting testModifyTable.");
    }

    /**
     * Get Data
     */
    public void testGet() {
        LOG.info("Entering testGet.");

        // Specify the column family name.
        byte[] familyName = Bytes.toBytes("info");
        // Specify the column name.
        byte[][] qualifier = { Bytes.toBytes("name"), Bytes.toBytes("address") };
        // Specify RowKey.
        byte[] rowKey = Bytes.toBytes("012005000201");

        Table table = null;
        try {
            // Create the Configuration instance.
            table = conn.getTable(tableName);

            // Instantiate a Get object.
            Get get = new Get(rowKey);

            // Set the column family name and column name.
            get.addColumn(familyName, qualifier[0]);
            get.addColumn(familyName, qualifier[1]);

            // Submit a get request.
            Result result = table.get(get);

            // Print query results.
            for (Cell cell : result.rawCells()) {
                LOG.info(Bytes.toString(CellUtil.cloneRow(cell)) + ":"
                        + Bytes.toString(CellUtil.cloneFamily(cell)) + ","
                        + Bytes.toString(CellUtil.cloneQualifier(cell)) + ","
                        + Bytes.toString(CellUtil.cloneValue(cell)));
            }
            LOG.info("Get data successfully.");
        } catch (IOException e) {
            LOG.error("Get data failed ", e);
        } finally {
            if (table != null) {
                try {
                    // Close the HTable object.
                    table.close();
                } catch (IOException e) {
                    LOG.error("Close table failed ", e);
                }
            }
        }
        LOG.info("Exiting testGet.");
    }

    public void testScanData() {
        LOG.info("Entering testScanData.");

        Table table = null;
        // Instantiate a ResultScanner object.
        ResultScanner rScanner = null;
        try {
            // Create the Configuration instance.
            table = conn.getTable(tableName);

            // Instantiate a Get object.
            Scan scan = new Scan();
            scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"));

            // Set the cache size.
            scan.setCaching(5000);
            scan.setBatch(2);

            // Submit a scan request.
            rScanner = table.getScanner(scan);

            // Print query results.
            for (Result r = rScanner.next(); r != null; r = rScanner.next()) {
                for (Cell cell : r.rawCells()) {
                    LOG.info(Bytes.toString(CellUtil.cloneRow(cell)) + ":"
                            + Bytes.toString(CellUtil.cloneFamily(cell)) + ","
                            + Bytes.toString(CellUtil.cloneQualifier(cell)) + ","
                            + Bytes.toString(CellUtil.cloneValue(cell)));
                }
            }
            LOG.info("Scan data successfully.");
        } catch (IOException e) {
            LOG.error("Scan data failed ", e);
        } finally {
            if (rScanner != null) {
                // Close the scanner object.
                rScanner.close();
            }
            if (table != null) {
                try {
                    // Close the HTable object.
                    table.close();
                } catch (IOException e) {
                    LOG.error("Close table failed ", e);
                }
            }
        }
        LOG.info("Exiting testScanData.");
    }

    public void testSingleColumnValueFilter() {
        LOG.info("Entering testSingleColumnValueFilter.");

        Table table = null;

        // Instantiate a ResultScanner object.
        ResultScanner rScanner = null;

        try {
            // Create the Configuration instance.
            table = conn.getTable(tableName);

            // Instantiate a Get object.
            Scan scan = new Scan();
            scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"));

            // Set the filter criteria.
            SingleColumnValueFilter filter = new SingleColumnValueFilter(
                    Bytes.toBytes("info"), Bytes.toBytes("name"), CompareOp.EQUAL,
                    Bytes.toBytes("Xu Bing"));

            scan.setFilter(filter);

            // Submit a scan request.
            rScanner = table.getScanner(scan);

            // Print query results.
            for (Result r = rScanner.next(); r != null; r = rScanner.next()) {
                for (Cell cell : r.rawCells()) {
                    LOG.info(Bytes.toString(CellUtil.cloneRow(cell)) + ":"
                            + Bytes.toString(CellUtil.cloneFamily(cell)) + ","
                            + Bytes.toString(CellUtil.cloneQualifier(cell)) + ","
                            + Bytes.toString(CellUtil.cloneValue(cell)));
                }
            }
            LOG.info("Single column value filter successfully.");
        } catch (IOException e) {
            LOG.error("Single column value filter failed ", e);
        } finally {
            if (rScanner != null) {
                // Close the scanner object.
                rScanner.close();
            }
            if (table != null) {
                try {
                    // Close the HTable object.
                    table.close();
                } catch (IOException e) {
                    LOG.error("Close table failed ", e);
                }
            }
        }
        LOG.info("Exiting testSingleColumnValueFilter.");
    }

    public void testFilterList() {
        LOG.info("Entering testFilterList.");

        Table table = null;

        // Instantiate a ResultScanner object.
        ResultScanner rScanner = null;

        try {
            // Create the Configuration instance.
            table = conn.getTable(tableName);

            // Instantiate a Get object.
            Scan scan = new Scan();
            scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"));

            // Instantiate a FilterList object in which filters have "and"
            // relationship with each other.
            FilterList list = new FilterList(Operator.MUST_PASS_ALL);
            // Obtain data with age of greater than or equal to 20.
            list.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes
                    .toBytes("age"), CompareOp.GREATER_OR_EQUAL, Bytes.toBytes(new Long(
                    20))));
            // Obtain data with age of less than or equal to 29.
            list.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes
                    .toBytes("age"), CompareOp.LESS_OR_EQUAL, Bytes.toBytes(new Long(29))));

            scan.setFilter(list);


            // Submit a scan request.
            rScanner = table.getScanner(scan);
            // Print query results.
            for (Result r = rScanner.next(); r != null; r = rScanner.next()) {
                for (Cell cell : r.rawCells()) {
                    LOG.info(Bytes.toString(CellUtil.cloneRow(cell)) + ":"
                            + Bytes.toString(CellUtil.cloneFamily(cell)) + ","
                            + Bytes.toString(CellUtil.cloneQualifier(cell)) + ","
                            + Bytes.toString(CellUtil.cloneValue(cell)));
                }
            }
            LOG.info("Filter list successfully.");
        } catch (IOException e) {
            LOG.error("Filter list failed ", e);
        } finally {
            if (rScanner != null) {
                // Close the scanner object.
                rScanner.close();
            }
            if (table != null) {
                try {
                    // Close the HTable object.
                    table.close();
                } catch (IOException e) {
                    LOG.error("Close table failed ", e);
                }
            }
        }
        LOG.info("Exiting testFilterList.");
    }

    /**
     * deleting data
     */
    public void testDelete() {
        LOG.info("Entering testDelete.");

        byte[] rowKey = Bytes.toBytes("012005000201");

        Table table = null;
        try {
            // Instantiate an HTable object.
            table = conn.getTable(tableName);

            // Instantiate an Delete object.
            Delete delete = new Delete(rowKey);

            // Submit a delete request.
            table.delete(delete);

            LOG.info("Delete table successfully.");
        } catch (IOException e) {
            LOG.error("Delete table failed ", e);
        } finally {
            if (table != null) {
                try {
                    // Close the HTable object.
                    table.close();
                } catch (IOException e) {
                    LOG.error("Close table failed ", e);
                }
            }
        }
        LOG.info("Exiting testDelete.");
    }

    public void dropIndex() {
        LOG.info("Entering dropIndex.");

        String indexName = "index_name";

        IndexAdmin iAdmin = null;
        try {
            // Instantiate IndexAdmin Object
            iAdmin = new IndexAdmin(conf);

            // Delete Secondary Index
            iAdmin.dropIndex(tableName, indexName);

            LOG.info("Drop index successfully.");
        } catch (IOException e) {
            LOG.error("Drop index failed ", e);
        } finally {
            if (iAdmin != null) {
                try {
                    // Close Secondary Index
                    iAdmin.close();
                } catch (IOException e) {
                    LOG.error("Close admin failed ", e);
                }
            }
        }
        LOG.info("Exiting dropIndex.");
    }

    /**
     * Delete user table
     */
    public void dropTable() {
        LOG.info("Entering dropTable.");

        Admin admin = null;
        try {
            admin = conn.getAdmin();
            if (admin.tableExists(tableName)) {
                // Disable the table before deleting it.
                admin.disableTable(tableName);

                // Delete table.
                admin.deleteTable(tableName);
            }
            LOG.info("Drop table successfully.");
        } catch (IOException e) {
            LOG.error("Drop table failed ", e);
        } finally {
            if (admin != null) {
                try {
                    // Close the Admin object.
                    admin.close();
                } catch (IOException e) {
                    LOG.error("Close admin failed ", e);
                }
            }
        }
        LOG.info("Exiting dropTable.");
    }

    public void grantACL() {
        LOG.info("Entering grantACL.");

        String user = "huawei";
        String permissions = "RW";

        String familyName = "info";
        String qualifierName = "name";

        Table mt = null;
        Admin hAdmin = null;
        try {
            // Create ACL Instance
            mt = conn.getTable(AccessControlLists.ACL_TABLE_NAME);

            CoprocessorRpcChannel service = mt
                    .coprocessorService(HConstants.EMPTY_START_ROW);
            AccessControlProtos.AccessControlService.BlockingInterface protocol = AccessControlProtos.AccessControlService
                    .newBlockingStub(service);
            Permission perm = new Permission(Bytes.toBytes(permissions));

            hAdmin = conn.getAdmin();
            HTableDescriptor ht = hAdmin.getTableDescriptor(tableName);

            // Judge whether the table exists
            if (hAdmin.tableExists(mt.getName())) {
                // Judge whether ColumnFamily exists
                if (ht.hasFamily(Bytes.toBytes(familyName))) {
                    // grant permission
                    ProtobufUtil.grant(null,protocol, user, tableName,
                            Bytes.toBytes(familyName),
                            (qualifierName == null ? null : Bytes.toBytes(qualifierName)),
                            perm.getActions());
                } else {
                    // grant permission
                    ProtobufUtil.grant(null,protocol, user, tableName,
                            null, null, perm.getActions());
                }
            }
            LOG.info("Grant ACL successfully.");
        } catch (Exception e) {
            LOG.error("Grant ACL failed ", e);
        } finally {
            if (mt != null) {
                try {
                    // Close
                    mt.close();
                } catch (IOException e) {
                    LOG.error("Close table failed ", e);
                }
            }

            if (hAdmin != null) {
                try {
                    // Close Admin Object
                    hAdmin.close();
                } catch (IOException e) {
                    LOG.error("Close admin failed ", e);
                }
            }
        }
        LOG.info("Exiting grantACL.");
    }

    public void testMOBDataRead() {
        LOG.info("Entering testMOBDataRead.");
        ResultScanner scanner = null;
        Table table = null;
        Admin admin = null;
        try {

            // get table object representing table tableName
            table = conn.getTable(tableName);
            admin = conn.getAdmin();
            admin.flush(table.getName());
            Scan scan = new Scan();
            // get table scanner
            scanner = table.getScanner(scan);
            for (Result result : scanner) {
                byte[] value = result.getValue(Bytes.toBytes("mobcf"),
                        Bytes.toBytes("cf1"));
                String string = Bytes.toString(value);
                LOG.info("value:" + string);
            }
            LOG.info("MOB data read successfully.");
        } catch (Exception e) {
            LOG.error("MOB data read failed ", e);
        } finally {
            if(scanner != null){
                scanner.close();
            }
            if (table != null) {
                try {
                    // Close table object
                    table.close();
                } catch (IOException e) {
                    LOG.error("Close table failed ", e);
                }
            }
            if (admin != null) {
                try {
                    // Close the Admin object.
                    admin.close();
                } catch (IOException e) {
                    LOG.error("Close admin failed ", e);
                }
            }
        }
        LOG.info("Exiting testMOBDataRead.");
    }

    public void testMOBDataInsertion() {
        LOG.info("Entering testMOBDataInsertion.");

        Table table = null;
        try {
            // set row name to "row"
            Put p = new Put(Bytes.toBytes("row"));
            byte[] value = new byte[1000];
            // set the column value of column family mobcf with the value of "cf1"
            p.addColumn(Bytes.toBytes("mobcf"), Bytes.toBytes("cf1"), value);
            // get the table object represent table tableName
            table = conn.getTable(tableName);
            // put data
            table.put(p);
            LOG.info("MOB data inserted successfully.");

        } catch (Exception e) {
            LOG.error("MOB data inserted failed ", e);
        } finally {
            if(table != null){
                try {
                    table.close();
                } catch (Exception e1) {
                    LOG.error("Close table failed ", e1);
                }
            }
        }
        LOG.info("Exiting testMOBDataInsertion.");
    }

    public void testCreateMOBTable() {
        LOG.info("Entering testCreateMOBTable.");

        Admin admin = null;
        try {
            // Create Admin instance
            admin = conn.getAdmin();
            HTableDescriptor tabDescriptor = new HTableDescriptor(tableName);
            HColumnDescriptor mob = new HColumnDescriptor("mobcf");
            // Open mob function
            mob.setMobEnabled(true);
            // Set mob threshold
            mob.setMobThreshold(10L);
            tabDescriptor.addFamily(mob);
            admin.createTable(tabDescriptor);
            LOG.info("MOB Table is created successfully.");

        } catch (Exception e) {
            LOG.error("MOB Table is created failed ", e);
        } finally {
            if (admin != null) {
                try {
                    // Close the Admin object.
                    admin.close();
                } catch (IOException e) {
                    LOG.error("Close admin failed ", e);
                }
            }
        }
        LOG.info("Exiting testCreateMOBTable.");
    }

    public static void main(String[] args) {
        TestSample ts = new TestSample();
        try {
            ts.testSample();
        } catch (Exception e1) {
            System.out.println("There exists some wrong.");
        }
    }
}
