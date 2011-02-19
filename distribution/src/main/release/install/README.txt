SAVARA Installation Instructions
================================

Service Validator for JBossESB:

1) Download JBossAS (http://www.jboss.org/jbossas) - version 5.1.0.GA or higher. Follow the installation
instructions that accompany this distribution.

2) Download JBossESB (http://www.jboss.org/jbossesb) - version 4.7 or higher. Follow the installation
instructions that accompany this distribution, on how to install the ESB into the JBossAS environment.

3) Edit the deployment.properties file in this ${SAVARA}/install folder. Set the org.jboss.as.home
property to the root directory where the JBossAS environment is located, and change the
org.jboss.as.config property from default if you wish to start your JBossAS using a different
configuration. Set the org.jboss.esb.home property to the root directory where the JBossESB
environmet is located.

4) Start a command window and execute the command 'ant deploy'.

5) You should now start your JBossAS server.

When you wish to uninstall this component, simply go back to the ${SAVARA}/install folder
and execute the command 'ant undeploy'.
