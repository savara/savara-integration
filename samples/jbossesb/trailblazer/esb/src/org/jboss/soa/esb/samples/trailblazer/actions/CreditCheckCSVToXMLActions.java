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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.jboss.soa.esb.actions.AbstractActionLifecycle;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;
import org.jboss.soa.esb.message.format.MessageType;
import org.jboss.soa.esb.util.Util;

public class CreditCheckCSVToXMLActions extends AbstractActionLifecycle {
	protected ConfigTree	_config;
	
	private static Logger _logger = Logger.getLogger(CreditCheckCSVToXMLActions.class);
	
	public CreditCheckCSVToXMLActions(ConfigTree config) { _config = config; }
	
	public Message handle(Message message) throws Exception{
		
		_logger.debug("CSV message received: " + Util.serialize(message) );		
		
		String csv=(String)message.getBody().get();
		
		String[] parts=csv.split(",");
		
		String xml="<creditCheckResult ssn=\""+parts[0]+
					"\" score=\""+parts[1]+"\" />";

		_logger.debug("XML data to return" + xml);
				
		//send back the reply
        Message replyMessage = MessageFactory.getInstance().getMessage(MessageType.JBOSS_XML);		
		
        replyMessage.getBody().add(xml);
		
		return replyMessage;
	}
}


