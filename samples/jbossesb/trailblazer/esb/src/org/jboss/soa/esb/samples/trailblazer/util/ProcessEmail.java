/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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


package org.jboss.soa.esb.samples.trailblazer.util;

import java.util.Properties;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.log4j.Logger;
import org.jboss.soa.esb.helpers.Email;


public class ProcessEmail {
	
	private Logger logger = Logger.getLogger(this.getClass());	
	private String emailTo, quoteID, rate, code, ssn;
    private Properties properties = new TrailblazerProperties();

    public ProcessEmail(String email, String quoteID, String rate, String code, String ssn) {
		this.emailTo=email;
		this.quoteID=quoteID;
		this.rate=rate;
		this.code=code;
		this.ssn=ssn;
		logger.debug("creating email helper");
						
	}
	
	public void sendEmail() {		
		try {
			if (null==emailTo) {
				logger.error("no email found for customer, aborting send");
				return;
			}
			logger.info("customer SSN " + ssn + " - sending email to: " + emailTo);
			Email emailMessage = new Email();			
			emailMessage.setSendTo(emailTo);
			emailMessage.setSubject("TrailBlazer Quote from Bank");
			String emailFrom = properties.getProperty("email.from");
			emailMessage.setFrom(emailFrom);
//			File emailTemplate = new File(LoanBrokerConstants.EMAIL_TEMPLATE);
//			String quoteMsg=FileUtil.readTextFile(emailTemplate);
			emailMessage.setMessage(fillTemplate());
			emailMessage.sendMessage();			
		}catch (Exception e) {			
			logger.error("error sending email - " + e);	
			e.printStackTrace();		
		}		
	}
	
	private String fillTemplate() throws Exception{
		String templatePath = properties.getProperty("email.template.path", "template");
		String templateFile = properties.getProperty("email.template.file", "quote");
		                 
		logger.info("loading StringTemplate from path: "+templatePath);
		logger.info("loading StringTemplate from file: "+templateFile);
		
		StringTemplateGroup group =  new StringTemplateGroup("loan");
		StringTemplate email = group.getInstanceOf(templatePath + "/" + templateFile);
		
		//String email, String quoteID, String rate, String code, String ssn
		email.setAttribute("quote", quoteID);
		email.setAttribute("rate", rate);
		email.setAttribute("ssn", ssn);
		email.setAttribute("code", code);
		email.setAttribute("email", emailTo);
		
		return email.toString();
		
	}
	
	public static void main(String[] args) {
		ProcessEmail email = new ProcessEmail("dbrum101@gmail.com", "111", ".65", "0", "123456");
		email.sendEmail();
		
	}

}
