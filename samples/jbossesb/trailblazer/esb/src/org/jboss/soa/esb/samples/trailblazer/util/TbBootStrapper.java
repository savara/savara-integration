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
 * @author JBoss Inc.
 */
package org.jboss.soa.esb.samples.trailblazer.util;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.jboss.soa.esb.persistence.manager.ConnectionManager;
import org.jboss.soa.esb.common.Configuration;
import org.jboss.soa.esb.listeners.StandAloneBootStrapper;
import org.jboss.soa.esb.persistence.manager.ConnectionManagerFactory;
import org.jboss.soa.esb.testutils.FileUtil;
//import org.jboss.soa.esb.testutils.HsqldbUtil;
import org.jboss.soa.esb.testutils.TestEnvironmentUtil;

public class TbBootStrapper extends StandAloneBootStrapper {
	
	protected static Logger _logger = Logger.getLogger(TbBootStrapper.class);
	
	public static void main(String[] args) throws Exception
	{
		Exception eT = null;
		if (args.length < 1)
		{
			eT = new Exception ("No configuration file specified - Ending immediately");
			_logger.fatal(eT);
			throw eT;
		} else {
			_logger.info("Starting the trailblazer with arg=" + args[0]);
		}
		TbBootStrapper boot = null;

		String configName = args[0];
		long lSecondsToRun = 365 * 24 * 3600;   // run for 1 year (is it enough ?)
		if (args.length > 1)
			try 
		{ 
			lSecondsToRun = Long.parseLong(args[1]);
		}
		catch (Exception e)
		{
			_logger.fatal(e);
			throw e;
		}
		try
		{
			boot = new TbBootStrapper(configName);
			if (lSecondsToRun < 5)
				lSecondsToRun = 5;
			long lRunTo = System.currentTimeMillis()+1000*lSecondsToRun;

			while (System.currentTimeMillis() < lRunTo)
				try { Thread.sleep(1000); }
				catch (InterruptedException e) { break; }
		}
		finally
		{
			if (null!=boot)
				boot.requestEnd();
		}
	}
	
	private TbBootStrapper() throws Exception {super(null);}
	
	public TbBootStrapper (String configName) throws Exception
	{
		super(configName, null);
	}
	
	public TbBootStrapper (String configName, String validationFileName) throws Exception 
	{
		super(configName, validationFileName);		
	}

	/*
	 * Setup HSQLDB for the Registry and the MessageStore
	 */
	protected void runBefore(){	
		//System.setProperty("com.arjuna.common.util.propertyservice.verbosePropertyManager", "on");
		String baseDir="";
		String productDir = "../../";
		if (TestEnvironmentUtil.getUserDir("trailblazer").equals("trailblazer/")) {
			baseDir = "product/samples/trailblazer/";
			productDir = "product/";
		}
		DOMConfigurator.configure(baseDir + "log4j.xml");
		String driver = Configuration.getStoreDriver();
		System.out.println("Driver=" + driver);
		if ("org.hsqldb.jdbcDriver".equals(driver)) {
			
			//start hsqldb
			try {
			HsqldbUtil.startHsqldb(productDir + "build/hsqldb", "jbossesb");
			String database = "hsqldb";	
			
			//message store db
			String sqlDir = productDir + "install/message-store/sql/" + database + "/";
			String sqlCreateCmd    = TestEnvironmentUtil.readTextFile(new File(sqlDir + "create_database.sql"));
			String sqlDropCmd      = TestEnvironmentUtil.readTextFile(new File(sqlDir + "drop_database.sql"));		
			
			ConnectionManager mgr = ConnectionManagerFactory.getConnectionManager();
			Connection con = mgr.getConnection();
			
			Statement stmnt = con.createStatement();
			System.out.println("Dropping the message store schema if exists...");
			stmnt.execute(sqlDropCmd);
			System.out.println("Creating the message store schema...");
			stmnt.execute(sqlCreateCmd);
			
			//registry DB
			sqlDir = productDir + "install/jUDDI-registry/sql/" + database + "/";
			System.out.println("Dropping the registry schema if exists...");
			sqlDropCmd      = TestEnvironmentUtil.readTextFile(new File(sqlDir + "drop_database.sql")).replaceAll("\\$\\{prefix}", "");
			stmnt.execute(sqlDropCmd);
			System.out.println("creating the registry schema...");
            String resource = "juddi-sql/" + database + "/create_database.sql";
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            sqlCreateCmd    = FileUtil.readStream(is).trim().replaceAll("\\$\\{prefix}", "");
			stmnt.execute(sqlCreateCmd);
			System.out.println("inserting registry publishers...");
			String sqlInsertPubCmd = TestEnvironmentUtil.readTextFile(new File(sqlDir + "import.sql")).trim().replaceAll("\\$\\{prefix}", "");
			stmnt.execute(sqlInsertPubCmd);
			stmnt.close();
			con.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		}
		
	/*
	 * Shuts down the HSQLDB instance
	 */
		protected void runAfter()
		{
			try { Thread.sleep(5000); }
			catch (InterruptedException e) {}
			
			//shutdown message store if using hsqldb
			if ("org.hsqldb.jdbcDriver".equals(Configuration.getStoreDriver())) {
				try {
					HsqldbUtil.stopHsqldb(Configuration.getStoreUrl(),
							Configuration.getStoreUser(),Configuration.getStorePwd() );
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
		}

	

}
