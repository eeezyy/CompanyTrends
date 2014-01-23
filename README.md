aic13
=====

crowdsourcing platform

Mockup for crowdsourcing platform (replacement for MobileWorks)

Built using Eclipse J2EE environment (Kepler) running on Tomcat 7

- import into Eclipse as Existing Maven Project
- build project in Eclipse, either add Tomcat as server and "run on server" or export as war file
- copy mock.db to a convenient location and change the path in src\main\resources\META-INF\persistence.xml
- in case of war file copy the war file to <tomcat installdir>/webapps. Start Tomcat with <tomcat installdir>/bin/startup.bat
- the webservice is available on http://localhost:8080/mock/rest/task
  GET http://localhost:8080/mock/rest/task?id=4
  POST http://localhost:8080/mock/rest/task <task><id>4</id><user>peter1</user> ... other fields ...</task>
  PUT http://localhost:8080/mock/rest/task <task><id>4</id><user>peter1</user> ... other fields ...</task>
  DELETE http://localhost:8080/mock/rest/task?id=4
  
Web service for crowdsourcing app

- copy sqlite-jdbc-3.7.2.jar to <tomcat installdir>/dir
- build project in Eclipse, either add Tomcat as server and "run on server" or export as war file
- change DbConnectionManager.java to hold the location of aic.db
- in case of war file copy the war file to <tomcat installdir>/webapps. Start Tomcat with <tomcat installdir>/bin/startup.bat
- the webservice is available on http://localhost:8080/crowd/rest/tag
- GET http://localhost:8080/crowd/rest/tag?name=tag1