package org.jboss.soa.esb.samples.trailblazer.web;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "LoanBrokerInterface", targetNamespace = "http://localhost/trailblazer")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface LoanBrokerInterface {
    @WebMethod
    public void RequestLoan(
        @WebParam(name = "webCustomer_1", partName = "webCustomer_1")
        WebCustomer webCustomer_1);

}
