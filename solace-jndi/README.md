# Solace JMS Project

The Solace JMS API supports JMS version 1.1. The JMS API at [Solace Dev Portal](http://dev.solace.com/tech/jms-api/) has a series of tutorials and articles to help developers to learn.

This sample project is a conslidated version of 2 samples at [Obtaining JMS objects using JNDI](http://dev.solace.com/get-started/jms-tutorials/obtaining-jms-objects-using-jndi/). The project publishes persistent messages to queue "toNifi" and receives from queue "fromNifi". Readers are encouraged to write additional code to receive from topic "T/fromNifi".

Gradle is used to build the project.
```
$ ./gradlew build
Starting a Gradle Daemon (subsequent builds will be faster)
:compileJava
:processResources NO-SOURCE
:classes
:jar
:startScripts
:distTar
:distZip
:assemble
:compileTestJava NO-SOURCE
:processTestResources NO-SOURCE
:testClasses UP-TO-DATE
:test NO-SOURCE
:check UP-TO-DATE
:build

BUILD SUCCESSFUL

Total time: 7.72 secs
```

A custom task is added build.gradle. The **args** points Solace router msg_backbone_ip:port. 
```groovy
task(runQueuePubSubJNDI, dependsOn: 'classes', type: JavaExec) {
    main = 'com.solace.sample.QueuePubSubJNDI'
    classpath = sourceSets.main.runtimeClasspath
    args '192.168.56.101'
}

```

After setting up both Solace router and NiFi, the project runs and generates logs as below.
```
$ ./gradlew runQueuePubSubJNDI
Starting a Gradle Daemon (subsequent builds will be faster)
:compileJava UP-TO-DATE
:processResources NO-SOURCE
:classes UP-TO-DATE
:runQueuePubSubJNDI
QueuePubSubJNDI initializing...
May 13, 2017 1:37:42 PM com.solacesystems.jcsmp.protocol.impl.TcpClientChannel call
INFO: Connecting to host 'orig=tcp://192.168.56.101, scheme=tcp://, host=192.168.56.101' (host 1 of 1, smfclient 2, attempt 1 of 1, this_host_attempt: 1 of 1)
May 13, 2017 1:37:42 PM com.solacesystems.jcsmp.protocol.impl.TcpClientChannel call
INFO: Connected to host 'orig=tcp://192.168.56.101, scheme=tcp://, host=192.168.56.101' (smfclient 2)
May 13, 2017 1:37:42 PM com.solacesystems.jcsmp.protocol.impl.TcpClientChannel close
INFO: Channel Closed (smfclient 2)
May 13, 2017 1:37:42 PM com.solacesystems.jcsmp.protocol.impl.TcpClientChannel call
INFO: Connecting to host 'orig=tcp://192.168.56.101, scheme=tcp://, host=192.168.56.101' (host 1 of 1, smfclient 4, attempt 1 of 1, this_host_attempt: 1 of 1)
May 13, 2017 1:37:42 PM com.solacesystems.jcsmp.protocol.impl.TcpClientChannel call
INFO: Connected to host 'orig=tcp://192.168.56.101, scheme=tcp://, host=192.168.56.101' (smfclient 4)
May 13, 2017 1:37:42 PM com.solacesystems.jcsmp.protocol.impl.TcpClientChannel call
INFO: Connecting to host 'orig=tcp://192.168.56.101, scheme=tcp://, host=192.168.56.101' (host 1 of 1, smfclient 6, attempt 1 of 1, this_host_attempt: 1 of 1)
May 13, 2017 1:37:42 PM com.solacesystems.jcsmp.protocol.impl.TcpClientChannel call
INFO: Connected to host 'orig=tcp://192.168.56.101, scheme=tcp://, host=192.168.56.101' (smfclient 6)
May 13, 2017 1:37:42 PM com.solacesystems.jcsmp.protocol.impl.TcpClientChannel close
INFO: Channel Closed (smfclient 6)
May 13, 2017 1:37:42 PM com.solacesystems.jcsmp.protocol.impl.TcpClientChannel call
INFO: Connecting to host 'orig=tcp://192.168.56.101, scheme=tcp://, host=192.168.56.101' (host 1 of 1, smfclient 8, attempt 1 of 1, this_host_attempt: 1 of 1)
May 13, 2017 1:37:42 PM com.solacesystems.jcsmp.protocol.impl.TcpClientChannel call
INFO: Connected to host 'orig=tcp://192.168.56.101, scheme=tcp://, host=192.168.56.101' (smfclient 8)
May 13, 2017 1:37:42 PM com.solacesystems.jcsmp.protocol.impl.TcpClientChannel close
INFO: Channel Closed (smfclient 8)
May 13, 2017 1:37:42 PM com.solacesystems.jms.SolSession start
Waiting for a message ... (press Ctrl+C) to terminate
INFO: SolSession started.
Connected. About to send message 'Hello world Queues!' to queue 'toNifi'...

...

Message received.
Message Dump:
JMSCorrelationID:                       null
JMSDeliveryMode:                        2
JMSDestination:                         Queue 'fromNifi'
JMSExpiration:                          0
JMSMessageID:                           ID:192.168.4.175f31e15c02e020a00:199
JMSPriority:                            0
JMSTimestamp:                           1494696871023
JMSType:                                null
JMSProperties:                          {JMS_Solace_isXML:null,path:./,filename:109289709822166,JMS_Solace_DeliverToOne:false,appID:99,JMS_Solace_ElidingEligible:false,JMS_Solace_DeadMsgQueueEligible:false,Solace_JMS_Prop_IS_Reply_Message:false,uuid:86319fc0-1733-4827-ad32-ac35450284ac,JMS_Solace_DeliverToOne:false,JMS_Solace_DeadMsgQueueEligible:false,JMS_Solace_ElidingEligible:false,Solace_JMS_Prop_IS_Reply_Message:false}
Destination:                            Queue 'fromNifi'
AppMessageType:                         null
AppMessageID:                           ID:192.168.4.175f31e15c02e020a00:199
CorrelationId:                          null
SendTimestamp:                          1494696871023 (Sat May 13 2017 13:34:31)
Class Of Service:                       USER_COS_1
DeliveryMode:                           PERSISTENT
Message Id:                             6627339
User Property Map:                      9 entries
  Key 'JMS_Solace_isXML' (String): true
  Key 'path' (String): ./
  Key 'filename' (String): 109289709822166
  Key 'JMS_Solace_DeliverToOne' (String): false
  Key 'appID' (String): 99
  Key 'JMS_Solace_ElidingEligible' (String): false
  Key 'JMS_Solace_DeadMsgQueueEligible' (String): false
  Key 'Solace_JMS_Prop_IS_Reply_Message' (String): false
  Key 'uuid' (String): 86319fc0-1733-4827-ad32-ac35450284ac

Binary Attachment:                      len=19
  48 65 6c 6c 6f 20 77 6f    72 6c 64 20 51 75 65 75    Hello.world.Queu
  65 73 21                                              es!


appID = 99, latency = 4 ms
Message sent. Exiting.
May 13, 2017 1:34:31 PM com.solacesystems.jcsmp.protocol.impl.TcpClientChannel close
INFO: Channel Closed (smfclient 4)

BUILD SUCCESSFUL

Total time: 13.018 secs
```
