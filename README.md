# administration

## Build with

* 	[Maven](https://maven.apache.org/) - Dependency Management
* 	[JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Java™ Platform, Standard Edition Development Kit 
* 	[Spring Boot](https://spring.io/projects/spring-boot) - Framework to ease the bootstrapping and development of new Spring Applications
* 	[MySQL](https://www.mysql.com/) - Open-Source Relational Database Management System
* 	[git](https://git-scm.com/) - Free and Open-Source distributed version control system 
* 	[Lombok](https://projectlombok.org/) - Never write another getter or equals method again, with one annotation your class has a fully featured builder, Automate your logging variables, and much more.
* 	[Swagger 2](https://swagger.io/) - Open-Source software framework backed by a large ecosystem of tools that helps developers design, build, document, and consume RESTful Web services.
* [LogBack](http://logback.qos.ch/) - Logback is intended as a successor to the popular log4j project, [picking up where log4j leaves off](http://logback.qos.ch/reasonsToSwitch.html).
* [Docker](https://www.docker.com/) -A container is a standard unit of software that packages up code and all its dependencies so the application runs quickly and reliably from one computing environment to another.

## Files and Directories

The microservice has a particular directory structure. A representative project is shown below:

```

├── root
├── src
│   └── main
│       └── java
│           └── com.smart.ecommerce
│	            ├── config
│	            ├── oauth
│	            └── administration
│           		├── controller
│           		├── exception
│           		├── repository
│           		├── service
│           		└── dto
├── src
│   └── main
│       └── resources
│           ├── application.properties
│           ├── banner.txt
│           └── logback.xml
├── pom.xml
├── DockerFile
└── README.md
```
## packages
- `oauth` — security configuration;
- `config` —General configurarition. Swagger 2 config RestTemplateConfig;
 - `administration/` — contains the project specific packages
- `administration/repositories` — to communicate with the database;
- `administration/services` — to hold our business logic;
- `administration/controllers` — to listen to the client;
- - `administration/dto` — data model objects;
- `resources/` - Contains all the static resources, templates and property files;
- `resources/application.properties` - It contains application-wide properties. Spring reads the properties defined in this file to configure your application. You can define server’s default port, server’s context path, database URLs etc, in this file.
- `pom.xml` - contains all the project dependencies
## Dependencies
### Entitys
Entity objects are centralized in a separate library called [core entity](https://gitlab.com/innovacion-tecnologica/e-commerce/micro-services/core-entity). To modify and use the library objects, the dependency must be added in the **POM** file of the microservice.
```
<dependency>
	 <groupId>com.smart.ecommerce</groupId>
     <artifactId>ecommerce-entity</artifactId>
     <version>1.0</version>
</dependency>
```
### Logger
  
The loggin utility centralizes the functionality of being able to generate traces at different levels of the transactions served by the microservice. To use the library functions, the dependency must be added in the **POM** file of the microservice.
```
<dependency>
	 <groupId>com.smart.ecommerce</groupId>
     <artifactId>ecommerce-logging</artifactId>
     <version>1.0</version>
</dependency>
```
### Platform Adapter Service
  
The loggin utility centralizes the functionality of being able to generate traces at different levels of the transactions served by the microservice. To use the library functions, the dependency must be added in the **POM** file of the microservice.
```
<dependency>
	 <groupId>com.smart.ecommerce</groupId>
     <artifactId>ecommerce-smartplatform-adapter</artifactId>
     <version>1.0</version>
</dependency>
```
## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.smart.ecommerce.AdministrationApplication` class from your IDE.

- Download the zip or clone the Git repository.
- Unzip the zip file (if you downloaded one)
- Open Command Prompt and Change directory (cd) to folder containing pom.xml
- Open Eclipse 
   - File -> Import -> Existing Maven Project -> Navigate to the folder where you unzipped the zip
   - Select the project
- Choose the Spring Boot Application file (search for @SpringBootApplication)
- Right Click on the file and Run as Java Application

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```
