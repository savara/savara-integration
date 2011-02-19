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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import junit.framework.JUnit4TestAdapter;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.junit.Test;

public class FlatFileProcessorTest {

	private Logger logger = Logger.getLogger(this.getClass());
	private static String FLAT_FILE = "FlatFileProcessorTest.txt";
	/**
	 * Tests the processing of flat file containing a loan quote request. First
	 * we try to find a temporary directory. When this succeeds we drop a
	 * loan request file in there and try to process it.
	 *
	 */
	@Test public void processRequest()
	{
		File writableDir=null;
		logger.log(Level.INFO, "Check to see if I can write to /tmp");
		File tmpDir = new File("/tmp");
		if (tmpDir.exists() && tmpDir.canWrite()) {
			writableDir=tmpDir;
		} else {
			File tempDir = new File("C:/temp");
			if (tempDir.exists() && tempDir.canWrite()) {
				writableDir=tempDir;
			} else {
				logger.log(Level.ERROR, "Could not find either /tmp or C:\\Temp for a temporary " 
						+ "writing space.");
			    assertTrue(false);
			}
		}
//		We found a temp space so now we can drop in a load request file.
		try {
			logger.log(Level.INFO, "Creating " + FLAT_FILE + " file in " + writableDir.getAbsolutePath());
			File loanRequestFile = new File(writableDir.getAbsolutePath() + "/"+ FLAT_FILE);
			if (loanRequestFile.exists()) {
				loanRequestFile.delete();
			}
			loanRequestFile.createNewFile();
			String str="23456890,3,4,20000,5,unique";
			FileUtil.writeTextFile(loanRequestFile, str);
			
			logger.log(Level.INFO, "Processing FlatFileProcessorTest.txt");
			ManagerFlatFile pollManager = new ManagerFlatFile(null);
			pollManager.processFile(loanRequestFile);
			
			//Check to see if the file is now moved.
			assertFalse(loanRequestFile.exists());
			
			File replyToFile = null;//new File(writableDir.getAbsolutePath() + "/" 
			//	+ ManagerFlatFile.OUTGOING_DIR + "/" + ManagerFlatFile.PREFIX
			//	+ FLAT_FILE);
			logger.log(Level.INFO, "Check to see if " + replyToFile.getAbsolutePath()
					+ " exists");
			assertTrue(replyToFile.exists());
			logger.log(Level.INFO, "OK");
			
			//Checking the content of the reply
			logger.log(Level.INFO, "Check the reply, should be ('8.29,FileBasedBank-0,0,unique'");
			assertTrue("8.29,FileBasedBank-0,0,unique".equals(FileUtil.readTextFile(replyToFile)));
			logger.log(Level.INFO, "OK");
			//Cleaning up
			replyToFile.delete();
			
		} catch (Throwable e) {
			logger.log(Level.ERROR,e.getMessage(),e);
			assertTrue(false);
		}
	}
	
	public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(FlatFileProcessorTest.class);
    }
}
