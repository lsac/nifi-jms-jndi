/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.solace.sample;

import com.solacesystems.jms.SolJmsUtility;

import javax.jms.DeliveryMode;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.solacesystems.jms.SupportedProperty;
import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

public class QueuePubSubJNDI {

    public void run(String... args) throws Exception {

        System.out.println("QueuePubSubJNDI initializing...");

        // The client needs to specify all of the following properties:
        Properties env = new Properties();
        env.put(InitialContext.INITIAL_CONTEXT_FACTORY, "com.solacesystems.jndi.SolJNDIInitialContextFactory");
        env.put(InitialContext.PROVIDER_URL, (String) args[0]);
        env.put(SupportedProperty.SOLACE_JMS_VPN, "default");
        env.put(Context.SECURITY_PRINCIPAL, "default");
        env.put(Context.SECURITY_CREDENTIALS, "");

        // InitialContext is used to lookup the JMS administered objects.
        InitialContext initialContext = new InitialContext(env);
        // Lookup ConnectionFactory.
        QueueConnectionFactory cf = (QueueConnectionFactory) initialContext.lookup("/jms/cf/default");
        // JMS Connection
        QueueConnection connection = cf.createQueueConnection();

        // Create a non-transacted, Auto Ack session.
        Session session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

        // Lookup Queue.
        Queue qPub = (Queue) initialContext.lookup("/JNDI/Q/toNifi");

        // Lookup Queue.
        Queue qSub = (Queue) initialContext.lookup("/JNDI/Q/fromNifi");
        // From the session, create a consumer for the destination.
        MessageConsumer consumer = session.createConsumer(qSub);
        /**
         * Anonymous inner-class for receiving messages *
         */
        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {

                try {
                    if (message instanceof TextMessage) {
                        System.out.printf("TextMessage received: '%s'%n", ((TextMessage) message).getText());
                    } else {
                        System.out.println("Message received.");
                    }
                    System.out.printf("Message Dump:%n%s%n", SolJmsUtility.dumpMessage(message));
                    long x = message.getJMSTimestamp();
                    System.out.printf("appID = %d, latency = %d ms \n", message.getLongProperty("appID"), (System.currentTimeMillis() - x));

                } catch (JMSException e) {
                    System.out.println("Error processing incoming message.");
                    e.printStackTrace();
                }

            }
        });
        // Do not forget to start the JMS Connection.
        connection.start();

        // Output a message on the console.
        System.out.println("Waiting for a message ... (press Ctrl+C) to terminate ");
        // From the session, create a producer for the destination.
        // Use the default delivery mode as set in the connection factory
        MessageProducer producer = session.createProducer(qPub);

        // Create a text message.
        TextMessage message = session.createTextMessage("Hello world Queues!");
        message.setBooleanProperty(SupportedProperty.SOLACE_JMS_PROP_DELIVER_TO_ONE, false);

        System.out.printf("Connected. About to send message '%s' to queue '%s'...%n", message.getText(),
                qPub.toString());

        for (int i = 0; i < 100; i++) {
            message.setLongProperty("appID", i);
            producer.send(qPub, message, DeliveryMode.PERSISTENT, Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
            System.out.println("SENT: " + i);
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
            }
        }

        System.out.println("Message sent. Exiting.");

        connection.close();
        initialContext.close();
    }

    public static void main(String... args) throws Exception {

        // Check command line arguments
        if (args.length < 1) {
            System.out.println("Usage: QueuePubSubJNDI <msg_backbone_ip:port>");
            System.exit(-1);
        }

        QueuePubSubJNDI app = new QueuePubSubJNDI();
        app.run(args);
    }
}
