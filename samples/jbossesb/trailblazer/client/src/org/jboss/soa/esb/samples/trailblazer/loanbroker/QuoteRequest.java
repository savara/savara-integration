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
package org.jboss.soa.esb.samples.trailblazer.loanbroker;

import java.io.Serializable;


public class QuoteRequest implements Serializable 
{
	private static final long serialVersionUID = 1L;
	public int ssn;
	public String ref;
	public int creditScore;
	public int creditHistoryLen;
	public int amount;
	public int term;
	public String customerUniqueId;
	public String customerEmail;
	
	/**
	 * Gets for the credit score.
	 * @return credit score
	 */
	public int getCreditScore() {
		return creditScore;
	}
	/**
	 * Sets the credit score.
	 * @param creditScore
	 */
	public void setCreditScore(int creditScore) {
		this.creditScore = creditScore;
	}
	/**
	 * Gets the credit history length (in months).
	 * @return credit history length
	 */
	public int getCreditHistoryLen() {
		return creditHistoryLen;
	}
	/**
	 * Sets the credit history length (in months).
	 * @param creditHistoryLen
	 */
	public void setCreditHistoryLen(int historyLength) {
		this.creditHistoryLen = historyLength;
	}
	/**
	 * Gets the requested loan amount (in dollar).
	 * @return the loan amount
	 */
	public int getAmount() {
		return amount;
	}
	/** 
	 * Sets the loan amount.
	 * @param amount - the loan amount (in dollar).
	 */
	public void setAmount(int loanAmount) {
		this.amount = loanAmount;
	}
	/**
	 * Gets the duration of the loan, the term, (in months).
	 * @return term
	 */
	public int getTerm() {
		return term;
	}
	/**
	 * Sets the term (duration) of the loan (in months)
	 * @param term
	 */
	public void setTerm(int loanTerm) {
		this.term = loanTerm;
	}
	/** 
	 * Sets the Social Security Number (SSN).
	 * @return ssn
	 */
	public int getSsn() {
		return ssn;
	}
	/**
	 * Sets the quote reference.
	 * @param ref
	 */
	public void setRef(String ref) {
		this.ref = ref;
	}
	/** 
	 * Gets the quote reference.
	 * @return ref
	 */
	public String getRef() {
		return ref;
	}
	/**
	 * Sets the Social Security Number (SSN).
	 * @param ssn
	 */
	public void setSsn(int ssn) {
		this.ssn = ssn;
	}
	/**
	 * Returns a human readable string representation of this object.
	 * @return toString
	 */
	public String toString() {
		return this.getClass().getSimpleName() + "=["
			+ "ssn=" + ssn
			+ "ref=" + ref
		    + ", creditScore=" + creditScore
			+ ", creditHistoryLen=" + creditHistoryLen
			+ ", amount=" + amount
			+ ", term=" + term
			+ ", customerUniqueId="+ customerUniqueId + "]";	
	}
	
	public String getXML() {
		StringBuffer ret=new StringBuffer();
		
		ret.append("<quoteRequest ssn=\""+ssn+"\" ");
		ret.append("ref=\""+ref+"\" ");
		ret.append("creditScore=\""+creditScore+"\" ");
		ret.append("creditHistoryLen=\""+creditHistoryLen+"\" ");
		ret.append("amount=\""+amount+"\" ");
		ret.append("term=\""+term+"\" ");
		ret.append("customerUID=\""+ssn+"\" ");
		ret.append("customerEmail=\""+customerEmail+"\" />");
		
		return(ret.toString());
	}
	
	public String getCustomerUniqueId() {
		return customerUniqueId;
	}
	public void setCustomerUniqueId(String customerUID) {
		this.customerUniqueId = customerUID;
	}
}
