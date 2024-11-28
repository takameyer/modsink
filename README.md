# Modsink Application

This project is a Spring Boot application that manages member registrations. It includes RESTful APIs and a simple web interface for registering and viewing members. The application uses JPA for data persistence and is tested using JUnit.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- **Java Development Kit (JDK)**: Make sure you have JDK 11 or later installed.
- **Maven**: Ensure you have Apache Maven installed for building the project.
- **Database**: The application uses an in-memory H2 database by default, so no additional setup is required for development purposes.

## Building the Project

1. **Clone the Repository**: Start by cloning the repository to your local machine.

   ```bash
   git clone <repository-url>
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

The application is configured to use an in-memory H2 database for development. You can change the database configuration in the `src/main/resources/application.properties` file if needed.

## Troubleshooting

- **Port Conflicts**: If port `8080` is already in use, you can change the port by modifying the `server.port` property in the `application.properties` file.
- **Dependency Issues**: Ensure all dependencies are correctly specified in the `pom.xml` file. Run `mvn clean install` to resolve any dependency issues.

