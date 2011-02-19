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
import java.math.BigDecimal;

public class BankQuoteReply implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private BigDecimal interestRate;
	private String quoteId;
	private String ref;
	private int errorCode;
	private String customerUID;
	private String customerEmail;
	private String customerSSN;
	private String customerName;	
	private String customerAddress;	
	private String customerSalary;
	private String loanAmount;
	private String loanDuration;
	
	/**
	 * Gets the SerialVersionUID.
	 * @return serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	/**
	 * Gets the errorCode.
	 * 	<li>0 - success
	 *  <li>1 - loan term exceeds the maximum available loan term
	 *  <li>2 - invalid SSN
	 *  <li>3 - invalid credit score
	 *  <li>4 - invalid credit history length
	 *  <li>5 - invalid loan term
	 *  <li>6 - invalid message
	 *  <li>7 - invalid customerUID
	 * @return errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}
	/**
	 * Sets the errorCode.
	 *  <li>0 - success
	 *  <li>1 - loan term exceeds the maximum available loan term
	 *  <li>2 - invalid SSN
	 *  <li>3 - invalid credit score
	 *  <li>4 - invalid credit history length
	 *  <li>5 - invalid loan term
	 * @param errorCode
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * Gets the quoted interest rate.
	 * @return interestRate
	 */
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	/**
	 * Sets the quoted interest rate.
	 * @param interestRate
	 */
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	/**
	 * Gets the Quote ID.
	 * @return quoteId
	 */
	public String getQuoteId() {
		return quoteId;
	}
	/**
	 * Sets the quoteId.
	 * @param quoteId
	 */
	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
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
			+ "interestRate=" + interestRate
			+ ", quoteId=" + quoteId
			+ ", errorCode=" + errorCode 
			+ ", customerUID=" + customerUID + "]";
	}
	public String getCustomerUID() {
		return customerUID;
	}
	public void setCustomerUID(String customerUID) {
		this.customerUID = customerUID;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public String getCustomerEmail() {
		return customerEmail;
	}
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerSalary() {
		return customerSalary;
	}
	public void setCustomerSalary(String customerSalary) {
		this.customerSalary = customerSalary;
	}
	public String getCustomerSSN() {
		return customerSSN;
	}
	public void setCustomerSSN(String customerSSN) {
		this.customerSSN = customerSSN;
	}
	public String getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}
	public String getLoanDuration() {
		return loanDuration;
	}
	public void setLoanDuration(String loanDuration) {
		this.loanDuration = loanDuration;
	}
}
