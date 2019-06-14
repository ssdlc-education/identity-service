# Test POST method for createAccount
curl -d'{"username":"Bob","firstName":"Bob","lastName":"Demo","email":"bobdemo@gmail.com","createTime":"2017-07-21T17:32:28Z","updateTime":"2017-07-21T17:32:28Z","description":"Test account for Bob", "password":"PASSWORD"}' -H "Content-Type: application/json" -X POST http://localhost:8080/v1/accounts/ -i

# Test GET method for getAccount/
curl http://localhost:8080/v1/accounts/Alice
curl http://localhost:8080/v1/accounts/Bob
