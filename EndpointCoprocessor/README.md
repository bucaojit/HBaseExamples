BUILDING:

To compile the protobuf, you will need the proto compiler version 2.5.  You can get this by downloading it using:
curl https://protobuf.googlecode.com/files/protobuf-2.5.0.tar.gz > protobuf.tar.gz
Then compiling it and adding the protoc binary to the beginning of your PATH env variable.

A simple way to compile the protobuf is to add it to the HBase src directory, namely:
hbase/hbase-protocol/src/main/protobuf
And then issuing the command to compile using Maven:
mvn compile -Dcompile-protobuf

The generated Java file will be located in:
hbase/hbase-protocol/src/main/java/org/apache/hadoop/hbase/protobuf/generated

Another way to build it would be to call the 'protoc' directly and supplying the correct arguments to it.
The --proto_path will have to be specified to point to the hbase-protocol directory and a --java_out path
is needed to a known directory.

Set the hbase-site.xml to use the coprocessor:
    <property>
        <name>hbase.coprocessor.region.classes</name>
        <value>org.apache.hadoop.hbase.coprocessor.example.MyHelloWorld</value>
    </property>

The example itself simply logs an INFO line on the server side when it is called:
2014-07-08 20:28:59,634 INFO  [RpcServer.handler=5,port=32894] example.MyHelloWorld: This is the Hello World coprocessor, message is: This is the value we are setting for Hello World

The bold part is passed in from the client.  The server then passes a long back to the client, I picked ‘1013’ so on the client side you will see:
This is the message: 1013
