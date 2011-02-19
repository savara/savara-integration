package org.jboss.soa.esb.samples.trailblazer.loanbroker;

import javax.naming.InitialContext;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import org.jboss.soa.esb.samples.trailblazer.web.*;

/**
 * LoanRequester is a small class that serves as a web service client.
 */
public class LoanRequester {
	public LoanRequester() {	
	}
	
	/**
	 * Grab the port and call request loan.
	 * @return customer customer bean
	 */
	public static void request(WebCustomer customer) { 
		try {
			InitialContext iniCtx = new InitialContext(); 
			//Service service = (Service)iniCtx.lookup("java:comp/env/service/LoanBrokerService");
			//LoanBrokerInterface port = (LoanBrokerInterface)service.getPort(LoanBrokerInterface.class);
            Service service = Service.create(
                    new URL("http://localhost:8080/trailblazer/LoanBroker?wsdl"),
                    new QName("http://localhost/trailblazer","LoanBrokerService") );
            LoanBrokerInterface port = (LoanBrokerInterface)service.getPort(LoanBrokerInterface.class );
			port.RequestLoan(customer);
		} catch (Exception e) {
			e.printStackTrace();
		 }
	} 
}