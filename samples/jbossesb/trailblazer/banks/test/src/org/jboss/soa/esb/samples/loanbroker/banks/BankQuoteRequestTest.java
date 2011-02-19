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
import junit.framework.JUnit4TestAdapter;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

public class BankQuoteRequestTest {

	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Testing the XML layout of a BankQuoteRequest
	 */
	@Test public void obtainExampleXML(){
		BankQuoteRequest bankQuoteRequest = new BankQuoteRequest();
		bankQuoteRequest.setSsn(123456890);
		bankQuoteRequest.setCreditScore(3);
		bankQuoteRequest.setHistoryLength(4);
		bankQuoteRequest.setLoanTerm(5);
		bankQuoteRequest.setLoanAmount(20000);
		bankQuoteRequest.setCustomerUID("unique");
		
		XStream xstream = new XStream();
		String bankQuoteRequestXml=xstream.toXML(bankQuoteRequest);
		logger.info("xml=\n" + bankQuoteRequestXml);
		String expectedXml = 
			  "<org.jboss.soa.esb.samples.loanbroker.banks.BankQuoteRequest>\n"
			+ "  <ssn>123456890</ssn>\n"
			+ "  <creditScore>3</creditScore>\n"
			+ "  <historyLength>4</historyLength>\n"
			+ "  <loanAmount>20000</loanAmount>\n"
			+ "  <loanTerm>5</loanTerm>\n"
		    + "  <customerUID>unique</customerUID>\n"
			+ "</org.jboss.soa.esb.samples.loanbroker.banks.BankQuoteRequest>";
		assertTrue(expectedXml.equals(bankQuoteRequestXml));
	}
	
	public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BankQuoteRequestTest.class);
    }
}
