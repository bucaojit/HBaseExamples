HBaseExamples
==============

Some HBase code examples I've written while working on HBase, for use as a starting point for more involved work.

To compile the examples, the JAR's located in the hbase/lib directory need to be added to the CLASSPATH before
compilation.

Included a few useful coprocessor examples, one is for an Observer coprocessor, and the more complex
Endpoint coprocessor example.

EndpointCoprocessor includes a coprocessor used for newer versions of HBase that require the use of Google Protocol
Buffers to be compiled and used as the serialization medium.  It is based on the 0.98 version of HBase, where it does
not seem to have a full end-to-end protobuf coprocessor example.
