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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/*
 * Utility class used to persist the customer quote requests to a serialized Hash list on the file system
 * Key is the customer's SSN #
 */

public class CustomerMasterFile implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static Map<String, Customer> customers;
	private static final String TMP_DIR	= System.getProperty("java.io.tmpdir","/tmp");
	private static final File FILE = new File(TMP_DIR, "customers");
	
	static {
		getCustomers();
	}
	
	public static synchronized void writeCustomers() {
		try {
			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(FILE));
			stream.writeObject(customers);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized void  getCustomers() {
		
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE));
			customers = (Map<String, Customer>)ois.readObject();
		} catch (Exception e) {
			customers = new HashMap<String, Customer>();
		}	
		
	}
	
	public static Customer getCustomer(String id) {
		return customers.get(id);
	}
	
	public static void addCustomer(String id, Customer customer) {
		customers.put(id, customer);
		writeCustomers();
	}
	

}
