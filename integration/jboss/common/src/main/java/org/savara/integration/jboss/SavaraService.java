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
package org.savara.integration.jboss;

import java.util.logging.Logger;

import org.savara.activity.ActivityProcessor;
import org.savara.activity.ActivityProcessorFactory;
import org.savara.activity.ActivityValidator;
import org.savara.activity.DefaultActivityProcessor;
import org.savara.activity.notifier.jms.JMSActivityNotifier;
import org.savara.activity.validator.cdm.CDMActivityValidator;
import org.savara.common.config.file.FileConfiguration;

/**
 * This is the JBoss Savara Service Validator Service managing the Service Validator.
 *
 * @author gbrown
 *
 */
public class SavaraService extends org.jboss.system.ServiceMBeanSupport implements SavaraServiceMBean {

	private final static Logger logger = Logger.getLogger(SavaraService.class.getName());
  
	/**
	 * The default constructor.
	 */
	public SavaraService() {
	}

	/**
	 * This method starts the service, instantiating the
	 * Service Validation Manager and registering it with JNDI.
	 */
	protected void startService() throws Exception {
		logger.info("Starting Savara Service Validator Manager");

		// Create ActivityProcessor
		ActivityProcessor dap=new DefaultActivityProcessor();
		
		FileConfiguration config=new FileConfiguration();
		
		JMSActivityNotifier notifier=new JMSActivityNotifier();
		notifier.setConfiguration(config);
		
		dap.getNotifiers().add(notifier);
		
		ActivityValidator validator=new CDMActivityValidator();
		
		dap.getValidators().add(validator);
		
		setActivityProcessor(dap);
	}
	
	public void setActivityProcessor(ActivityProcessor ap) {
		ActivityProcessorFactory.setActivityProcessor(ap);
	}

	/**
	 * This method stops the service, closing the
	 * Service Validation Manager and unregistering it from JNDI.
	 */
	protected void stopService() throws Exception {
		logger.info("Stopping Savara Service Validator Manager");
	}
}
