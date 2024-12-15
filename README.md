# Smartjob challenge
## How to run application
* Go to path folder
* Run mvn clean install
* cd target
* java -jar smartjob-0.0.1-SNAPSHOT.jar --server.port=8080

## How to test application
* Create a POSTMAN environmental variable with name TOKEN and paste token from login whit Bearer word, Example: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtcm9zZWxsaSIsImlhdCI6MTcyOTgzMDIyNCwiZXhwIjoxNzI5ODMzODI0fQ.dkBWwrFkr1_84J9Fh5MaJNh67zrfy7j2YgIcbr6v7H4 (Token es valid for 1h, after that you will have to login again)

## Only request that no require token are auth/register and auth/login

## Apis
* To test the aplication, you can follow swagger api portal.
http://localhost:8080/swagger-ui/index.html

After that, you have to register the user, and after that you can use another apis. You will have to set Authorization header attribute with the Bearer token. For example, a valid flow should be:
* Register user (password with at least 4 letters and 4 numbers)
* Get users
* Get Phone by user id
* Update user:  Change phones 
* Get Phone by user id
* Get users (modified date should be change)
* Login again (login date should be change)

## Considerations
* Email has a validation to check @dominio.cl
* Password has a regex checking at least 4 letters and 4 numbers.

## Password in application.properties
* It is not a valid way to do it. In prod case, the password should be taken from Secret Manager or another place without webexposition. I did it because it is the easiest way to run database in challenge 