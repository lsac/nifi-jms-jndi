/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.jms.cf;

import javax.jms.ConnectionFactory;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.processor.util.StandardValidators;
import org.apache.nifi.ssl.SSLContextService;

/**
 * Defines a strategy to create implementations to load and initialize third
 * party implementations of the {@link ConnectionFactory}
 */
public interface JNDIConnectionFactoryProviderDefinition extends JMSConnectionFactoryProviderDefinition {

    static final String CF_NAME = "cfname";
    static final String JNDI__CONNECTION_FACTORY_IMP = "JNDI ConnectionFactory Implementation";
    static final String JNDI__CONNECTION_FACTORY = "JNDI ConnectionFactory";

    static final PropertyDescriptor CONNECTION_FACTORY_IMPL = new PropertyDescriptor.Builder()
            .name(CF_IMPL)
            .displayName(JNDI__CONNECTION_FACTORY_IMP)
            .description("A fully qualified name of the JNDI ConnectionFactory implementation "
                    + "class (i.e., com.solacesystems.jndi.SolJNDIInitialContextFactory)")
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .required(true)
            .expressionLanguageSupported(true)
            .build();
    static final PropertyDescriptor CLIENT_LIB_DIR_PATH = new PropertyDescriptor.Builder()
            .name(CF_LIB)
            .displayName("Solace Client Libraries path (i.e., /usr/jms/lib)")
            .description("Path to the directory with additional resources (i.e., JARs, configuration files etc.) to be added "
                    + "to the classpath. Such resources typically represent target MQ client libraries for the "
                    + "ConnectionFactory implementation.")
            .addValidator(new ClientLibValidator())
            .required(true)
            .expressionLanguageSupported(true)
            .build();

    // ConnectionFactory specific properties
    static final PropertyDescriptor BROKER_URI = new PropertyDescriptor.Builder()
            .name(BROKER)
            .displayName("Broker URI")
            .description("URI pointing to the network location of the JMS Message broker. For example, "
                    + "'myhost' for Solace")
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .required(true)
            .expressionLanguageSupported(true)
            .build();

    static final PropertyDescriptor CF_LOOKUP = new PropertyDescriptor.Builder()
            .name(CF_NAME)
            .displayName("Connection Factory Lookup Name")
            .description("Look up the connection factory object in the JNDI object store.")
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .required(true)
            .expressionLanguageSupported(false)
            .build();

    static final PropertyDescriptor SSL_CONTEXT_SERVICE = new PropertyDescriptor.Builder()
            .name("SSL Context Service")
            .description("The SSL Context Service used to provide client certificate information for TLS/SSL connections.")
            .required(false)
            .identifiesControllerService(SSLContextService.class)
            .build();
    static final PropertyDescriptor USER = new PropertyDescriptor.Builder()
            .name("User Name")
            .description("User Name used for authentication and authorization. (i.e., clientname@solaceVpn)")
            .required(false)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();
    static final PropertyDescriptor PASSWORD = new PropertyDescriptor.Builder()
            .name("Password")
            .description("Password used for authentication and authorization.")
            .required(false)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .sensitive(true)
            .build();

}
