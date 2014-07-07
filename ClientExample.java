package org.apache.hadoop.hbase.coprocessor.example;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.coprocessor.example.generated.BulkDeleteProtos.BulkDeleteRequest;
import org.apache.hadoop.hbase.coprocessor.example.generated.BulkDeleteProtos.BulkDeleteRequest.Builder;
import org.apache.hadoop.hbase.coprocessor.example.generated.BulkDeleteProtos.BulkDeleteRequest.DeleteType;
import org.apache.hadoop.hbase.coprocessor.example.generated.BulkDeleteProtos.BulkDeleteResponse;
import org.apache.hadoop.hbase.coprocessor.example.generated.BulkDeleteProtos.BulkDeleteService;
import org.apache.hadoop.hbase.ipc.BlockingRpcCallback;
import org.apache.hadoop.hbase.ipc.ServerRpcController;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;

public class ClientExample {
  
  public static void main(String[] args) {
    final Scan scan = new Scan();

    HTable ht = null;
    Configuration conf = HBaseConfiguration.create();    
    try {
      ht = new HTable(conf, "table1");
    } catch(Exception e) {      
    }
    long noOfDeletedRows = 0L;
    Batch.Call<BulkDeleteService, BulkDeleteResponse> callable = 
        new Batch.Call<BulkDeleteService, BulkDeleteResponse>() {
      ServerRpcController controller = new ServerRpcController();
      BlockingRpcCallback<BulkDeleteResponse> rpcCallback = 
        new BlockingRpcCallback<BulkDeleteResponse>();
        
  
      public BulkDeleteResponse call(BulkDeleteService service) throws IOException {
        Builder builder = BulkDeleteRequest.newBuilder();
        builder.setScan(ProtobufUtil.toScan(scan));
        builder.setDeleteType(DeleteType.VERSION);
        builder.setRowBatchSize(5);
        service.delete(controller, builder.build(), rpcCallback);
        return rpcCallback.get();
      }
    };    
    try {
      Map<byte[], BulkDeleteResponse> result = null;
      try {      
        result = ht.coprocessorService(BulkDeleteService.class, scan
            .getStartRow(), scan.getStopRow(), callable);
      } catch (Throwable e) {
        e.printStackTrace();
      }
      for (BulkDeleteResponse response : result.values())
        noOfDeletedRows += response.getRowsDeleted();
      System.out.println("no Of deleted rows: " + noOfDeletedRows);
    } catch(Exception e) {}
    
  }
}

