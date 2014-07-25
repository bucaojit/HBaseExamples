package org.apache.hadoop.hbase.coprocessor.example;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.Coprocessor;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.CoprocessorException;
import org.apache.hadoop.hbase.coprocessor.CoprocessorService;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.example.generated.MyExampleProtos.HelloWorldResponse.Builder;
import org.apache.hadoop.hbase.coprocessor.example.generated.MyExampleProtos.HelloWorldRequest;
import org.apache.hadoop.hbase.coprocessor.example.generated.MyExampleProtos.HelloWorldResponse;
import org.apache.hadoop.hbase.coprocessor.example.generated.MyExampleProtos.HelloWorldService;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.Service;


public class MyHelloWorld extends HelloWorldService implements CoprocessorService,
    Coprocessor {
  //private static final String NO_OF_VERSIONS_TO_DELETE = "noOfVersionsToDelete";
  private static final Log LOG = LogFactory.getLog(MyHelloWorld.class);

  private RegionCoprocessorEnvironment env;

  @Override
  public Service getService() {
    return this;
  }

  @Override 
  public void sendHelloWorldRequest(RpcController controller, 
                                              HelloWorldRequest request, 
                                              RpcCallback<HelloWorldResponse> done) {
    LOG.info("This is the Hello World coprocessor, message is: " + request.getMessage());
    
    Builder responseBuilder = HelloWorldResponse.newBuilder();
    responseBuilder.setMessage(1013);
    HelloWorldResponse hwResponse = responseBuilder.build();
    done.run(hwResponse);
  }  

  @Override
  public void start(CoprocessorEnvironment env) throws IOException {
    if (env instanceof RegionCoprocessorEnvironment) {
      this.env = (RegionCoprocessorEnvironment) env;
    } else {
      throw new CoprocessorException("Must be loaded on a table region!");
    }
  }

  @Override
  public void stop(CoprocessorEnvironment env) throws IOException {
    // nothing to do
  }
  
}
