# Spring MVC

### Environment Setup

[set Tomcat as server](http://javahonk.com/javax-servlet-http-httpservlet-was-not-found-on-the-java-build-path/)

[HttpServlet was not found error](http://howtodoinjava.com/tools/eclipse/solved-the-superclass-javax-servlet-http-httpservlet-was-not-found-on-the-java-build-path-in-eclipse/)

[spring mvc basic](https://www.tutorialspoint.com/spring/spring_mvc_hello_world_example.htm)

Once you are done creating the source and configuration files, export your application. 
Right-click on your application and use Export > WAR File option and save your HelloWeb.war file in Tomcat's webapps folder.

### How to change tomcat port number

- Goto tomcat>conf folder
- Edit server.xml
- Search "Connector port"
- Replace "8080" by your port number
- Restart tomcat server.

### Starting and Stopping the Tomcat Server

This appendix describes how to start and stop the Tomcat server from a command line prompt as follows:

Go to the appropriate subdirectory of the EDQP Tomcat installation directory. The default directories are:

- On Linux: /opt/Oracle/Middleware/opdq/server/tomcat/bin

- On Windows: c:\Oracle\Middleware\opdq\server\tomcat\bin

Where server is the name of your Oracle DataLens Server.

Run the startup command:

- On Linux: ./startup.sh

- On Windows: % startup.bat

Verify that the service was started correctly by looking for the final server startup messages.

Likewise, the Tomcat Server can be stopped from the command line with the following command:

- On Linux: ./shutdown.sh

- On Windows: % shutdown.bat
