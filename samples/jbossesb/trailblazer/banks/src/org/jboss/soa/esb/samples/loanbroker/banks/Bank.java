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

package org.jboss.soa.esb.samples.loanbroker.banks;

import java.math.BigDecimal;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * This Bank is pretty well focussed on doing one thing, which is giving out loans. On the 
 * reception of a BankQuoteRequest it will compute the interest rate it will return
 * in the BankQuoteReply.
 * 
 * Each bank instance can be customized using the parameters bankName, RatePremium and MaxLoanTerm.
 * 
 * @author kstam
 *
 */
public class Bank 
{
	private Logger logger = Logger.getLogger(this.getClass());
	
	private static final BigDecimal PRIME_RATE            = BigDecimal.valueOf(8.0);
	private static final BigDecimal DEFAULT_RATE_PREMIUM  = BigDecimal.valueOf(0.5);
	private static final int        DEFAULT_MAX_LOAN_TERM = 30;
	private static final String     DEFAULT_BANK_NAME     = "DefaultBank";
	private static int _quoteCounter                      = 0;
	
	//Default values for bank
	private String bankName                               = DEFAULT_BANK_NAME;
	private BigDecimal ratePremium                        = DEFAULT_RATE_PREMIUM;
	private int maxLoanTerm                               = DEFAULT_MAX_LOAN_TERM;

	/** 
	 * The Bank's constructor.
	 * 
	 * @param bankName    - the name of the bank
	 * @param ratePremium - the premium it charges for the loan
	 * @param maxLoanTerm - the maximum loan term
	 */
	public Bank(String bankName, BigDecimal ratePremium, int maxLoanTerm) {
		super();
		if (bankName!=null) {
			this.bankName=bankName;
		}
		if (ratePremium!=null) {
			this.ratePremium=ratePremium;
		}
		if (maxLoanTerm > 0) {
			this.maxLoanTerm=maxLoanTerm;
		}
	}
	/**
	 * The bank computes the interest rate based on configuration parameters of the bank and
	 * the information in the {@link BankQuoteRequest.
	 * @param bankQuoteRequest - the bank quote request containing requests parameters
	 * @return bankQuoteReply  - the bank quote replay contaning the interest rate
	 */
	private BankQuoteReply computeBankQuoteReply(BankQuoteRequest bankQuoteRequest)
	{
		BankQuoteReply bankQuoteReply = new BankQuoteReply();
		if (bankQuoteRequest.loanTerm <= maxLoanTerm) {
			BigDecimal interestRate=BigDecimal.valueOf((bankQuoteRequest.loanTerm/12.0)/10.0);
			interestRate=PRIME_RATE.add(ratePremium).add(interestRate);
			bankQuoteReply.setInterestRate(interestRate.setScale(2, BigDecimal.ROUND_HALF_UP));
			bankQuoteReply.setErrorCode(0);
		} else {
			bankQuoteReply.setInterestRate(BigDecimal.valueOf(0.00).setScale(2, BigDecimal.ROUND_HALF_UP));
			bankQuoteReply.setErrorCode(1);
		}
		String quoteId=bankName + "-" + _quoteCounter++;
		bankQuoteReply.setQuoteId(quoteId);
		bankQuoteReply.setRef(bankQuoteRequest.getRef());
		bankQuoteReply.setCustomerUID(bankQuoteRequest.getCustomerUID());
		bankQuoteReply.setCustomerEmail(bankQuoteRequest.customerEmail);
		return bankQuoteReply;
	}
	/**
	 * Processes the Loan Quote request and returns the banks reply.
	 * 
	 * @param bankQuoteRequest - the bank quote request
	 * @return bankQuoteReply  - the bank quote reply
	 */
	protected BankQuoteReply processMessage(BankQuoteRequest bankQuoteRequest)
	{
		logger.log(Level.INFO, "Bank '" + bankName + "' received a request for SSN=" 
				+ bankQuoteRequest.getSsn()
				+ " for $" + bankQuoteRequest.loanAmount 
				+ " over " + bankQuoteRequest.loanTerm + " months.");
		logger.log(Level.DEBUG, bankQuoteRequest);
		
		BankQuoteReply bankQuoteReply = computeBankQuoteReply(bankQuoteRequest);
		logger.log(Level.INFO, "Bank '" + bankName + " offers SSN=" 
				+ bankQuoteRequest.getSsn() + " " + bankQuoteReply);
		
		return bankQuoteReply;
	}

	protected BankQuoteRequest getQuoteFromXML(String xml) {
		BankQuoteRequest quote = new BankQuoteRequest();
		
		try {
			DocumentBuilderFactory fact=DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(true);
			
			java.io.InputStream xmlstr=
				new java.io.ByteArrayInputStream(xml.getBytes());

			DocumentBuilder builder=fact.newDocumentBuilder();
			org.w3c.dom.Document doc=builder.parse(xmlstr);
			
			org.w3c.dom.Element tree = doc.getDocumentElement();
			
			quote.setSsn(Integer.parseInt(tree.getAttribute("ssn")));
			quote.setRef(tree.getAttribute("ref"));
			quote.setCreditScore(Integer.parseInt(tree.getAttribute("creditScore")));
			quote.setHistoryLength(Integer.parseInt(tree.getAttribute("creditHistoryLen")));
			quote.setLoanAmount(Integer.parseInt(tree.getAttribute("amount")));
			quote.setLoanTerm(Integer.parseInt(tree.getAttribute("term")));
			quote.setCustomerUID(tree.getAttribute("customerUID"));
			quote.customerEmail=tree.getAttribute("customerEmail");
			
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return quote;
	}
}
