Account Service - REST API

* Overview:

This project is a simple RESTful Account Management API built using Java, Spark Framework, and Gson. It supports account creation, money transfers, and retrieval of account details. The system ensures that only treasury accounts can have a negative balance, while non-treasury accounts must maintain sufficient funds for transfers.


Technologies Used:

- Java (Backend Language)

- Spark Java (Microframework for REST API)

- Gson (JSON Serialization/Deserialization)

- Maven (Build Tool & Dependency Management)

- Postman (API Testing)

- Gatling (Load Testing)


Setup & Installation:

1. Prerequisites

Ensure you have the following installed:

- Java 8+ (JDK 8 or later)

- Maven (Ensure mvn is available)

- Postman (For API testing)

- Gatling (For Load Testing, optional)

2. Clone the Repository

git clone https://github.com/your-repository/account-service.git
cd account-service

3. Install Dependencies & Build

mvn clean install

4. Run the Application

mvn exec:java -Dexec.mainClass=com.example.App

The server will start on http://localhost:4567

5. Postman Collection

To test the API in Postman, import the following file:

Accounts.postman_collection.json

- Open Postman

- Go to File → Import

- Select Accounts.postman_collection.json


Decisions that I have taken:
D.1. Why do I prefer to use "Apache Spark" instead of "Spring Boot" while modeling the "Account Service" with Java? 
Apache Spark is simple, expressive, and built for productivity. It allows us to keep the code in a demo. Also, it is much stronger than the Spring Boot in terms of speed and efficiency due to in-memory computation and scalability. Apache Spark supports high-level APIs in Scala, Java, and Python for ease of use.

D.2. Why did I use Postman?
After modeling the Account Service, I wanted to verify the requirements using Postman.


Known Issues:

Currency Conversion is a stub: Exchange rates are hardcoded, and no API is used.

No Persistent Storage: Accounts exist only in memory; restarting the server resets data.
