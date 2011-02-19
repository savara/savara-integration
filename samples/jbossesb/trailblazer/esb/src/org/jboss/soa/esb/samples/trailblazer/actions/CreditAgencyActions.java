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
 * @daniel.brum@jboss.com
 */

import java.util.Random;

import org.apache.log4j.Logger;
import org.jboss.soa.esb.actions.AbstractActionLifecycle;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;
import org.jboss.soa.esb.message.format.MessageType;
import org.jboss.soa.esb.util.Util;

public class CreditAgencyActions extends AbstractActionLifecycle {
	protected ConfigTree	_config;
	
	private static Logger _logger = Logger.getLogger(CreditAgencyActions.class);
	
	public CreditAgencyActions(ConfigTree config) { _config = config; }
	
	public Message noOperation(Message message) { return message; }
	
	public Message processCreditRequest(Message message) throws Exception{
		
		_logger.debug("message received: " + Util.serialize(message) );		
		
		String csvData = (String)message.getBody().get();
		_logger.debug("csv data received: " + csvData);
		
		String[] parts=csvData.split(",");
		
		//generate a random score between 1 and 10
		Random rand = new Random();
		int n = 10;
		int score = rand.nextInt(n+1);
		
		//send back the reply
        Message replyMessage = MessageFactory.getInstance().getMessage(MessageType.JBOSS_XML);		
		
        _logger.info("CreditAgency sending back a credit score of " + score);
        
        // Return the unique customer ref (ssn) and the score
        String resp=parts[1]+","+score;
        replyMessage.getBody().add(resp);
		
		return replyMessage;
	}
	
	public Message debugMessage(Message message) throws Exception{
		
		_logger.debug("message received in processCreditRequest with message: " + Util.serialize(message));

		
		
		return message;
	}
}


