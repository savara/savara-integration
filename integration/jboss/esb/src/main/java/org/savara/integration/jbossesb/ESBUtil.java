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

import org.apache.log4j.Logger;
import org.pi4soa.common.util.NamesUtil;
import org.pi4soa.common.xml.NameSpaceUtil;
import org.pi4soa.common.xml.XMLUtils;

/**
 * This class provides utility functions for processing
 * ESB messages.
 */
public class ESBUtil {

	public static final String SOAP_ENVELOPE = "http://schemas.xmlsoap.org/soap/envelope/";

	/**
	 * This method returns the message content associated
	 * with the supplied JBoss ESB message.
	 * 
	 * @param message The esb message
	 * @return The message content
	 */
	public static java.io.Serializable getMessage(
			org.jboss.soa.esb.message.Message message) {
		java.io.Serializable ret=null;
		
		ret = (java.io.Serializable)message.getBody().get();

		// Check if should return a multipart message
		if (ret == null) {
			
			// Check if single or multipart message
			if (message.getBody().getNames() != null &&
					message.getBody().getNames().length == 1) {
				
				Object mesg=message.getBody().get(message.getBody().getNames()[0]);
				
				if (logger.isDebugEnabled()) {
					logger.debug("MESSAGE("+
							message.getBody().getNames()[0]+")="+mesg);
				}
				
				if (mesg instanceof java.io.Serializable) {
					ret = (java.io.Serializable)mesg;
				}
				
			} else if (message.getBody().getNames() != null &&
					message.getBody().getNames().length > 1) {
				
				java.util.Hashtable<String,Object> multipart=
							new java.util.Hashtable<String,Object>();
				
				for (int i=0; i < message.getBody().getNames().length; i++) {
					multipart.put(message.getBody().getNames()[i],
							message.getBody().get(message.getBody().getNames()[i]));
				}
				
				ret = multipart;
			}
		}
		
		if (ret instanceof byte[]) {
			ret = new String((byte[])ret);
		}
		
		ret = getBody(ret);
		
		return(ret);
	}
	
	public static java.io.Serializable getBody(java.io.Serializable value) {
		java.io.Serializable ret=null;
		
		if (value instanceof org.w3c.dom.Element &&
				((org.w3c.dom.Element)value).getLocalName().equals("Envelope") &&
				((org.w3c.dom.Element)value).getNamespaceURI().equals(
								SOAP_ENVELOPE)) {
			
			org.w3c.dom.NodeList bodylist=((org.w3c.dom.Element)value).
						getElementsByTagNameNS(SOAP_ENVELOPE, "Body");
			
			for (int i=0; ret == null && i < bodylist.getLength(); i++) {
				if (bodylist.item(i) instanceof org.w3c.dom.Element &&
						bodylist.item(i).getLocalName().equals("Body")) {
					org.w3c.dom.NodeList children=((org.w3c.dom.Element)bodylist.item(i)).getChildNodes();
				
					for (int j=0; ret == null && j < children.getLength(); j++) {
						if (children.item(j) instanceof org.w3c.dom.Element) {
							ret = (java.io.Serializable)children.item(j);
						}
					}
				}
			}
		} else if (value instanceof String) {
			
			try {
				org.w3c.dom.Node node=XMLUtils.getNode((String)value);
				
				ret = getBody((java.io.Serializable)node);
				
				// Convert to string
				if (ret instanceof org.w3c.dom.Node) {
					ret = XMLUtils.getText((org.w3c.dom.Node)ret);
				}
				
			} catch(Exception e) {
				logger.warn("Failed to obtain message body from value: "+value);
			}
			
		} else {
			ret = value;
		}

		return(ret);
	}
	
	private static Logger logger = Logger.getLogger(ESBUtil.class);	
}
