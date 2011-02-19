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

import java.io.File;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.xml.DOMConfigurator;
import org.sadun.util.polling.BasePollManager;
import org.sadun.util.polling.CycleEndEvent;
import org.sadun.util.polling.CycleStartEvent;
import org.sadun.util.polling.DirectoryLookupEndEvent;
import org.sadun.util.polling.DirectoryLookupStartEvent;
import org.sadun.util.polling.DirectoryPoller;
import org.sadun.util.polling.FileFoundEvent;
import org.sadun.util.polling.FileSetFoundEvent;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * The FlatFilePollManager monitors a directory specified in the configuration. When a file arrives it
 * tries to process the content assuming it contains one line with loan request information.
 * So for example the content of the file could read some like <br>23456890,3,4,20000,5<br>
 * The order of values needs to be SocialSecurityNumber, CreditScore, HistoryLength [month], LoanAmount [dollar],
 * and finally the LoanTerm [month]. The request will be processed and a BankQouteReply is generated. The
 * original file is moved to a 'processed' directory, and the BankQuoteReply is serialized to an 'outgoing' 
 * directory from the it can be picked up by the system that dropped the request.
 * 
 * @author kstam
 *
 */
class ManagerFlatFile extends BasePollManager 
{
	private Logger logger = Logger.getLogger(this.getClass());
	
	private static final String BANK_NAME        = "FileBasedBank";
	private static final BigDecimal RATE_PREMIUM = BigDecimal.valueOf(0.25);
	private static final int MAXLOANTERM         = 120;
	private static final String QUOTE_SUFFIX 	 = "quote";
	private String m_outputDir=null;
	
	public ManagerFlatFile(String outputDir) {
		m_outputDir = outputDir;
	}
	
	/**
	 * Handle to the cycle start event.
	 */
	public void cycleStarted(CycleStartEvent evt) {
		logger.log(Level.DEBUG, "Poller awakened "
				+ (evt.getPoller().getFilter() == null ? "(no filtering)" : evt
						.getPoller().getFilter().toString()));
	}
	/**
	 * Handle to the cycle end event.
	 */
	public void cycleEnded(CycleEndEvent evt) {
		logger.log(Level.DEBUG, "Poller going to sleep");
	}
	/**
	 * Handle to the directory lookup start event.
	 */
	public void directoryLookupStarted(DirectoryLookupStartEvent evt) {
		logger.log(Level.DEBUG, "Scanning " + evt.getDirectory());
	}
	/**
	 * Handle to the directory lookup end event.
	 */
	public void directoryLookupEnded(DirectoryLookupEndEvent evt) {
		logger.log(Level.DEBUG, "Finished scanning " + evt.getDirectory());
	}
	/**
	 * Handle to the file set found event. Currenty this is the only event that
	 * is actually used to proces the incoming loan quote requests. Each file
	 * is send to be processed.
	 */
	public void fileSetFound(FileSetFoundEvent evt) {
		File[] files = evt.getFiles();
		
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			processFile(file);
		}
	}
	/**
	 * Handle to the file found event.
	 */
	public void fileFound(FileFoundEvent evt) {
		logger.log(Level.DEBUG, evt);
		// Add artificial delay
	}
	/**
	 * Returns the name of this PollManager and a quick summary of what it does.
	 */
	public String toString() {
		return "FlatFilePollManager - a pollmanager which processed incoming -flat file based- loan requests";
	}
	/**
	 * Each file is processed assuming it contains one line with loan request information.
	 * So for example the content of the file could read some like <br>23456890,3,4,20000,5<br>
	 * The order of values needs to be SocialSecurityNumber, CreditScore, HistoryLength [month], LoanAmount [dollar],
	 * and finally the LoanTerm [month]. The BankQuoteRequest is processed and a BankQouteReply is generated. The
	 * original file is moved to a processed directory, and the BankQuoteReply is serialized to an 'outgoing' 
	 * directory from the it can be picked up by the system that dropped the request.
	 * 
	 * @param file - incoming flat file with loan quote request info.
	 */
	protected void processFile(File file) 
	{
		BankQuoteReply bankQuoteReply=new BankQuoteReply();
		bankQuoteReply.setQuoteId(BANK_NAME);
		int errorCode=7;
		String message=null;
		logger.log(Level.INFO, "Found " + file.getAbsolutePath() );
		
		try {
            //Read the content of the file into a String
			message=FileUtil.readTextFile(file);
			
			Bank fileBasedBank = new Bank(BANK_NAME, RATE_PREMIUM, MAXLOANTERM);
			
			BankQuoteRequest bankQuoteRequest = fileBasedBank.getQuoteFromXML(message);
	      
			bankQuoteReply = fileBasedBank.processMessage(bankQuoteRequest);	
		} catch (Throwable e) {
			//Package up the error, so it can be processed
			logger.log(Level.ERROR, e.getMessage());
			bankQuoteReply.setErrorCode(errorCode);
		}
		
		try {
			//Create the outgoing response file
			File outgoingDir  = new File(m_outputDir);
			if (!outgoingDir.exists()) {
				outgoingDir.mkdir();
			}
			File outgoingFile = new File(outgoingDir + "/" + file.getName()+"."+QUOTE_SUFFIX);
			
			logger.log(Level.INFO, "Creating outgoing file " + outgoingFile.getAbsolutePath());
			outgoingFile.createNewFile();
			
			XStream xstream = new XStream(new DomDriver());
			xstream.alias("quote", BankQuoteReply.class);
			
			String reply = xstream.toXML(bankQuoteReply);
						
			logger.log(Level.INFO, "Writing reply " + reply);
			FileUtil.writeTextFile(outgoingFile, reply);

			// Delete file
			file.delete();
			
		} catch (Exception e) {
			logger.log(Level.ERROR, e.getMessage(), e);
		}
	}
	
	public static void main(String[] args)
	{
		DOMConfigurator.configure("log4j.xml");
		String monitoredDir = "C:/tmp/input";
		String outputDir = "C:/tmp/output";
		if (args!=null && args.length > 0) {
			monitoredDir=args[0];
			if (args.length > 1) {
				outputDir=args[1];
			}
		}
System.out.println("MONITOR="+monitoredDir+" OUTPUT="+outputDir);		
		File monitoredDirectory = new File(monitoredDir);
		if (!monitoredDirectory.exists() || !monitoredDirectory.isDirectory()){
			System.err.println("Invalid directory " + monitoredDirectory.getAbsolutePath());
			System.out.println("Trying to create the directory.");
			try {
				monitoredDirectory.mkdir();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		DirectoryPoller poller = new DirectoryPoller(monitoredDirectory);
		poller.addPollManager(new ManagerFlatFile(outputDir));
		poller.start();
	}
	

}