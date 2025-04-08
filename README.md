# HYPERATIVA: TECH CHALLENGE

An API to create and query credit cards following the PCI DSS security standard.
- [Tech challenge repository](https://github.com/hyperativa/back-end/)

## Requirements
- **Java 21**
- **Maven**
- **MongoDB**
- **Docker**

## Tech Stack
- **Spring Boot**: Java framework for building web applications.
- **Spring MongoDB**: For MongoDB integration.
- **Spring Security**: Provides authentication and authorization.
- **Spring Web**: For building RESTful APIs.
- **Spring Boot DevTools**: Enhances development experience with features like automatic restarts.
- **Lombok**: Reduces boilerplate code in Java.
- **JWT**: For secure token-based authentication.
- **Jasypt**: For encrypting sensitive data.
- **Bouncy Castle**: For cryptographic operations.
- **JUnit 5**: For unit testing.
- **Mockito**: For mocking dependencies in tests.

## Configuration

### 1. MongoDB
- In the file `docker/mongodb/docker-compose.yml`, set the `MONGO_INITDB_ROOT_PASSWORD` for the MongoDB user `appdev`;
- In the file `docker/mongodb/init-mongo.js`, set the password for the user `appdev` and the database name `hyperativa_challenge`;
- In terminal, navigate to the `docker/mongodb` directory and run the following command to start the MongoDB container:
```bash
docker-compose up -d
```
- The MongoDB container and database will be accessible at `mongodb://appdev:PASSWORD@localhost:27017/?authSource=hyperativa_challenge`.

### 2. Environment Variables
- Evaluate the `application.properties` file to set up the database connection and other configurations.
```plaintext
spring.application.name=hyperativa-challenge
server.port=9001
server.servlet.context-path=/api/v1

logging.level.root=INFO
logging.level.dev.rmmarquini.hyperativa.challenge=DEBUG
logging.file.name=logs/app.log

spring.data.mongodb.uri=mongodb://appdev:PASSWORD@localhost:27017/?authSource=hyperativa_challenge
spring.data.mongodb.database=hyperativa_challenge
logging.level.org.springframework.data.mongodb=DEBUG

jwt.secret=hyperativa-challenge

# command to generate the encryptor pwd: $ openssl rand -base64 32
jasypt.encryptor.password=RANDOM_32bytes_PASSWORD
```

### 3. Jasypt
- To encrypt sensitive data, use the Jasypt library. The `jasypt.encryptor.password` property is used to encrypt and decrypt sensitive information.
- To generate the encryptor password, you can use the following command:
```bash
openssl rand -base64 32
```

### 4. Postman
- The Postman collection is available in the `assets` directory. Import the `hyperativa_challenge.postman_collection.json` file into Postman to test the API endpoints.

## Running the Application

### 1. Install Dependencies
- In the terminal, navigate to the root directory of the project and run the following command:
```bash
mvn clean install
```
### 2. Run the Application
- In the terminal, navigate to the root directory of the project and run the following command:
```bash
mvn spring-boot:run
```
### 3. Testing
- To run the tests, navigate to the root directory of the project and run the following command:
```bash
mvn test
```

## Endpoints

### 1. Authentication

Assuming is there an User microservices and any user is already registered, you can use the following endpoint to authenticate a fake one, and obtain a JWT token:

- **POST** `/api/v1/auth/token?username=ANY_USERNAME`
- **Auth** `Bearer {token}`: use [JWT.io](https://jwt.io/) to create a fake user token, as follows:
  - Header:
    ```json
    {
        "alg": "HS512",
        "typ": "JWT"
    }
    ```
  
  - Payload:
    ```json
    {
        "sub": "ANY_USERNAME"
    }
    ```

  - Signature with the `jwt.secret`:
    ```plaintext
       HMACSHA512(
        base64UrlEncode(header) + "." +
        base64UrlEncode(payload),
        hyperativa-challenge
       )
    ```
  - Or, you can use this configuration to create a JWT token for the user `client1`:
    - Endpoint: `/api/v1/auth/token?username=client1`
    - Bearer Token: `eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJjbGllbnQxIn0.La8SXJXmQh7P6_kIX8gtsajJhYEXV-XZ3cA4SFBHqTSk26WcxUxySYKflckTEwvM1vYRau-8PPIm3foj1Q9uEw`

- Response:
  - Status `200 OK`
  - Body: RAW Token

### 2. Create credit card
- **POST** `/api/v1/cards`
- Auth `Bearer {token}`: get the token from the authentication endpoint.
- Body:
```json
{
    "batchDescription": "DESAFIO-HYPERATIVA",
    "batchDate": "20180524",
    "batchNumber": "0001",
    "lineIdentifier": "C",
    "batchSequenceNumber": "2",
    "cardNumber": "4456897999999999"
}
```
- Response:
  - Status `201 Created`
  - Body:
  ```json
    {
      "id": "67f57b231aa66f56ddda2571",
      "maskedCardNumber": "************9999"
    }
  ```

### 3. Upload a list of cards in a batch
- **POST** `/api/v1/cards/upload`
- Auth `Bearer {token}`: get the token from the authentication endpoint.
- Body: multipart/form-data
```plaintext
{
    "file": "cards.txt"
}
```
- The file `cards.txt` is available in the `assets` directory. The file contains a list of credit cards, one per line, in the following format:
```plaintext
DESAFIO-HYPERATIVA           20180524LOTE0001000010    
C2     4456897999999999                                
C1     4456897922969999                                
C3     4456897999999999                                
C4     4456897998199999                                
C5     4456897999999999124                             
C6     4456897912999999                                
C7     445689799999998                                 
C8     4456897919999999                                
C9     4456897999099999                                
C10    4456897919999999                                
LOTE0001000010  
```
- Response:
    - Status `201 Created`
    - Body: 
    ```json
    [
        {
            "id": "67f57b531aa66f56ddda2572",
            "maskedCardNumber": "************9999"
        },
        {
            "id": "67f57b531aa66f56ddda2573",
            "maskedCardNumber": "************9999"
        },
        {
            "id": "67f57b531aa66f56ddda2574",
            "maskedCardNumber": "************9999"
        }
    ]
    ```

### 4. Query card
- **GET** `/api/v1/cards/{cardNumber}`
- Auth `Bearer {token}`: get the token from the authentication endpoint.
- Response:
  - Status `200 OK`
  - Body:
  ```json
  {
    "id": "67f57b231aa66f56ddda2571",
    "maskedCardNumber": "************9999"
  }
  ```
  
## Notes

1. The application is designed to handle credit card data in compliance with the PCI DSS security standard;
2. The sensitive data is encrypted using Jasypt and Bouncy Castle libraries, and stored safely in MongoDB;
3. The environment variables used to configure the application aren't protected, but in a real scenario they should be stored in a secure way, like Github secrets and variables in the repository settings;
4. The application is designed to be modular and extensible, allowing for easy integration with other services and components.

___
Made with :coffee: by Rafa Mardegan :wave: [Get in touch](https://www.linkedin.com/in/rafamardegan/)
