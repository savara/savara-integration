
The trailblazer is a small application that uses several of the services provided by the JBoss ESB.


Required to run the Trailblazer:
- ANT
- A mail server to send email notifications
- JBoss AS 4.2.1 or higher with EJB3 and JBossWS support.  You can install the latest JBoss AS JEMS installer and select the "EJB3" install config to get the right one installed.
- JBoss AS 4.2.1 or higher with the JBoss ESB SAR deployment (jbossesb.sar). 
- JBossESB 4.3 or higher

Settings required to edit before running:

File: jbossesb-properties.xml (jbossesb.sar/jbossesb-properties.xml)
- Update the section titled "transports" and specify all of the SMTP mail server settings for your environment.

File : deployment.properties (install/deployment.properties)
- Update the JBossAS and JBossESB location settings.

File : trailblazer.properties (TB_ROOT/trailblazer.properties)
- Update the file.bank.monitored.directory and file.output.directory properties' value properly. 
  These are input and output folder needs to be specified, they are set to /tmp/input /tmp/output by default.

File : jboss-esb.xml (TB_ROOT/esb/conf/jboss-esb.xml)
- there is a "<fs-provider>..</fs-provider>" block, update the "directory" attribute value as same as file.output.directory value in trailblazer.properties file.

Basic Trailblazer example running instructions:
------------------------------------------------
To run the Trailblazer, follow these steps:

1 - run your JBoss AS - you will need to have the 4.2.1 or higher with the EJB3 support installed.  This is required because the TB uses the JSR-181 pojo style web service.

2 - from the TB_ROOT, execute the command to start the ESB: "ant deploy"
* this should deploy the ESB and WAR files to your JBoss AS server/default.

3 - from the TB_ROOT/banks execute the command to start the JMS Bank service: "ant runJMSBank".

4 - start another window/shell, from the TB_ROOT/banks execute the command to start the File bank service: "ant runFileBank".

5 - from the jsp: localhost:8080/trailblazer.

That's it.  Now you can submit quotes, You will see either a loan request rejected (single email)
because the score is less than 4, or two emails (one from JMS bank and one from FileBased
bank) with valid quotes.
NOTE: The current code does not actually send an email. But if you wish to send the real email, then uncomment the relevant lines in the
esb/src/org/jboss/soa/esb/samples/trailblazer/actions/NotifyCustomerActions.java file, and redeploy the example.
