# Modsink Application

This project is a demonstration of how one could migrate a [legacy JBoss Java application](https://github.com/jboss-developer/jboss-eap-quickstarts/tree/8.0.x/kitchensink) to use modern technologies.
The application itself is a RESTful API with a simple frontend interface for registering and viewing a list of members.
The chosen modernization upgrades for this project are:
* Spring Boot for the application layer
* MongoDB for the data layer
* Thymeleaf for the frontend layer
* JUnit for testing
* TestContainers for testing persistance layer

## Prerequisites

Before you begin, ensure you have met the following requirements:

- **Java Development Kit (JDK)**: Make sure you have [JDK 21](https://www.oracle.com/java/technologies/downloads/) or later installed.
- **Maven**: Ensure you have [Apache Maven](https://maven.apache.org/install.html) installed for building the project.
- **Database**: The application uses MongoDB for it's persistance layer.  Either deploy a local community MongoDB, or better yet, deploy a free tier database with [MongoDB Atlas](https://www.mongodb.com/products/platform/atlas-database) in the cloud
- **Docker**(Optional for Testing): The tests for this application are using TestContainers, which require Docker.  This allows us to perform integration tests against a test database and not against the one configured in the application.

## Configuring MongoDB Database

There are two recommended ways to go about this, either with Docker or with MongoDB Atlas.

### MongoDB Atlas
* Register and Login to [MongoDB Atlas](https://www.mongodb.com/products/platform/atlas-database)
* Create an M0 Cluster from the Atlas Dashboard
* Navigate to SECURITY -> Database Access and add a new Database User with permissions to `read and write to any database`
   * Note the Username and Password
* Click "Connect" in our cluster to find and note the uri string to your MongoDB instance
* In the project, modify `application.properties` under the resources folder.  Change `spring.data.mongodb.uri` to your Atlas URL with the following format: `mongodb+srv://<username>:<password>@<cluster-uri>/<database-name>?retryWrites=true&w=majority
* NOTE: Depending on your permissions, it may not be necessary to create the database before running the application.  To be safe, create the database beforehand and ensure your user has permission to read and write from it.

### Docker Image
* User the following commands to quickly deploy a local MongoDB instance:

   ```bash
   docker pull mongo
   docker run --name mongodb -d -p 27017:27017 mongo
   ```

* In the project, ensure `application.properties` has `spring.data.mongodb.uri` set to `mongodb://localhost:27017/modsink`

* To stop the docker image:
   ```bash
   docker stop mongodb
   ```


## Building the Project

1. **Clone the Repository**: Start by cloning the repository to your local machine.

   ```bash
   git clone https://github.com/takameyer/modsink
   cd modsink
   ```

2. **Build the Project**: Use Maven to build the project. This will compile the code, run the tests, and package the application.

   ```bash
   mvn clean install
   ```

   If the build is successful, you should see a message indicating that the build has completed.

## Running the Application

1. **Run the Application**: Use the following command to start the application.

   ```bash
   mvn spring-boot:run
   ```

   The application will start on the default port `8080`. You can access it at `http://localhost:8080`.

2. **Access the Web Interface**: Open a web browser and navigate to `http://localhost:8080` to access the home page. Here, you can register new members and view the list of registered members.

3. **Access the API**: The RESTful API is available at `http://localhost:8080/api/members`. You can use tools like `curl` or Postman to interact with the API.

## Testing

The project includes integration and unit tests to ensure the functionality of the application. To run the tests, use the following command:

```bash
mvn test
```

This will execute all the tests and provide a summary of the results.

## Configuration

The application is configured to MongoDB for development. You can change the database configuration in the `src/main/resources/application.properties` file if needed.

## Troubleshooting

- **Port Conflicts**: If port `8080` is already in use, you can change the port by modifying the `server.port` property in the `application.properties` file.
- **Dependency Issues**: Ensure all dependencies are correctly specified in the `pom.xml` file. Run `mvn clean install` to resolve any dependency issues.


## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

