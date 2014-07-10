package org.apache.hadoop.hbase.coprocessor.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class CreateTable {
  
  public static void main(String[] args) {
    HTable ht = null;
    Configuration conf = HBaseConfiguration.create();
    HBaseAdmin hbadmin = null;
    try {
      hbadmin = new HBaseAdmin(conf);
      if(!hbadmin.isTableAvailable("table_example")) {
        TableName tablename = TableName.valueOf("table_example");
  
        HTableDescriptor desc = new HTableDescriptor(tablename);
        desc.addFamily(new HColumnDescriptor("cf1"));
        hbadmin.createTable(desc);
      }
      ht = new HTable(conf, "table_example");
    } catch(Exception e) {
      e.printStackTrace();
    }
    // Now we can operate on HTable ht
    Put put = new Put(Bytes.toBytes("Value to enter"));
    put.add(Bytes.toBytes("cf1"), Bytes.toBytes("newQualifier"), Bytes.toBytes("NewValue"));
    try {
      ht.put(put);    
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
