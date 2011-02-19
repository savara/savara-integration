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
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

public class BankQuoteReplyTest {

	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Testing the XML layout of a BankQuoteReply
	 */
	@Test public void obtainExampleXML(){
		BankQuoteReply bankQuoteReply = new BankQuoteReply();
		bankQuoteReply.setErrorCode(0);
		bankQuoteReply.setCustomerUID("unique");
		bankQuoteReply.setInterestRate(BigDecimal.valueOf(11.7));
		bankQuoteReply.setQuoteId("JBBANK_1");
		
		XStream xstream = new XStream();
		String bankQuoteReplyXml=xstream.toXML(bankQuoteReply);
		logger.info("xml=\n" + bankQuoteReplyXml);
		String expectedXml = 
			  "<org.jboss.soa.esb.samples.loanbroker.banks.BankQuoteReply>\n"
			+ "  <interestRate>11.7</interestRate>\n"
            + "  <quoteId>JBBANK_1</quoteId>\n"
            + "  <errorCode>0</errorCode>\n"
            + "  <customerUID>unique</customerUID>\n"
            + "</org.jboss.soa.esb.samples.loanbroker.banks.BankQuoteReply>";
		assertTrue(expectedXml.equals(bankQuoteReplyXml));
	}
	
	public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BankQuoteReplyTest.class);
    }
}
