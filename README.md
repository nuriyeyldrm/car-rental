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

##### Application starts on localhost port 8080
- [http://localhost:8080/api/user/*]
- [http://localhost:8080/api/admin/*]
- [http://localhost:8080/api/files/*]
- [http://localhost:8080/api/car/*]
- [http://localhost:8080/api/reservation/*] 

#### Available Services
| Http Method | Path | Usage |
| ------ | ------ | ------ |
| GET | /api/user/auth | get user by username |
| GET | /api/admin/{id}/auth | get user by id (preauthorize admin) |
| GET | /api/admin/auth/all | get all users (preauthorize admin) |
| POST | /api/user/register | register |
| POST | /api/user/login | login |
| PUT | /api/user/auth | update to user |
| PUT | /api/admin/{id}/auth | update to user (preauthorize admin) |
| PATCH | /api/user/auth | update to password |
| DELETE | /api/admin/{id}/auth | delete to user (preauthorize admin) |
#### ... continues

#### Postman collections for user and admin part [https://www.getpostman.com/collections/f5dbf51ba0962c03e89f]

#### NOTE: All method except signup/login methods, needs Authorization Bearer token in header 

