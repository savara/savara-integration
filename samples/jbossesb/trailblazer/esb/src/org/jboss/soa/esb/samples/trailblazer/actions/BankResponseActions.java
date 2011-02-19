package org.jboss.soa.esb.samples.trailblazer.actions;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and others contributors as indicated 
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
 * 
 * (C) 2005-2006,
 * @author mark.little@jboss.com
 */

import org.apache.log4j.Logger;
import org.jboss.soa.esb.actions.AbstractActionLifecycle;
import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;
import org.jboss.soa.esb.message.format.MessageType;
import org.jboss.soa.esb.samples.trailblazer.util.ProcessEmail;

public class BankResponseActions extends AbstractActionLifecycle {
	
	protected ConfigTree	_config;
	
    private final String SERVICE_NAME = "customer";
    private final String SERVICE_CAT = "notifiers";
    private ServiceInvoker notifierInvoker;

	private static Logger _logger = Logger.getLogger(BankResponseActions.class);
	
	public BankResponseActions(ConfigTree config) {
		_config = config;
		
        try {
        	notifierInvoker = new ServiceInvoker(SERVICE_CAT, SERVICE_NAME);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ServiceInvoker for '" + SERVICE_CAT + ":" + SERVICE_NAME + "'.", e);
        }
  	}
	
	public Message processResponseFromJMSBank(Message message) throws Exception {
		System.out.println("Got the message from the JMS bank: " + message.getBody().get());
		
		_logger.debug("JMS bank message received: \n" + message.getBody().get());
		
        Message notifyMessage = MessageFactory.getInstance().getMessage(MessageType.JBOSS_XML);
        notifyMessage.getBody().add(message.getBody().get());
		
        notifierInvoker.deliverAsync(notifyMessage);

        return null;
	}
	
	public Message processResponseFromFileBank(Message message) throws Exception {
		String mesg=new String((byte[])message.getBody().get());
		
		System.out.println("Got the message from the File bank: " + mesg);
		
		_logger.debug("File bank message received: \n" + mesg);
		
        Message notifyMessage = MessageFactory.getInstance().getMessage(MessageType.JBOSS_XML);
        notifyMessage.getBody().add(mesg);
		
        notifierInvoker.deliverAsync(notifyMessage);

        return null;
	}
}
