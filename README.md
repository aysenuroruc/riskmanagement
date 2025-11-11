# Betting Odds Risk Management Project

### Functionality
-  View all matches with current odds
-  Place single bets on match outcomes
-  Real-time odds adjustment based on risk
-  Risk limit validation per match outcome
-  Thread-safe concurrent bet processing

### Focusing On
- **Tech Stack**: Java 21, Spring Boot 3, JPA, MapStruct
- **Focus**: Clean Architecture, SOLID, Testing, Best Practices
- 
### Prerequisites
- Java 21 or higher
- Maven 3.8+
- 
### Installation
Clone repo, build and run the application.
- git clone <repository-url>
- cd betting-odds-risk-management
- mvn clean install
- mvn spring-boot:run

### Accessing the Application
- Application: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html -- swagger for API documentation
- H2 Console: http://localhost:8080/h2-console
    - JDBC URL: `jdbc:h2:mem:bettingdb
    - Username: `sa`
    - Password: (empty)