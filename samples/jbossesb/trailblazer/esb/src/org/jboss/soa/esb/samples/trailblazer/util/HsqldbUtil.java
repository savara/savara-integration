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

import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.hsqldb.Server;
/**
 * Utility to start and stop a hsql Database.
 * 
 * @author <a href="mailto:kurt.stam@jboss.com">Kurt Stam</a>
 *
 */
public class HsqldbUtil 
{
	final private static String THREAD_NAME = "hypersonic-unittest";
	
	private static Logger _logger = Logger.getLogger(HsqldbUtil.class);
	/**
	 * Starts the hsql database in it's own thread. 
	 * Don't forget to shut it down when you're done.
	 * 
	 * @param databaseFile - i.e. build/hsqltestdb
	 * @param databaseName - i.e. jbossesb
	 * @throws Exception
	 */
	public static void startHsqldb(final String databaseFile,
			final String databaseName) throws Exception 
	{
		// Start DB in new thread, or else it will block us
		Thread serverThread = new Thread(THREAD_NAME) {
			public void run() {
				try {
					// Create startup arguments
					String[] args = new String[4];
					args[0] = "-database.0";
					args[1] = databaseFile;
					args[2] = "-dbname.0";
					args[3] = databaseName;
					
					_logger.info("creating db from this script: " + databaseFile);

					// Start server
					Server.main(args);
				} catch (Exception e) {
					e.printStackTrace();
				}
//				log.error("Failed to start database", e);
			}
		};
		serverThread.run();
	}
	/**
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	public static void stopHsqldb(String url, String username, String password) throws Exception {
		java.sql.Connection connection = DriverManager.getConnection(
				url, username, password);
		Statement statement = connection.createStatement();
		String shutdownCommand = "SHUTDOWN COMPACT";
		statement.executeQuery(shutdownCommand);
	}

	
}
