package org.savara.integration.jbossesb;

import static org.junit.Assert.*;

import org.pi4soa.common.xml.XMLUtils;
import org.savara.integration.jbossesb.ESBUtil;

public class ESBUtilTest {

	@org.junit.Test
	public void testGetBodyText() {
		
		String mesg="<soap:Envelope xmlns:soap=\""+ESBUtil.SOAP_ENVELOPE+"\" >"+
			"<soap:Header>"+
				"<m:Trans xmlns:m=\"http://www.w3schools.com/transaction/\" "+
						"soap:mustUnderstand=\"1\">234</m:Trans>"+
			"</soap:Header>"+
		  	"<soap:Body>"+
				"<m:GetPrice xmlns:m=\"http://www.w3schools.com/prices\">"+
					"<m:Item>Apples</m:Item>"+
				"</m:GetPrice>"+
			"</soap:Body>"+
			"</soap:Envelope>";
		
		String expected="<m:GetPrice xmlns:m=\"http://www.w3schools.com/prices\">"+
							"<m:Item>Apples</m:Item>"+
						"</m:GetPrice>";
		
		java.io.Serializable result=ESBUtil.getBody(mesg);
		
		if ((result instanceof String) == false) {
			fail("Result should be a string");
		}
		
		if (result.equals(expected) == false) {
			fail("Failed to match body");
		}
	}
	
	@org.junit.Test
	public void testGetBodyXML() {
		
		String mesg="<soap:Envelope xmlns:soap=\""+ESBUtil.SOAP_ENVELOPE+"\" >"+
			"<soap:Header>"+
				"<m:Trans xmlns:m=\"http://www.w3schools.com/transaction/\" "+
						"soap:mustUnderstand=\"1\">234</m:Trans>"+
			"</soap:Header>"+
		  	"<soap:Body>"+
				"<m:GetPrice xmlns:m=\"http://www.w3schools.com/prices\">"+
					"<m:Item>Apples</m:Item>"+
				"</m:GetPrice>"+
			"</soap:Body>"+
			"</soap:Envelope>";
		
		try {
			org.w3c.dom.Node mesgxml=XMLUtils.getNode(mesg);
			
			String expected="<m:GetPrice xmlns:m=\"http://www.w3schools.com/prices\">"+
								"<m:Item>Apples</m:Item>"+
							"</m:GetPrice>";
			
			java.io.Serializable result=ESBUtil.getBody((java.io.Serializable)mesgxml);
			
			if ((result instanceof org.w3c.dom.Node) == false) {
				fail("Result should be a DOM Node");
			}
			
			String resulttext=XMLUtils.getText((org.w3c.dom.Node)result);
			
			if (resulttext.equals(expected) == false) {
				fail("Failed to match body");
			}
		} catch(Exception e) {
			fail("Failed to get body from XML: "+e);
		}
	}
}
