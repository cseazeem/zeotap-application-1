## Overview
The Rule Engine with AST is a 3-tier application designed to determine user eligibility based on attributes such as age, department, income, and spend. The application utilizes an Abstract Syntax Tree (AST) to represent conditional rules and allows for dynamic creation, combination, and modification of these rules.

## Technologies Used
- **Java Development Kit (JDK)**: Version 22
- **Spring Boot**: For building the backend RESTful APIs
- **PostgreSQL**: As the relational database
- **JDBI**: For database access
- **Lombok**: For reducing boilerplate code in Java classes
- **Liquibase**: For managing database migrations
- **React**: For the frontend user interface

- # Rule Engine API Documentation

This documentation provides details on the Rule Engine backend functionality, including how to create, combine, and evaluate rules using RESTful APIs.

## Endpoints

### 1. Create Rule
**Endpoint:** `POST /rules/create_rule`

**Description:** This endpoint creates a new rule based on a specified rule string.

**Request Body:**
```json
{
  "ruleString": "(age > 30 AND income < 60000)"
}

Response Example:

{
  "id": "1",
  "name": "Rule_123e4567-e89b-12d3-a456-426614174000",
  "ruleString": "(age > 30 AND income < 60000)",
  "astJson": "{...}",
  "createdAt": "2024-10-20T12:34:56",
  "updatedAt": "2024-10-20T12:34:56"
}

2. Combine Rules
Endpoint: POST /rules/combine_rules

Description: Combines two or more existing rules into a single, composite rule.

Request Body:

{
  "ruleIds": [1,2]
}

Response Example:

{
  "id": "323e4567-e89b-12d3-a456-426614174002",
  "name": "CombinedRule_323e4567-e89b-12d3-a456-426614174002",
  "ruleString": "(age > 30 AND income < 60000) OR (age < 25 AND income > 70000)",
  "astJson": "{...}",
  "createdAt": "2024-10-20T12:45:00",
  "updatedAt": "2024-10-20T12:45:00"
}

3. Evaluate Rule
Endpoint: POST /rules/{id}/evaluate_rule

Description: Evaluates a rule against provided data to determine if the conditions are met.

Path Parameter:

id - The Long of the rule to be evaluated.
Request Body Example:

{
  "age": 35,
  "income": 50000,
  "experience": 8,
  "department": "Engineering"
}

{
  "result": true
}

Usage Examples
Creating Individual Rules
Example Rule 1:

json
Copy code
{
  "ruleString": "((age > 30 AND department = 'Sales') OR (age < 25 AND department = 'Marketing')) AND (salary > 50000 OR experience > 5)"
}
Example Rule 2:

json
Copy code
{
  "ruleString": "(age > 30 AND department = 'Marketing') AND (salary > 20000 OR experience > 5)"
}
Combining Rules
To create a composite rule, you can combine the above two rules using their UUIDs:

Request Body:


{
  "ruleIds": [1,2]
}
Response Example:

{
  "id": "323e4567-e89b-12d3-a456-426614174002",
  "name": "CombinedRule_323e4567-e89b-12d3-a456-426614174002",
  "ruleString": "(Combined Rule String)",
  "astJson": "{...}",
  "createdAt": "2024-10-20T12:45:00",
  "updatedAt": "2024-10-20T12:45:00"
}
Evaluating a Combined Rule
To check if the data meets the conditions of a rule, use the evaluate_rule endpoint with the rule ID.

Request Example:

{
  "age": 35,
  "department": "Sales",
  "salary": 60000,
  "experience": 3
}
Response Example:

{
  "result": true
}


## Features
- Create, combine, and evaluate rules using a user-friendly interface.
- Dynamic handling of rules with AST representation.
- Secure API endpoints using JWT for authentication.
- Database migrations managed via Liquibase.

## Project Structure

## Project Structure
```
/rule-engine
|-- /src
|   |-- /main
|   |   |-- /java
|   |   |   |-- /com
|   |   |   |   |-- /cseazeem
|   |   |   |   |   |-- /rule-engine
|   |   |   |   |   |   |-- /controller
|   |   |   |   |   |   |-- /service
|   |   |   |   |   |   |-- /dao
|   |   |   |   |   |   |-- /model
|   |   |   |   |   |   |-- /parser
|   |   |   |   |   |   |-- /exception
|   |   |   |   |-- /resources
|   |   |-- /resources
|   |       |-- /db
|   |           |-- /changelog
|   |           |-- application.properties
|-- /frontend
|   |-- /src
|   |   |-- /components
|   |   |-- /services
|   |   |-- /App.js
|-- README.md
```

## Installation
### Backend
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd rule-engine
   ```
2. Set up PostgreSQL and create a database for the application.
3. Update `application.properties` with your database configuration.
4. Run Liquibase migrations:
   ```bash
   mvn liquibase:update
   ```
5. Build and run the Spring Boot application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

### Frontend
1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the React application:
   ```bash
   npm start
   ```

## Usage
- Use Postman or any API client to interact with the backend API endpoints.
- Access the frontend application at `http://localhost:3000` after starting the React app.

## Testing
- Unit and integration tests can be found in the `/test` directory. Run the tests using:
  ```bash
  mvn test
  ```

## Non-Functional Requirements
- **Security**: Implemented JWT for securing API endpoints.
- **Performance**: Optimized database queries with JDBI.

## Contributing
If you would like to contribute to the project, please submit a pull request or open an issue.

## License
This project is licensed under the MIT License.

## Acknowledgments
- [Spring Boot](https://spring.io/projects/spring-boot)
- [JDBI](https://jdbi.org/)
- [Liquibase](https://www.liquibase.org/)
- [Lombok](https://projectlombok.org/)
- [React](https://reactjs.org/)
