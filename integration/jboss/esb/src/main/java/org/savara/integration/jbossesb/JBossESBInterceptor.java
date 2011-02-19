/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and others contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.savara.integration.jbossesb;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import org.savara.activity.ActivityProcessor;
import org.savara.activity.ActivityProcessorFactory;
import org.savara.activity.model.InteractionActivity;
import org.savara.activity.model.MessageParameter;
import org.savara.common.util.MessageUtils;
import org.jboss.soa.esb.message.*;
import org.jboss.soa.esb.couriers.*;

/**
 * This class represents the filter used to intercept ESB
 * messages an apply them to relevant Service Validators.
 */
public class JBossESBInterceptor extends org.jboss.soa.esb.filter.InputOutputFilter {
	
	private static final String TOPIC_PREFIX = "topic/";
	private static final String QUEUE_PREFIX = "queue/";
	private static final String JMS_PROTOCOL_PREFIX = "jms:";

	private static final Logger logger = Logger.getLogger(JBossESBInterceptor.class);
	
	/**
	 * The default constructor.
	 */
	public JBossESBInterceptor() {
	}
	
	/**
	 * This method is invoked when a message is sent by an
	 * ESB service.
	 * 
	 * @param msg The ESB message
	 * @param params The parameters
	 * @throws CourierException Failed to validate message
	 */
	public Message onOutput(Message msg, java.util.Map params)
			throws CourierException {
		
		String endpoint=getEndpoint(msg);
		
		java.io.Serializable message=ESBUtil.getMessage(msg);
	
		if (endpoint != null && message instanceof String) {
			
			try {
				ActivityProcessor ap=ActivityProcessorFactory.getActivityProcessor();
				
				InteractionActivity ia=new InteractionActivity();
				
				ia.setDestinationAddress(endpoint);
				
				ia.setOutbound(true);
				
				MessageParameter mp=new MessageParameter();
				mp.setType(MessageUtils.getMessageType(message));
				mp.setValue((String)message);
				
				ia.getParameter().add(mp);
				
				String replyTo=getReplyToEndpoint(msg);

				if (replyTo != null) {
					ia.setReplyToAddress(replyTo);
				}
				
				ap.process(ia);
				
			} catch(Exception e) {
				logger.error("Failed to report interaction activity", e);
			}
		}
	        
		return(msg);
	}
	
	/**
	 * This method is invoked when a message is received by an
	 * ESB service.
	 * 
	 * @param msg The ESB message
	 * @param params The parameters
	 * @throws CourierException Failed to validate message
	 */
	public Message onInput(Message msg, java.util.Map params)
			throws CourierException {
		
		String endpoint=getEndpoint(msg);
		
		java.io.Serializable message=ESBUtil.getMessage(msg);
		
		if (endpoint != null && message instanceof String) {
			
			try {
				ActivityProcessor ap=ActivityProcessorFactory.getActivityProcessor();
				
				InteractionActivity ia=new InteractionActivity();
				
				ia.setDestinationAddress(endpoint);
				
				ia.setOutbound(false);
				
				MessageParameter mp=new MessageParameter();
				mp.setType(MessageUtils.getMessageType(message));
				mp.setValue((String)message);
				
				ia.getParameter().add(mp);
				
				String replyTo=getReplyToEndpoint(msg);

				if (replyTo != null) {
					ia.setReplyToAddress(replyTo);
				}
				
				ap.process(ia);
				
			} catch(Exception e) {
				logger.error("Failed to report interaction activity", e);
			}
		}

		return(msg);
	}
	
	/**
	 * This method returns an endpoint associated with the 'to'
	 * destination of the supplied message.
	 * 
	 * @param msg The message
	 * @return The endpoint, or null if not relevant
	 */
	protected String getEndpoint(Message msg) {
		String ret=null;
		
		if (msg != null && msg.getHeader() != null && 
				msg.getHeader().getCall() != null &&
				msg.getHeader().getCall().getTo() != null &&
				msg.getHeader().getCall().getTo().getAddr() != null) {
				
			String key=msg.getHeader().getCall().getTo().getAddr().getAddress();
			int ind=-1;
			
			if (key.startsWith(JMS_PROTOCOL_PREFIX) && 
					((ind=key.indexOf(QUEUE_PREFIX)) != -1 ||
					(ind=key.indexOf(TOPIC_PREFIX)) != -1)) {
				ret = JMS_PROTOCOL_PREFIX+key.substring(ind);
			}
		}
		
		logger.debug("Destination for message '"+msg+"' is: "+ret);
		
		return(ret);
	}
	
	/**
	 * This method returns an endpoint associated with the 'to'
	 * destination of the supplied message.
	 * 
	 * @param msg The message
	 * @return The endpoint, or null if not relevant
	 */
	protected String getReplyToEndpoint(Message msg) {
		String ret=null;
		
		if (msg != null && msg.getHeader() != null && 
				msg.getHeader().getCall() != null &&
				msg.getHeader().getCall().getReplyTo() != null &&
				msg.getHeader().getCall().getReplyTo().getAddr() != null) {
				
			String key=msg.getHeader().getCall().getReplyTo().getAddr().getAddress();
			int ind=-1;
			
			if (key.startsWith(JMS_PROTOCOL_PREFIX) && 
					((ind=key.indexOf(QUEUE_PREFIX)) != -1 ||
					(ind=key.indexOf(TOPIC_PREFIX)) != -1)) {
				ret = JMS_PROTOCOL_PREFIX+key.substring(ind);
			}
		}
		
		logger.debug("Reply-To Destination for message '"+msg+"' is: "+ret);

		return(ret);
	}
}
