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

import java.math.BigDecimal;
import java.util.Properties;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.xml.DOMConfigurator;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * The JMS Manager listens to an incoming queue (by default queue/A on localhost). It consumes the
 * message assuming it is a TextMessage, and that the text is an XML structure that can be marshalled
 * into a BankQuoteRequest. The request will be processed and a BankQuoteRequest Reply will be serialized to XML
 * and send to the outgoing queue (by default this is queue/B on localhost).
 * 
 * @author kstam
 */
public class ManagerJMS implements javax.jms.MessageListener, ExceptionListener {
	private static Logger logger = Logger.getLogger(ManagerJMS.class);
	/** Name of the Bank */
	private static final String BANK_NAME = "JMSBasedBank";
	/** Rate premium the bank charges */
	private static final BigDecimal RATE_PREMIUM = BigDecimal.valueOf(0.50);
	/** Maximum duration of the loan the bank will accept [months] */
	private static final int MAXLOANTERM = 120;
	/** Default incoming queue */
	private static String QUEUE_IN = "queue/C";
	/** Default outgoing queue */
	private static String QUEUE_OUT = "queue/D";
	/** Properties used when constructing the InitialContext */
	private static Properties properties = new Properties();
	/** Queue connection for incoming queue */
	private QueueConnection inQueueConnection;
        
        /**
         * The minimum error delay.
         */
        private static final long MIN_ERROR_DELAY = 1000 ;
        /**
         * The maximum error delay.
         */
        private static final long MAX_ERROR_DELAY = (MIN_ERROR_DELAY << 5) ;
        /**
         * The error delay.
         */
        private long errorDelay ;


	/**
	 * Sets up the queue listener. By default is listenes to queue/A on localhost.
	 */
	public void listen() throws NamingException, JMSException {
			//MQ Series
			//properties.put(Context.INITIAL_CONTEXT_FACTORY,"com.ibm.mq.jms.context.WMQInitialContextFactory");
			//properties.put(Context.PROVIDER_URL           , "dev38:1415/SYSTEM.DEF.SVRCONN");
			//ActiveMQ
			//properties.put(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			//properties.put(Context.PROVIDER_URL           , "tcp://localhost:61616");
			//JBossMQ
            /** Context used to lookup the Queues */
            InitialContext ctx = getContext();
                try {
			logger.info("Looking up connection factory");
			QueueConnectionFactory qcf = (QueueConnectionFactory) ctx
					.lookup("ConnectionFactory");
			logger.info("Creating connection");
			inQueueConnection = qcf.createQueueConnection();
			logger.info("Creating session");
			QueueSession qs = inQueueConnection.createQueueSession(false,
					Session.DUPS_OK_ACKNOWLEDGE);
			logger.info("Looking up queue=" + QUEUE_IN);
			Queue inQueue=null;
			try {
				inQueue = (Queue) ctx.lookup(QUEUE_IN);
			} catch (NamingException ne) {
				inQueue = qs.createQueue(QUEUE_IN);
			}
			//Queue 
			QueueReceiver qr = qs.createReceiver(inQueue);
			qr.setMessageListener(this);
                        inQueueConnection.setExceptionListener(this);
			inQueueConnection.start();
                } finally {
                        ctx.close();
		}
	}

    private InitialContext getContext() throws NamingException {
        InitialContext ctx;
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        properties.put(Context.PROVIDER_URL           , "localhost");
        ctx = new InitialContext(properties);
        return ctx;
    }

    /**
	 * Sets up the queue listener. By default is listenes to queue/A on localhost.  It consumes the
	 * message assuming it is a TextMessage, and that the text is an XML structure that can be marshalled
	 * into a BankQuoteRequest. The request will be processed and a BankQuoteRequest Reply will be serialized to XML
	 * and send to the outgoing queue (by default this is queue/B on localhost). If the processing results
	 * in an error a BankQuoteReply with ErrorCode other then 0 is send to the outgoing queue.
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 * changed this to use a CSV from the request, left the reply as is - dbrum
	 */
	public void onMessage(Message message) {
		QueueConnection outQueueConnection;
		logger.info("Got message: " + message);
		try {
			TextMessage textMessage = (TextMessage) message;
			String xml = textMessage.getText();

			Bank bank = new Bank(BANK_NAME, RATE_PREMIUM, MAXLOANTERM);

			BankQuoteRequest bankQuoteRequest = bank.getQuoteFromXML(xml);
			           
			BankQuoteReply bankQuoteReply = bank
					.processMessage(bankQuoteRequest);
			
			XStream xstream = new XStream(new DomDriver());
			xstream.alias("quote", BankQuoteReply.class);
			
			String bankQuoteReplyXML = xstream.toXML(bankQuoteReply);
			
			logger.log(Level.INFO, "Looking up connection factory");
            InitialContext ctx = getContext();
			QueueConnectionFactory qcf = (QueueConnectionFactory) ctx
					.lookup("ConnectionFactory");
			
			logger.log(Level.INFO, "Creating connection");
			outQueueConnection = qcf.createQueueConnection();
			try {
				logger.log(Level.INFO, "Creating session");
				QueueSession qs = outQueueConnection.createQueueSession(false,
						Session.DUPS_OK_ACKNOWLEDGE);
				Queue outQueue;
				try {
					outQueue = (Queue) ctx.lookup(QUEUE_OUT);
				} catch (NamingException ne) {
					outQueue = qs.createQueue(QUEUE_OUT);
				}
				TextMessage responseTextMessage = qs.createTextMessage(bankQuoteReplyXML);

				QueueSender queueSender = qs.createSender(outQueue);

				queueSender.send(responseTextMessage);
				logger.info("message being sent back on " + QUEUE_OUT + "\n" + responseTextMessage);
				System.out.println("message being sent back on " + QUEUE_OUT + "\n" + responseTextMessage);
			} catch (JMSException je) {
				logger.log(Level.ERROR, je.getMessage(), je);
			} finally {
				outQueueConnection.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
        
        /**
         * Handle an asynchronous exception.
         * @param jmsException The asynchronous exception
         */
        public void onException(final JMSException jmsException) {
            errorDelay = MIN_ERROR_DELAY ;
            logger.warn("JMS Connection lost, attempting to reconnect") ;
            
            while(true)
            {
                try {
                    close() ;
                } catch (final Exception ex) {} // ignore
                
                try {
                    listen() ;
                    logger.warn("Established connection, continuing with processing") ;
                    break ;
                } catch (final Exception ex) {} // ignore
                
                logger.warn("Error establishing connection, backing off for " + errorDelay + " milliseconds") ;
                
                try {
                    Thread.sleep(errorDelay) ;
                } catch (final InterruptedException ie) {} // ignore
                
                if (errorDelay < MAX_ERROR_DELAY)
                {
                    errorDelay <<= 1 ;
                }
            }
                
            
        }
        
	/** 
	 * Closing the queue connection as to not leak connection resources.
	 */
	public void close() throws JMSException {
                if (inQueueConnection != null) {
                    inQueueConnection.close();
                    inQueueConnection = null ;
                }
	}
	/**
	 * Invokes an instance of JMS based bank listener.
	 * @param args - provider url, queue_in, queue_out
	 */
	public static void main(String[] args) {
		if (args != null && args.length > 0) {
			if (args.length > 0) {
				QUEUE_IN = args[1];
				if (args.length > 1) {
					QUEUE_OUT = args[2];
				}
			}
		} else {
			System.out.println(
					"Usage: ManagerJMS providerURL, queueIn, queueOut");
		}
		System.out.println("Starting JMS Bank Listener...");
		System.out.println("Listening  to Queue '" + QUEUE_IN + "'.");
		System.out.println("Responding to Queue '" + QUEUE_OUT + "'.");
		DOMConfigurator.configure("log4j.xml");
		ManagerJMS managerJMS = new ManagerJMS();
                try {
                    managerJMS.listen();

		    synchronized(managerJMS) {
			managerJMS.wait();
		    }
                } catch (final Exception ex) {
                    logger.error(ex.getMessage(), ex) ;
                }
	}
}
