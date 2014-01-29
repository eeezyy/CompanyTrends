aic13
=====

Run on public server

The application is deployed on http://companytrends-tuvienna.rhcloud.com/crowd

- select company, organization or product from the dropdown list
- enter a topic and click on search button to get related topics
- select a topic and click "assign job", 
  articles are searched in background and a task for each article will be sent to mock platform which simulates a crowdsourcing platform
  you will be redirected to the created job
  click "reload" to check if articles have been found
  click "Menu" -> "Job List" to see the list of assigned jobs
  
- go to http://companytrends-tuvienna.rhcloud.com/mock to check for tasks assigned to the crowdsourcing platform
- select a task and rate it
- go back to the joblist and select the created job. See the ratings per article and the combined results
- user quality indicator can be seen at "Menu" -> "User Quality"

Build yourself

Prerequisites

- install Tomcat 7 application server (http://tomcat.apache.org/)
- install Maven 2 (https://maven.apache.org/)
- (optional) install Eclipse for Java EE Developer (http://www.eclipse.org/downloads/moreinfo/jee.php)
- clone repository from (https://github.com/eeezyy/CompanyTrends)
  or download ZIP-File (https://github.com/eeezyy/CompanyTrends/archive/master.zip) which will be faster

Crowdsourcing platform

to deploy:
- change src\main\resources\META-INF\persistence.xml to point to the location where aic.db should be created
  (must have write permission)
- in the terminal go to the root project folder and run Maven: mvn install
- then copy the files 
	./crowd/targed/crowd-0.0.1-SNAPSHOT.war
	./mock/targed/mock-0.0.1-SNAPSHOT.war
  to <tomcat installdir>/webapps and rename them to:
	crowd.war
	mock.war
- Start Tomcat with <tomcat installdir>/bin/startup.bat
- open http://localhost:8080/crowd

for developer:
- import into Eclipse as Existing Maven Project
- build project in Eclipse, either add Tomcat as server and "run on server" or export as war file
- change src\main\resources\META-INF\persistence.xml to point to the location where aic.db should be created
  (must have write permission)
- in case of war file copy the war file to <tomcat installdir>/webapps. Start Tomcat with <tomcat installdir>/bin/startup.bat
- open http://localhost:8080/crowd

Mockup for crowdsourcing platform (replacement for MobileWorks)

- import into Eclipse as Existing Maven Project
- build project in Eclipse, either add Tomcat as server and "run on server" or export as war file
- change src\main\resources\META-INF\persistence.xml to point to the location where mock.db should be created
- in case of war file copy the war file to <tomcat installdir>/webapps. Start Tomcat with <tomcat installdir>/bin/startup.bat
- open http://localhost:8080/mock
  
