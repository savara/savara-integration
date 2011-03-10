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
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.samples.trailblazer.util.ProcessEmail;

public class NotifyCustomerActions extends AbstractActionLifecycle {
	
	protected ConfigTree	_config;
	
	private static Logger _logger = Logger.getLogger(NotifyCustomerActions.class);
	
	public NotifyCustomerActions(ConfigTree config) { _config = config; }
	
	public Message notifyCustomer(Message message) throws Exception {
		
		_logger.debug("message received: \n" + message.getBody().get());
		
		//get the quote response and send it to the customer
		ConfigTree tree = ConfigTree.fromXml((String)message.getBody().get());		

		String quoteID = tree.getFirstTextChild("quoteId");
		String rate = tree.getFirstTextChild("interestRate");
		String errorCode = tree.getFirstTextChild("errorCode");
		String ssn = tree.getFirstTextChild("customerUID");
		String email = tree.getFirstTextChild("customerEmail");
				
		_logger.debug("info using for email: " + quoteID +" "+rate+" "+errorCode+" "+ssn+" "+email);
		
		
		_logger.debug("preparing to send the quote response via email to customer");
		ProcessEmail procEmail = new ProcessEmail(email, quoteID, rate, errorCode, ssn);

		// Uncomment to actually send an email
		//procEmail.sendEmail();
		
		System.out.println("Sent email to: " +email);
		
		
		return null;
	}
}
