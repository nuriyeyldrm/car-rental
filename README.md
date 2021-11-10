# Car Rental API Back-end

The Car Management API implements a complex domain model flow to rental a car. The flow simulates a real world application in terms of having a variety of rental options and business rules.

#### Technologies
- Spring Boot (JPA, Web, Devtools, Validation, Security)
- Spring Framework
- PostgreSQL
- Maven
- Javax
- Jsonwebtoken
- Lombok
- Swagger

##### Application starts on localhost port 8080
- [http://localhost:8080/car-rental/api/user/*]
- [http://localhost:8080/car-rental/api/admin/*]
- [http://localhost:8080/car-rental/api/files/*]
- [http://localhost:8080/car-rental/api/car/*]
- [http://localhost:8080/car-rental/api/reservation/*] 

#### Available Services
| Http Method | Path | Usage |
| ------ | ------ | ------ |
| GET | /car-rental/api/user/auth | get user by username |
| GET | /car-rental/api/admin/{id}/auth | get user by id (preauthorize admin) |
| GET | /car-rental/api/admin/auth/all | get all users (preauthorize admin) |
| POST | /car-rental/api/user/register | register |
| POST | /car-rental/api/user/login | login |
| PUT | /car-rental/api/user/auth | update to user |
| PUT | /car-rental/api/admin/{id}/auth | update to user (preauthorize admin) |
| PATCH | /car-rental/api/user/auth | update to password |
| DELETE | /car-rental/api/admin/{id}/auth | delete to user (preauthorize admin) |
#### ... continues

#### All Postman collections [https://www.getpostman.com/collections/f5dbf51ba0962c03e89f]

#### NOTE: All method except signup/login/visitors methods, needs Authorization Bearer token in header 

