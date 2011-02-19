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

import java.io.Serializable;

public class Customer implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public	String	name=""	,address=""	,employerName="";
	public	double	salary=0.0	,loanAmount=0.0;
	public	int		loanDuration=0;	
	public int		ssn=0;
	public String	email="";
	public int creditScore=0;
	public String quoteID, rateOffered, quoteCode;
	
	public String getCSV() {
		String buff = (name+","+ssn+","+address+","+employerName+","+salary+","
				+loanAmount+","+loanDuration+","+email+","+creditScore);
				
		return buff;
	}

	public String getXML() {
		StringBuffer ret=new StringBuffer();
		
		ret.append("<creditCheck name=\""+name+"\" ");
		ret.append("ssn=\""+ssn+"\" ");
		ret.append("address=\""+address+"\" ");
		ret.append("employerName=\""+employerName+"\" ");
		ret.append("salary=\""+salary+"\" ");
		ret.append("loanAmount=\""+loanAmount+"\" ");
		ret.append("loanDuration=\""+loanDuration+"\" ");
		ret.append("email=\""+email+"\" ");
		ret.append("creditScore=\""+creditScore+"\" />");
		
		return(ret.toString());
	}
}
