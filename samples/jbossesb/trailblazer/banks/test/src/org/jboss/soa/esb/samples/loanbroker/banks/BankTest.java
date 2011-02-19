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

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import junit.framework.JUnit4TestAdapter;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.junit.Test;

public class BankTest {

	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Tests the computation of the interestRate for the TestBank.
	 *
	 */
	@Test public void processLoanRequest(){
		
		//Building a BankQuoteRequest
		BankQuoteRequest bankQuoteRequest = new BankQuoteRequest();
		bankQuoteRequest.setSsn(123456890);
		bankQuoteRequest.setCreditScore(3);
		bankQuoteRequest.setHistoryLength(4);
		bankQuoteRequest.setLoanTerm(72);
		bankQuoteRequest.setLoanAmount(20000);
		bankQuoteRequest.setCustomerUID("unique");
		logger.log(Level.INFO, bankQuoteRequest);
		
		//Getting a Bank Instance
		String bankName        = "TestBank";
		BigDecimal ratePrimium = BigDecimal.valueOf(0.25);
		int maxLoanTerm        = 120;
		Bank bank = new Bank(bankName, ratePrimium, maxLoanTerm);
		
		//Getting a quote from this bank
		BankQuoteReply bankQuoteReply = bank.processMessage(bankQuoteRequest);
		logger.log(Level.INFO, bankQuoteReply);
		
		//Checking for validity
		BigDecimal expectedInterestRate = BigDecimal.valueOf(8.85);
		assertTrue(bankQuoteReply.getInterestRate().equals(expectedInterestRate));
	}
	
	public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BankTest.class);
    }
}
