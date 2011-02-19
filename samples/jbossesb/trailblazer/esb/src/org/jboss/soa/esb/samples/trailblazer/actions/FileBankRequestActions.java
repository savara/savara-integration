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

import java.util.Properties;

import org.apache.log4j.Logger;
import org.jboss.soa.esb.actions.AbstractActionLifecycle;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.samples.trailblazer.util.TrailblazerProperties;

public class FileBankRequestActions extends AbstractActionLifecycle {
	
	protected ConfigTree	_config;
    private Properties properties = new TrailblazerProperties();
	
    private static final String BANK_REQUEST_DIR="file.bank.monitored.dir";
    
    private String m_bankRequestDir=null;
    
	private static Logger _logger = Logger.getLogger(FileBankRequestActions.class);
	
	public FileBankRequestActions(ConfigTree config) {
		_config = config;
		m_bankRequestDir = properties.getProperty(BANK_REQUEST_DIR);
  	}
	
	public Message processFileBankRequest(Message message) throws Exception {
		System.out.println("Got the message for the File bank: " + message.getBody().get());
		
		_logger.debug("File bank message received: \n" + message.getBody().get());
		
		java.io.File dir=new java.io.File(m_bankRequestDir);
		
		java.io.File f=java.io.File.createTempFile("FileBank", "tb", dir);
		
		java.io.FileOutputStream fos=new java.io.FileOutputStream(f);
		
		fos.write(((String)message.getBody().get()).getBytes());
		
		fos.flush();
		fos.close();
		
        return null;
	}
}
