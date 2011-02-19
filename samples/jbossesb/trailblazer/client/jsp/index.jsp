<%@ page import="org.jboss.soa.esb.samples.trailblazer.web.WebCustomer"%>
<%@ page import="org.jboss.soa.esb.samples.trailblazer.loanbroker.LoanRequester"%>
<% 
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
 
String[] formHeader  = {"Name", "Address", "SSN", "Email", "Salary", "Employer", "LoanAmount", "LoanDuration"};
String[] formValue   = new String[formHeader.length];

String errors = "";
int errorCount=0;

for (int i=0; i<formValue.length;i++) {
	formValue[i]    = String.valueOf(request.getParameter(formHeader[i]));
	//out.write(formHeader[i] + "=" + formValue[i] + "<br>");
	if ("null".equals(formValue[i]) || formValue[i]==null || "".equals(formValue[i])) {
	        formValue[i]="";
			errorCount++;
			errors += formHeader[i] + " is a required field<br>";
	}
}
//It's a new form. Let's set some default values.
if (errorCount==formHeader.length) {
	formValue[0]="Joe Broke";
	formValue[1]="1 Spenditall Str., BrokeTown 99999 DC";
	formValue[2]="1234567890";
	formValue[3]="joe@iliketospendit.com";
	formValue[4]="50000.00";
	formValue[5]="Wesayso & Co";
	formValue[6]="1000.00";
	formValue[7]="12";
}

%>
<html>
<head>
<title>JBossESB Loan Broker</title>
<link rel="shortcut icon" href="/favicon.ico"/>
<link rel="stylesheet" href="http://www.jboss.com/themes/jbosstheme/style/custom.css" type="text/css" media="all">
<link rel="stylesheet" href="http://www.jboss.com/themes/jbosstheme/style/global.css" type="text/css" media="all">
<link rel="stylesheet" href="http://www.jboss.com/themes/jbosstheme/style/headings.css" type="text/css" media="all">
<link rel="stylesheet" href="http://www.jboss.com/themes/jbosstheme/style/layout.css" type="text/css" media="all">
<link rel="stylesheet" href="http://www.jboss.com/themes/jbosstheme/style/navigation.css" type="text/css" media="all">
<link rel="stylesheet" href="http://www.jboss.com/themes/jbosstheme/style/pagelayout.css" type="text/css" media="all">
<link rel="stylesheet" href="http://www.jboss.com/themes/jbosstheme/style/tables.css" type="text/css" media="all">

<link rel="stylesheet" href="http://www.jboss.com/themes/jbosstheme/style/screen.css" type="text/css" media="screen">
<link rel="stylesheet" href="http://www.jboss.com/themes/jbosstheme/style/print.css" type="text/css" media="print">
<link rel="stylesheet" href="http://www.jboss.com/themes/jbosstheme/style/common.css" type="text/css" media="all">
</head>
<body>

<div id="TopLogo">
  <a href="http://www.jboss.com"><img src="http://www.jboss.com/themes/jbosstheme/img/logo.gif" alt="JBoss - The Professional Open Source Company" border="0" /></a>
</div>

<div id="TopMenu">
  <table cellpadding="0" cellspacing="0">
  <tr>
    <td class="menu_JBnetwork"><a href="http://network.jboss.com/">subscription</a></td>
    <td class="menu_JBcom"><a href="http://www.jboss.com/">jboss.com</a></td>

    <td class="menu_JBorg"><a href="http://labs.jboss.com">jboss.org</a></td>
    <td class="menu_RH"><a href="http://www.redhat.com">redhat.com</a></td>
  </tr>
  </table>
</div>


<% if (errorCount!=0) { %>
<form id="formLoanBrokerRequest" action="index.jsp" method="post">

<table width="100%" border="0" cellpadding="10" cellspacing="3">
        <tr><td colspan="2"><br><br><br><br>
        <tr><td colspan="2"><h1>Loan Broker Request Form</h1></td></tr>
        <% if (errorCount < formHeader.length) { %> 
        <tr><td colspan="2"><font color="red"><%=errors %></font></td></tr>
        <% } %>
        <% for (int i=0; i<formHeader.length;i++) { %>
        <tr>
			<td width="30%" nowrap><%=formHeader[i] %></td>
			<td width="70%"><input id="<%=formHeader[i] %>" name="<%=formHeader[i] %>" type="text"
	   		    class="textBox" tabindex="1" size="60" value="<%=formValue[i] %>" /></td>
	    </tr>
	    <% } %>
	
	<tr>
	    <td><INPUT TYPE="submit" VALUE="submit loan request">  </td>
	</tr>
</table>

</form>

<% } else  {
    //Setting the values in the request
    WebCustomer webCustomer = new WebCustomer();
    webCustomer.setName(formValue[0]);
    webCustomer.setAddress(formValue[1]);
    webCustomer.setSsn(Integer.valueOf(formValue[2]).intValue());
    webCustomer.setEmail(formValue[3]);
    webCustomer.setSalary(Double.valueOf(formValue[4]).doubleValue());
    webCustomer.setEmployerName(formValue[5]);
    webCustomer.setLoanAmount(Double.valueOf(formValue[6]).doubleValue());
    webCustomer.setLoanDuration(Integer.valueOf(formValue[7]).intValue());
    LoanRequester.request(webCustomer);
%>
    <table width="100%" border="0" cellpadding="10" cellspacing="3">
        <tr><td><br><br><br><br>
        <tr><td><h1>Loan Broker</h1></td></tr>
    	<tr><td><h2>Request was submitted</h2></td></tr>
        <tr><td><a href="index.jsp">Fill out another request</a></td></tr>
	
 <% } %>
</body>
</html>
