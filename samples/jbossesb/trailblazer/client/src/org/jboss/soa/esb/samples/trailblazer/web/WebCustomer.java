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


/*
 * a simple web Customer to use in the web services call and not expose the internal esb Customer object
 */
package org.jboss.soa.esb.samples.trailblazer.web;

import java.io.Serializable;

/*
 * WebCustomer represents the data captured in the client JSP page
 */

@SuppressWarnings("serial")
public class WebCustomer implements Serializable
{
    private java.lang.String address;

    private java.lang.String email;

    private java.lang.String employerName;

    private double loanAmount;

    private int loanDuration;

    private java.lang.String name;

    private double salary;

    private int ssn;

    public WebCustomer() {
    }

    public WebCustomer(
           java.lang.String address,
           java.lang.String email,
           java.lang.String employerName,
           double loanAmount,
           int loanDuration,
           java.lang.String name,
           double salary,
           int ssn) {
           this.address = address;
           this.email = email;
           this.employerName = employerName;
           this.loanAmount = loanAmount;
           this.loanDuration = loanDuration;
           this.name = name;
           this.salary = salary;
           this.ssn = ssn;
    }
	public String toString() {
		String ret = "-- WebCustomer details -- \n";
		ret += "name: " + name + "\n";
		ret += "address: " + address + "\n";
		ret += "employerName: " + employerName + "\n";
		ret += "salary: " + salary + "\n";
		ret += "loanAmount: " + loanAmount + "\n";
		ret += "loanDuration: " + loanDuration + "\n";
		ret += "ssn: " + ssn + "\n";
		ret += "email: " + email + "\n";
		ret += "--------------------------";
		
		return ret;
	}

    /**
     * Gets the address value for this WebCustomer.
     * 
     * @return address
     */
    public java.lang.String getAddress() {
        return address;
    }


    /**
     * Sets the address value for this WebCustomer.
     * 
     * @param address
     */
    public void setAddress(java.lang.String address) {
        this.address = address;
    }


    /**
     * Gets the email value for this WebCustomer.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this WebCustomer.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the employerName value for this WebCustomer.
     * 
     * @return employerName
     */
    public java.lang.String getEmployerName() {
        return employerName;
    }


    /**
     * Sets the employerName value for this WebCustomer.
     * 
     * @param employerName
     */
    public void setEmployerName(java.lang.String employerName) {
        this.employerName = employerName;
    }


    /**
     * Gets the loanAmount value for this WebCustomer.
     * 
     * @return loanAmount
     */
    public double getLoanAmount() {
        return loanAmount;
    }


    /**
     * Sets the loanAmount value for this WebCustomer.
     * 
     * @param loanAmount
     */
    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }


    /**
     * Gets the loanDuration value for this WebCustomer.
     * 
     * @return loanDuration
     */
    public int getLoanDuration() {
        return loanDuration;
    }


    /**
     * Sets the loanDuration value for this WebCustomer.
     * 
     * @param loanDuration
     */
    public void setLoanDuration(int loanDuration) {
        this.loanDuration = loanDuration;
    }


    /**
     * Gets the name value for this WebCustomer.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this WebCustomer.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the salary value for this WebCustomer.
     * 
     * @return salary
     */
    public double getSalary() {
        return salary;
    }


    /**
     * Sets the salary value for this WebCustomer.
     * 
     * @param salary
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }


    /**
     * Gets the ssn value for this WebCustomer.
     * 
     * @return ssn
     */
    public int getSsn() {
        return ssn;
    }


    /**
     * Sets the ssn value for this WebCustomer.
     * 
     * @param ssn
     */
    public void setSsn(int ssn) {
        this.ssn = ssn;
    }
	
}
