package org.apache.hadoop.hbase.coprocessor.example;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.coprocessor.example.generated.MyExampleProtos.HelloWorldResponse;
import org.apache.hadoop.hbase.coprocessor.example.generated.MyExampleProtos.HelloWorldRequest;
import org.apache.hadoop.hbase.coprocessor.example.generated.MyExampleProtos.HelloWorldRequest.Builder;
import org.apache.hadoop.hbase.coprocessor.example.generated.MyExampleProtos.HelloWorldService;
import org.apache.hadoop.hbase.ipc.BlockingRpcCallback;
import org.apache.hadoop.hbase.ipc.ServerRpcController;

public class MyHelloWorldClient {
  
  public static void main(String[] args) {
    

    HTable ht = null;
    Configuration conf = HBaseConfiguration.create();    
    try {
      ht = new HTable(conf, "table1");
    } catch(Exception e) {      
    }
    
    Batch.Call<HelloWorldService, HelloWorldResponse> callable = 
        new Batch.Call<HelloWorldService, HelloWorldResponse>() {
      ServerRpcController controller = new ServerRpcController();
      BlockingRpcCallback<HelloWorldResponse> rpcCallback = 
        new BlockingRpcCallback<HelloWorldResponse>();         

      @Override
      public HelloWorldResponse call(HelloWorldService instance) throws IOException {        
        Builder builder = HelloWorldRequest.newBuilder();        
        builder.setMessage("This is the value we are setting for Hello World");
        
        instance.sendHelloWorldRequest(controller, builder.build(), rpcCallback);
        return rpcCallback.get();        
      }
    };
 
      Map<byte[], HelloWorldResponse> result = null;   
      try {
        result = ht.coprocessorService(HelloWorldService.class, null, null, callable);
      } catch (Throwable e) {
        e.printStackTrace();     
      }
      for (HelloWorldResponse response : result.values())
        System.out.println("This is the message: " +response.getMessage());
    }
}

