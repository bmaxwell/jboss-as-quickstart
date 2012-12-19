/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.as.quickstarts.helloworld.iiop.client;

import java.util.Hashtable;

import java.rmi.RMISecurityManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.rmi.PortableRemoteObject;

import org.jboss.as.quickstarts.helloworld.iiop.ejb.HelloRemote;
import org.jboss.as.quickstarts.helloworld.iiop.ejb.HelloRemoteHome;

import javax.naming.NamingEnumeration;

import java.util.Properties;

public class JDKClient
{
    public static void main(String[] args) throws Exception
    {
				String host = "localhost";
				String ejbLookupPath = "HelloSessionBean";
				Class remoteHome = HelloRemoteHome.class;

        // Setup security for stub downloading.  See conf/security.policy in
        // this project as well as this project's build.xml 'run' target for
        // additional required configuration.
        // Note this can be removed and use -Djava.security.manager instead when running the
        //  standalone java client
        if ( System.getSecurityManager() == null )
        {
            System.setSecurityManager(new RMISecurityManager());
        }

				System.setProperty("com.sun.CORBA.ORBUseDynamicStub", "true");

				// get initial context
        InitialContext ctx = getIIOPInitialContext(host);

				// lookup the IIOP EJB
        Object iiopObj = ctx.lookup(ejbLookupPath);

				System.out.println("iiop object: " + iiopObj);
				if(iiopObj == null)
					throw new Exception(ejbLookupPath + " not found in context");

				System.out.println("Narrowing iiopObj to " + remoteHome);
        HelloRemoteHome ejbHome = (HelloRemoteHome) PortableRemoteObject.narrow(iiopObj, remoteHome);

				System.out.println("ejbHome: " + ejbHome);
				if(ejbHome == null)
					throw new Exception("ejbHome is null, unable to create the remote interface");

				System.out.println("Calling create on ejbHome");
        HelloRemote remote = ejbHome.create();
				System.out.println("remote: " + remote);
				if(remote == null)
					throw new Exception("remote is null after create, unable to invoke hello method");

        System.out.println("remote.hello('Client') => " + remote.hello("Client"));
    }

    protected static InitialContext getIIOPInitialContext(String host) throws NamingException
    {
        Hashtable<String,String> env = new Hashtable<String,String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.cosnaming.CNCtxFactory");
        env.put(Context.PROVIDER_URL, "corbaloc::" + host + ":3528/JBoss/Naming/root");
        return new InitialContext(env);
    }
}
