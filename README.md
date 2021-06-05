# Book Demo
This is a demo Spring Boot project illustrating CRUD operations on "Book" entity.

## Pre-requisites
1. Java 11 or higher (with JAVA_HOME variable and environment path set)
2. PostgreSQL - 9 or higher
2. Maven - 3.5 or higher (for Testing and Building the project) (with environment path set)
3. Postman - 8 or higher (For using Postman collection to test the project)

## Pre Configuration
This project uses a PostgreSQL database and a dedicated postgres user.
Follow these steps to setup your system,
1. Run the queries present in */resources/initialize_db.sql* to setup the user and the database.
2. The project is configured to connect to a database named **bookdemo, on localhost, at port 5432**. If your database server is configured else where or if your database name is different, you will have to update the datasource URL in the *application.properties* file, as per your configuration. If the configuration has been updated, you will have to build the project again to generate the latest JAR file.

This is the only database configuration required, the tables will automatically be created once this project connect to the database.

## Building the project
The project can be built using maven. Run this command in the base directory of the project,
```
mvn clean install
```
This will run the tests and package the project into a JAR file.

## Running the Project
The JAR file can be directly run on Java using this command in the build directory,
```
java -jar demo-0.0.1-SNAPSHOT.jar
```

## Running Tests
The project has a few integration tests configured. These tests can be run using the following maven command,
```
mvn test
```

## Using the Postman Collection
A Postman collection is available with this project - *resources/BookDemo.postman_collection.json*. This collection can be imported into Postman and used to test the APIs.
**Note:** The url of the requests is set to **localhost:8000**, if you are running this project elsewhere update the *url* variable in the collection.
Each of the postman requests have a few basic test cases that can be used to validate the APIs. When using this collection to test the APIs, it is recommended to use the Postman runner that runs all the requests in the collection.