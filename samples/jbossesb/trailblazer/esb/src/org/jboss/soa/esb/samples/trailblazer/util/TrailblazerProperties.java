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
 * (C) 2005-2006, JBoss Inc.
 */
package org.jboss.soa.esb.samples.trailblazer.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jboss.soa.esb.util.ClassUtil;

/**
 * Trilablazer Properties.
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class TrailblazerProperties extends Properties {

    private static final long serialVersionUID = 1L;
    public static final String CONFIG_FILE = "trailblazer.properties";
    private static Logger logger = Logger.getLogger(TrailblazerProperties.class);

    public TrailblazerProperties() {
        InputStream config = ClassUtil.getResourceAsStream("/" + CONFIG_FILE, TrailblazerProperties.class);

        if(config == null) {
            logger.error("Failed to locate '" + CONFIG_FILE + "' in the root of the classpath.");
            return;
        }

        try {
            load(config);
        } catch (IOException e) {
            logger.error("Error reading '" + CONFIG_FILE + "'.", e);
        } finally {
            try {
                config.close();
            } catch (IOException e) {
                logger.warn("Failed to close stream to '" + CONFIG_FILE + "'.");
            }
        }
    }
}
