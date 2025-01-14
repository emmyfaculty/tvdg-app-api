
## tvdeluxe Enterprise App API

### Overview

Client Management and Transactions.

### Features:
- Create and Manage Clients
- Clients Shipment Placement and Management.

### Tools:
- Language/Framework: Java/Spring Boot
- Flyway -Database migration
- JUnit - Unit and Integration testing
- AWS SMTP -Email notification service
- MySQL -RDBMS
- Circle-Ci CI/CD
- Docker
- AWS Elasticbeanstalk


To build the application:
-------------------
From the command line:

	$ mvn clean install

To buid without running the tests:

    $ mvn clean install -DskipTests

Run the application in a container:
-------------------
	$ docker-compose run --service-ports web -d


### To Run the Application Locally:

- Create application-dev.properties file in src/main/resources folder,

- Create your database,

#### For Database configuration:
Add the followings in application-dev file:

    spring.datasource.driverClassName = com.mysql.jdbc.Driver
    spring.datasource.url=jdbc:mysql://localhost:3306/{your-database-name}?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
    spring.datasource.username = {your_database_username}
    spring.datasource.password = {your_database_password}

#### For Email configuration-smtp protocol:
Add the followings in application-dev file:

    spring.mail.host = {your_email_host}
    spring.mail.username = {your_email_username}
    spring.mail.password = {your_email_password}
    spring.mail.properties.mail.transport.protocol = {your_email_protocol}
    spring.mail.properties.mail.smtp.port = {your_email_port}
    spring.mail.properties.mail.smtp.auth = {true|false}
    spring.mail.properties.mail.smtp.starttls.enable = {true|false}
    spring.mail.properties.mail.smtp.starttls.required = {true|false}


## Accessing the Application:
-------------------

App base url: http://localhost:5000/tvdgapp

Api documentation available at : http://localhost:5000/swagger-ui.html



#   t v d g - a p p - a p i  
 