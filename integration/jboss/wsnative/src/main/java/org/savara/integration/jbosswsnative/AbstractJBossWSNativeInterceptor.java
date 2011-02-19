/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.savara.integration.jbosswsnative;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pi4soa.common.util.NamesUtil;
import org.pi4soa.common.xml.NameSpaceUtil;
import org.pi4soa.common.xml.XMLUtils;
import org.savara.common.util.MessageUtils;
import org.jboss.ws.core.jaxws.handler.SOAPMessageContextJAXWS;
import org.jboss.ws.metadata.umdm.ParameterMetaData;
import org.jboss.wsf.common.DOMWriter;
import org.jboss.wsf.common.handler.GenericSOAPHandler;

/**
 * Common abstract base class for the client and server JBossWS
 * native interceptors.
 *
 */
public abstract class AbstractJBossWSNativeInterceptor extends GenericSOAPHandler {

	private static Log logger = LogFactory.getLog(AbstractJBossWSNativeInterceptor.class);

	/**
	 * This method returns the message content.
	 * 
	 * @param soapCtx The SOAP message context
	 * @return The message content
	 * @throws Exception Failed to get the message content
	 */
	public static String getMessage(SOAPMessageContext soapCtx) throws Exception {
		String ret=null;
				
		SOAPElement elem = getMessageBody(soapCtx);
		if (elem != null) {
			ret = DOMWriter.printNode(elem, true);
		}
		
		return(ret);
	}
	
	/**
	 * This method determines the message type associated with the supplied SOAP
	 * message context.
	 * 
	 * @param soapCtx The SOAP message context
	 * @return The message type
	 * @throws SOAPException Failed to determine the message type
	 */
	public static String getMessageType(SOAPMessageContext soapCtx, boolean request) throws SOAPException {
		String ret=null;
		
		if (soapCtx instanceof org.jboss.ws.core.jaxws.handler.MessageContextJAXWS) {
			org.jboss.ws.core.jaxws.handler.MessageContextJAXWS mc=
				(org.jboss.ws.core.jaxws.handler.MessageContextJAXWS)soapCtx;
		    org.jboss.ws.metadata.umdm.OperationMetaData opmetadata=mc.getOperationMetaData();
		    
		    if (opmetadata != null) {
		    	ParameterMetaData pmd=null;
		    	
		    	if (request && opmetadata.getInputParameters().size() == 1) {
			    	pmd = opmetadata.getInputParameters().get(0);	
		    	} else if (!request && opmetadata.getOutputParameters().size() == 1) {
			    	pmd = opmetadata.getOutputParameters().get(0);	
		    	}
		    	
		    	if (pmd != null) {
			    	// TODO: Find out why 'genericParam' is generated when the parameter
			    	// is a complex type
					if (pmd.getXmlName().getLocalPart().equals("genericParam")) {
						
						// Extract message value
			            SOAPElement elem=getMessageBody(soapCtx);
			            
			            if (elem != null) {
			            	ret = elem.getElementQName().toString();
			            }
	
					} else {
						ret = pmd.getXmlName().toString();
					}
		    	}
		    }
		}
		
		if (ret == null) {
			try {
				ret = MessageUtils.getMessageType(getMessage(soapCtx));
				logger.debug("Message type (derived from content)="+ret);
			} catch(Exception e) {
				logger.error("Failed to get message type from message content", e);
			}
		} else {
			logger.debug("Message type="+ret);
		}

		return(ret);
	}

	protected static SOAPElement getMessageBody(SOAPMessageContext soapCtx) throws SOAPException {
		SOAPElement ret=null;
		
		// Extract message value
        SOAPBody soapBody = soapCtx.getMessage().getSOAPPart().getEnvelope().getBody();
		
    	java.util.Iterator<?> iter=soapBody.getChildElements();
    	while (ret == null && iter.hasNext()) {
    		Object body=iter.next();
    		
    		if (body instanceof SOAPElement) {
    			ret = (SOAPElement)body;
    		}
    	}
        
        if (ret instanceof SOAPFault) {
        	SOAPFault fault=(SOAPFault)ret;
        	
        	ret = (SOAPElement)fault.getDetail().getChildElements().next();
        }

        return(ret);
	}

	/**
	 * This method returns the endpoint associated with the supplied SOAP
	 * message context.
	 * 
	 * @param soapCtx The SOAP message context
	 * @return The endpoint
	 */
	public static QName getEndpoint(SOAPMessageContext soapCtx) {
		QName service=(QName)soapCtx.get(MessageContext.WSDL_SERVICE);
        
		if (logger.isDebugEnabled()) {
			logger.debug("Service for endpoint = "+service);
		}
		
		if (service == null && soapCtx instanceof SOAPMessageContextJAXWS) {
			SOAPMessageContextJAXWS smc=(SOAPMessageContextJAXWS)soapCtx;
			
			if (smc.getEndpointMetaData() != null &&
							smc.getEndpointMetaData().getServiceMetaData() != null) {
				service = smc.getEndpointMetaData().getServiceMetaData().getServiceName();
			}			
		}

		return(service);
	}
}
