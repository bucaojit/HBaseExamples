/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package EndpointCoprocessor;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.coprocessor.BaseMasterObserver;
import org.apache.hadoop.hbase.coprocessor.MasterCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.ipc.BlockingRpcCallback;
import org.apache.hadoop.hbase.ipc.CoprocessorRpcChannel;
import org.apache.hadoop.hbase.ipc.ServerRpcController;
import org.apache.hadoop.hbase.util.Bytes;

import com.google.protobuf.ByteString;

/*
 *   The HTable javadocs has an incorrect example implementation:
 *   http://archive.cloudera.com/cdh5/cdh/5/hbase/apidocs/org/apache/hadoop/hbase/client/HTable.html#coprocessorService(byte[])
 *  
 *   The below example shows a correct way of making a single region coprocessor 
 *   call 
 *  
 *   CoprocessorRpcChannel channel = myTable.coprocessorService(rowkey);
 *   MyService.BlockingInterface service = MyService.newBlockingStub(channel);
 *   MyCallRequest request = MyCallRequest.newBuilder()
 *     ...
 *   .build();
 *   MyCallResponse response = service.myCall(null, request);
 */

public class SingleRegionCall extends BaseMasterObserver {
	private static final Log LOG = LogFactory.getLog(SingleRegionCall.class.getName());
    private HTable table;
            
    @Override
    public void preMove(ObserverContext<MasterCoprocessorEnvironment> ctx, 
                        HRegionInfo region, 
                        ServerName srcServer, 
                        ServerName destServer) {
    	if (LOG.isTraceEnabled()) LOG.trace("preMove -- ENTRY -- region: " + region.toString());    	    	    	

		table = new HTable(ctx.getEnvironment().getConfiguration(), region.getTable());

    	CoprocessorRpcChannel channel = table.coprocessorService(region.getStartKey());
    	
    	// MyRegionService is your service specified in the '.proto' file
    	// 
    	// Lines starting with 'generated' will be from the generated package
    	// that the proto compiler will generate
    	
    	MyRegionService.BlockingInterface myservice = MyRegionService.newBlockingStub(channel);
    	generated.RegionProtos.MyRequest.Builder 
    	    requestBuilder = MyRequest.newBuilder();
    	
    	// My request has no values to send to server and to build, 
    	// an example of setting a value would be something like below, 
    	// set before build() call:
    	
    	// request.setValue(5);
    	
    	generated.RegionProtos.MyRequest 
    	    request = requestBuilder.build();
    	try {
    		MyResponse response = myservice.myMethod(null, request);
    			
    	} catch(Exception e) {
    		LOG.error("Problem calling coprocessor: " + e);
    	}    	    	
    	if (LOG.isTraceEnabled()) LOG.trace("preMove -- EXIT -- region: " + region.toString());    
    }
}



