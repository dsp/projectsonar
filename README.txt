Project Sonar
===============
Project Sonar is social network analyzer running in your browser.
It is developed at the Karlsruhe Institute of Technology.

Sonar can access and visualize graphs stored in a database. Analyzation is
done by a variety of algorithms shipped with Sonar and a graphical
representation is calculated.

Getting started
---------------
To run the application, download the *.war archive and extract it
to your Java Servlet Container. For example on a Tomcat 6.0 installation:

 $ jar xvf Sonar.war /usr/local/tomcat/webapps/Sonar

Edit the hibernate configuration:

 $ vi /usr/local/tomcat/webapps/Sonar/WEB-INF/classes/hibernate.cfg.xml

Set the administration password by editing the main configuration file: 

 $ vi /usr/local/tomcat/webapps/Sonar/WEB-INF/classes/configuration.xml

Make sure that you have a table Node, Userlist, Edge in your database or
edige the respective hibernate mapping files called (User.hbm.xml, Edge.hbm.xml,
Node.hbm.xml) found in the same directory as the hibernate configuration.

Open Sonar in your browser. You might want to edit the log4j.conf file in the
distribution for a custom logfile. The default is stdout. Tomcat usually logs
the stdout to logs/catalina.out.

Features
---------------
 * Access graphs stored in a RDBMS
 * Run algorithsm to analyze a graph
 * Visuel representation of the graph.

Documentation and Website
---------------
The project website can be found at

 http://projectsonar.org

Design documentation and requirements analysis can be found
at the website.
