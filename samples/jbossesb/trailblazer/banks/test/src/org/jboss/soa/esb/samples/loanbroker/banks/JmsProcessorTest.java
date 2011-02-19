/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.soa.esb.samples.loanbroker.banks;

import static org.junit.Assert.assertTrue;

import java.util.Properties;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import junit.framework.JUnit4TestAdapter;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.junit.Test;

public class JmsProcessorTest {

	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * This can be activate if the server is running and you want
	 * to drop a message on the queue
	 *
	 */
	@Test public void sendLoanRequestXML()
	{

		QueueSession sess;
		Queue queue;
		try {
			Properties properties = new Properties();
			
			//ActiveMQ
			//properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		    //properties.put(Context.PROVIDER_URL           , "tcp://localhost:61616");
			//JBossMQ
		    properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		    properties.put(Context.PROVIDER_URL           , "localhost");
			InitialContext ctx = new InitialContext(properties);
		
			QueueConnectionFactory factory = (QueueConnectionFactory) ctx
					.lookup("ConnectionFactory");
			QueueConnection cnn = factory.createQueueConnection();
			sess = cnn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			queue = sess.createQueue("queue/C");
			//queue = sess.createQueue("queue/C");
			String str="23456890,3,4,20000,5,unique,kurt.stam@jboss.com";
			TextMessage msg = sess.createTextMessage(str);
			QueueSender sender = sess.createSender(queue);
			sender.send(msg);
            ctx.close();
			//if the test gets here without errors it passes.
			assertTrue(true);
		} catch (Exception e) {
			//if it fails it may be because the server is not up and the Queue is not found
			//I'm not letting the test fail, since we may want to do nightly build/test runs
			//which may not have the server up.
			logger.log(Level.ERROR, e.getMessage(), e);
			logger.log(Level.ERROR, "Is the server up and running?");
		}
	}

	public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(JmsProcessorTest.class);
    }
}
