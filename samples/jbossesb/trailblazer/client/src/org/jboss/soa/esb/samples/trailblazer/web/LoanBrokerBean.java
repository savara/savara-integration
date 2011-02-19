package org.jboss.soa.esb.samples.trailblazer.web;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.apache.log4j.Logger;
import org.jboss.soa.esb.samples.trailblazer.loanbroker.LoanBroker;

@WebService(name = "LoanBrokerInterface", targetNamespace = "http://localhost/trailblazer", serviceName = "LoanBrokerService")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class LoanBrokerBean {
		private static Logger logger = Logger.getLogger(LoanBrokerBean.class);
		
	   @WebMethod
	   public void RequestLoan(@WebParam(name="webCustomer_1") WebCustomer webCustomer_1)
	   {
			logger.info("WebCustomer received: \n" + webCustomer_1);
			
			LoanBroker broker = new LoanBroker();
			broker.processLoanRequest(webCustomer_1);
	   }
}