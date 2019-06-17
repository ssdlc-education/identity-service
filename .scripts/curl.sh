# Test POST method for createAccount
curl -d'{"username":"Bob","firstName":"Bob","lastName":"Demo","email":"bobdemo@gmail.com", "verified":true, "createTime":"2017-07-21T17:32:28Z","updateTime":"2017-07-21T17:32:28Z","description":"Test account for Bob", "password":"PASSWORD", "blockUntil":"2017-07-21T17:32:28Z", "nthTrial":0}' -H "Content-Type: application/json" -X POST http://localhost:8080/v1/accounts/ -i

# TEST GET method for getAccount
curl http://localhost:8080/v1/accounts/Alice
curl http://localhost:8080/v1/accounts/Bob

# TEST PUT method for updateAccount
 curl -d'{"username":"Bob","email":"blablabla@gmail.com", "verified":true, "updateTime":"2018-01-01T17:32:28Z","description": "Changed!", "password":"abcdefg", "blockUntil":"2018-01-01T17:54:28Z", "nthTrial":1}' -H "Content-Type: application/json" -X PUT http://localhost:8080/v1/accounts/@me

# Test GET method after update
curl http://localhost:8080/v1/accounts/Bob
