/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and others contributors as indicated
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
package org.savara.integration.jbosswsnative;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.savara.activity.ActivityProcessor;
import org.savara.activity.ActivityProcessorFactory;
import org.savara.activity.model.ExchangeType;
import org.savara.activity.model.InteractionActivity;
import org.savara.activity.model.MessageParameter;

public class JBossWSNativeClientInterceptor extends AbstractJBossWSNativeInterceptor {
	
	private static Log logger = LogFactory.getLog(JBossWSNativeClientInterceptor.class);

	public JBossWSNativeClientInterceptor() {
		if (logger.isDebugEnabled()) {
			logger.debug("JBossWSNativeClientInterceptor created");
		}
	}
   
	protected boolean handleInbound(MessageContext ctx) {
		boolean ret=true;
		SOAPMessageContext soapCtx = (SOAPMessageContext)ctx;

		QName endpoint=getEndpoint(soapCtx);
		
		if (endpoint != null) {
			
			try {
				ActivityProcessor ap=ActivityProcessorFactory.getActivityProcessor();
				
				InteractionActivity ia=new InteractionActivity();
				
				ia.setDestinationType(endpoint.toString());
				
				ia.setExchangeType(ExchangeType.RESPONSE);
				ia.setOutbound(false);
				
				MessageParameter mp=new MessageParameter();
				mp.setType(getMessageType(soapCtx, false));
				mp.setValue(getMessage(soapCtx));
				
				ia.getParameter().add(mp);
				
				ap.process(ia);
				
			} catch(Exception e) {
				logger.error("Failed to report interaction activity", e);
			}
		}

		return(ret);
	}

	protected boolean handleOutbound(MessageContext ctx) {
		boolean ret=true;
		SOAPMessageContext soapCtx = (SOAPMessageContext)ctx;

		QName endpoint=getEndpoint(soapCtx);
		
		if (endpoint != null) {
			
			try {
				ActivityProcessor ap=ActivityProcessorFactory.getActivityProcessor();
				
				InteractionActivity ia=new InteractionActivity();
				
				ia.setDestinationType(endpoint.toString());
				
				ia.setExchangeType(ExchangeType.REQUEST);
				ia.setOutbound(true);
				
				MessageParameter mp=new MessageParameter();
				mp.setType(getMessageType(soapCtx, true));
				mp.setValue(getMessage(soapCtx));
				
				ia.getParameter().add(mp);
				
				ap.process(ia);
				
			} catch(Exception e) {
				logger.error("Failed to report interaction activity", e);
			}
		}

		return(ret);
	}

	public boolean handleFault(MessageContext ctx) {
		return handleOutbound(ctx);
	}
}