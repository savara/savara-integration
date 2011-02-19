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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**
 * Utility class for reading a file into a String and back.
 * @author kstam
 */
public class FileUtil {
    /**
     * Read the file into a String.
     * @param file - the file to be read
     * @return String with the content of the file
     * @throws IOException - when we can't read the file
     */
    public static String readTextFile(File file) throws IOException 
    {
        StringBuffer sb = new StringBuffer(1024);
        BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));
        char[] chars = new char[1];
        while( (reader.read(chars)) > -1){
            sb.append(String.valueOf(chars)); 
            chars = new char[1];
        }
        reader.close();
        return sb.toString();
    }
    /**
     * Write a String into a file.
     * @param file - File to which we write the String
     * @param str - string which will be written
     * @throws IOException - when we can't write to the file
     */
    public static void writeTextFile(File file, String str) throws IOException 
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath()));
        writer.write(str);
        writer.close();
    }

}
