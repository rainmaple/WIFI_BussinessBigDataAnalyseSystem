package dao;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class Dao {
	static Configuration cfg = HBaseConfiguration.create();

	static {
		System.out.println(cfg.get("hbase.master"));
	}

	public static boolean isExists(String tableName)
			throws MasterNotRunningException , ZooKeeperConnectionException , IOException  {
		return new HBaseAdmin(cfg).tableExists(tableName);
	}

	public static void deleteTable(String tableName) throws IOException  {
		HBaseAdmin admin = new HBaseAdmin(cfg);
		admin.disableTable(tableName);
		admin.deleteTable(tableName);
		System.out.println(tableName + "is deleted!");
	}

	public static void create(String tableName, String[] columnFamily) throws IOException  {
		HBaseAdmin admin = new HBaseAdmin(cfg);
		if (admin.tableExists(tableName)) {
			System.out.println(tableName + " exists!");
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			for (String str : columnFamily) {
				tableDesc.addFamily(new HColumnDescriptor(str));
			}
			admin.createTable(tableDesc);
			System.out.println(tableName + " create successfully!");
		}
	}

	public static void put(String tablename, String row, String columnFamily, String column, String data)
			throws IOException  {
		HTable table = new HTable(cfg, tablename);
		Put put = new Put(Bytes.toBytes(row));
		put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(data));
		table.put(put);
	}

	public static void get(String tablename, String row) throws IOException  {
		HTable table = new HTable(cfg, tablename);
		Get get = new Get(Bytes.toBytes(row));
		Result result = table.get(get);
		System.out.println("Get: " + result);
	}

	public static ResultScanner scan(String tableName) throws IOException  {
		String str = "";
		HTable table = new HTable(cfg, tableName);
		Scan s = new Scan();
		ResultScanner rs = table.getScanner(s);
		return rs;
	}
	
	public static void deleteAllColumn(String tableName, String rowKey)
            throws IOException  {
        HTable table = new HTable(cfg, Bytes.toBytes(tableName));
        Delete deleteAll = new Delete(Bytes.toBytes(rowKey));
        table.delete(deleteAll);
    }
	
	public static void clear(String tablename) throws IOException {
		ResultScanner rs=scan(tablename);
		for(Result r:rs){
			deleteAllColumn(tablename, new String(r.getRow()));
		}
		System.out.println(tablename+"is clear");
	}

}
