package org.jboss.soa.esb.samples.trailblazer.loanbroker;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;
import org.jboss.soa.esb.message.format.MessageType;
import org.jboss.soa.esb.samples.trailblazer.util.TrailblazerProperties;
import org.jboss.soa.esb.samples.trailblazer.web.WebCustomer;
import org.jboss.soa.esb.client.ServiceInvoker;

/*
 * LoanBroker is responsible for getting customer requests for loans onto the JBoss ESB
 * Requests will come from a JSR-181 pojo web service (@org.jboss.soa.esb.samples.trailblazer.web.LoanBrokerWS)
 * The sequence of events from the LoanBroker are:
 * 1 - Prepare a request for the CreditAgency (transform the customer into a XML representation)
 * 2 - Send to CreditAgency and get response (lookup the customer using the SSN (see @CustomerMasterFile)
 * 3 - Prepare a LoanRequest for each of the banks in their unique data structures and send
 * 4 - Collect the response(s) from the bank(s) and send an email to the customer with the quote offers
 */

public class LoanBroker {

    private static Logger logger = Logger.getLogger(LoanBroker.class);
    //used to locate our entries in the trailblazer-properties
    private final String CREDIT_AGENCY_SERVICE_NAME = "creditagency.service.epr.name";
    private final String CREDIT_AGENCY_SERVICE_CAT = "creditagency.service.category";
    private final String BANK1_SERVICE_NAME = "bank1.service.epr.name";
    private final String BANK1_SERVICE_CAT = "bank1.service.category";
    private final String BANK2_SERVICE_NAME = "bank2.service.epr.name";
    private final String BANK2_SERVICE_CAT = "bank2.service.category";
    private final String NOTIFIER_SERVICE_NAME = "notifier.service.epr.name";
    private final String NOTIFIER_SERVICE_CAT = "notifier.service.category";
    private Properties properties = new TrailblazerProperties();
    private ServiceInvoker serviceInvoker; 
    private ServiceInvoker bank1Invoker;
    private ServiceInvoker bank2Invoker;
    private ServiceInvoker notifierInvoker;

    public LoanBroker() {
        String serviceCategoryName = properties.getProperty(CREDIT_AGENCY_SERVICE_CAT);
        String serviceName = properties.getProperty(CREDIT_AGENCY_SERVICE_NAME);
        String bank1ServiceCategoryName = properties.getProperty(BANK1_SERVICE_CAT);
        String bank1ServiceName = properties.getProperty(BANK1_SERVICE_NAME);
        String bank2ServiceCategoryName = properties.getProperty(BANK2_SERVICE_CAT);
        String bank2ServiceName = properties.getProperty(BANK2_SERVICE_NAME);
        String notifierServiceCategoryName = properties.getProperty(NOTIFIER_SERVICE_CAT);
        String notifierServiceName = properties.getProperty(NOTIFIER_SERVICE_NAME);

        try {
            serviceInvoker = new ServiceInvoker(serviceCategoryName, serviceName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ServiceInvoker for '" + serviceCategoryName + ":" + serviceName + "'.", e);
        }
        
        try {
            bank1Invoker = new ServiceInvoker(bank1ServiceCategoryName, bank1ServiceName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ServiceInvoker for '" + bank1ServiceCategoryName + ":" + bank1ServiceName + "'.", e);
        }
        
        try {
            bank2Invoker = new ServiceInvoker(bank2ServiceCategoryName, bank2ServiceName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ServiceInvoker for '" + bank2ServiceCategoryName + ":" + bank2ServiceName + "'.", e);
        }
        
        try {
            notifierInvoker = new ServiceInvoker(notifierServiceCategoryName, notifierServiceName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ServiceInvoker for '" + notifierServiceCategoryName + ":" + notifierServiceName + "'.", e);
        }
    }

    public void processLoanRequest(WebCustomer wCustomer) {

        Customer customer = getCustomer(wCustomer);
        //keep the customer in a file someplace for later use, if needed
        CustomerMasterFile.addCustomer(String.valueOf(customer.ssn), customer);

        //step 1 - send to credit agency for credit score if available

        int score=sendToCreditAgency(customer);

        //added a pause here to give the creditagency some time to reply
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //step 2 - check if score is acceptable
        
        if (score >= 7) {
	        //step 3a - send to Bank - async
	        System.out.println("sending to first Bank...");
	        sendToBank(bank1Invoker, customer, "b1");
	
	        System.out.println("sending to second Bank...");
	        sendToBank(bank2Invoker, customer, "b2");
        } else {
        	//step 3b - notify customer that credit is not acceptable
        	String invalidCredit="<insufficientCredit><customerUID>"+customer.ssn+"</customerUID>" +
        			"<ref>0</ref>"+
        			"<customerEmail>"+customer.email+"</customerEmail>" +
        					"<errorCode>3</errorCode></insufficientCredit>";
        	
            Message notifyMessage = MessageFactory.getInstance().getMessage(MessageType.JBOSS_XML);
            notifyMessage.getBody().add(invalidCredit);
    		
            try {
            	notifierInvoker.deliverAsync(notifyMessage);
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    private Customer getCustomer(WebCustomer wCustomer) {
        Customer customer = new Customer();
        customer.ssn = wCustomer.getSsn();
        customer.name = wCustomer.getName();
        customer.address = wCustomer.getAddress();
        customer.email = wCustomer.getEmail();
        customer.salary = wCustomer.getSalary();
        customer.loanAmount = wCustomer.getLoanAmount();
        customer.loanDuration = wCustomer.getLoanDuration();
        customer.creditScore = 0;

        return customer;
    }


    private void sendToBank(ServiceInvoker invoker, Customer customer, String ref) {
        Message message = MessageFactory.getInstance().getMessage(MessageType.JBOSS_XML);

        try {
            //create a Quote Request that the bank expects
            QuoteRequest quote = new QuoteRequest();
            quote.ssn = customer.ssn;
            quote.ref = ref;
            quote.amount = (int) customer.loanAmount;
            quote.creditScore = customer.creditScore;
            quote.creditHistoryLen = 0;    //not sure who added this one
            quote.term = customer.loanDuration;
            quote.customerEmail = customer.email;

            message.getBody().add(quote.getXML());
            invoker.deliverAsync(message);
            
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private int sendToCreditAgency(Customer customer) {
        Message message = MessageFactory.getInstance().getMessage(MessageType.JBOSS_XML);
        Message replyMessage;
        int score = 0;

        try {
            logger.debug("sending to credit agency");

            //set the customer details inside the message
            message.getBody().add(customer.getXML());
            
            logger.info("Sending request to creditAgency: " + message.getBody().get());

            replyMessage = serviceInvoker.deliverSync(message, 5000);
            if (replyMessage != null) {
                logger.info("received reply from creditAgency action: " + replyMessage.getBody().get());

                String xml =(String)replyMessage.getBody().get();
                
    			DocumentBuilderFactory fact=DocumentBuilderFactory.newInstance();
    			fact.setNamespaceAware(true);
    			
    			java.io.InputStream xmlstr=
    				new java.io.ByteArrayInputStream(xml.getBytes());

    			DocumentBuilder builder=fact.newDocumentBuilder();
    			org.w3c.dom.Document doc=builder.parse(xmlstr);
    			
    			org.w3c.dom.Element tree = doc.getDocumentElement();
        		
    			score = Integer.parseInt(tree.getAttribute("score"));
    			
    			logger.debug("Score from XML is: "+score);
            } else {
                logger.debug("reply not received from credit agency - setting a value of 5");
                score = 5;
            }
        } catch (Exception ex2) {
            logger.error("exception occured: " + ex2);
        }

        return score;
    }


}
