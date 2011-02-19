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

import java.io.Serializable;


public class BankQuoteRequest implements Serializable 
{
	private static final long serialVersionUID = 1L;
	public int ssn;
	public String ref;
	public int creditScore;
	public int historyLength;
	public int loanAmount;
	public int loanTerm;
	public String customerUID;
	public String customerEmail;
	public String customerSSN;
	public String customerName;	
	public String customerAddress;	
	public String customerSalary;	
	public String loanDuration;
	
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
	public int getHistoryLength() {
		return historyLength;
	}
	/**
	 * Sets the credit history length (in months).
	 * @param historyLength
	 */
	public void setHistoryLength(int historyLength) {
		this.historyLength = historyLength;
	}
	/**
	 * Gets the requested loan amount (in dollar).
	 * @return the loan amount
	 */
	public int getLoanAmount() {
		return loanAmount;
	}
	/** 
	 * Sets the loan amount.
	 * @param loanAmount - the loan amount (in dollar).
	 */
	public void setLoanAmount(int loanAmount) {
		this.loanAmount = loanAmount;
	}
	/**
	 * Gets the duration of the loan, the term, (in months).
	 * @return loanTerm
	 */
	public int getLoanTerm() {
		return loanTerm;
	}
	/**
	 * Sets the term (duration) of the loan (in months)
	 * @param loanTerm
	 */
	public void setLoanTerm(int loanTerm) {
		this.loanTerm = loanTerm;
	}
	/** 
	 * Sets the Social Security Number (SSN).
	 * @return ssn
	 */
	public int getSsn() {
		return ssn;
	}
	/**
	 * Sets the Social Security Number (SSN).
	 * @param ssn
	 */
	public void setSsn(int ssn) {
		this.ssn = ssn;
	}
	/**
	 * Gets the ref.
	 * @return ref
	 */
	public String getRef() {
		return ref;
	}
	/**
	 * Sets the ref.
	 * @param ref
	 */
	public void setRef(String ref) {
		this.ref = ref;
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
			+ ", historyLength=" + historyLength
			+ ", loanAmount=" + loanAmount
			+ ", loanTerm=" + loanTerm
			+ ", customerUID="+ customerUID + "]";	
	}
	
	public String getCustomerUID() {
		return customerUID;
	}
	public void setCustomerUID(String customerUID) {
		this.customerUID = customerUID;
	}
}
