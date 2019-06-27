
# Write JWT secret for token and credential service
openssl rand -hex 16 > .secret/token.key
openssl rand -hex 16 > .secret/cookie.key

# Test POST method for createAccount
curl -d'{"username":"Alice","firstName":"Alice","lastName":"Demo","email":"alice@gmail.com", "emailStatus":1, "description":"Test account for Alice", "password":"PASSWORD"}' -H "Content-Type: application/json" -X POST http://localhost:8080/v1/accounts/ -i
# curl -i will append the message to the terminal without starting a new line
echo ""


curl -d'{"username":"Bob","firstName":"Bob","lastName":"Demo","email":"bob@gmail.com", "emailStatus":1, "description":"Test account for Bob", "password":"PASSWORD"}' -H "Content-Type: application/json" -X POST http://localhost:8080/v1/accounts/ -i
# curl -i will append the message to the terminal without starting a new line
echo ""

# TEST GET method for getAccount
curl http://localhost:8080/v1/accounts/Alice
curl http://localhost:8080/v1/accounts/Bob

# Test POST method for login session
curl -d'{"username":"Alice","password":"PASSWORD"}' -H "Content-Type: application/json" -X POST http://localhost:8080/v1/sessions/ -i
# curl -i will append the message to the terminal without starting a new line
echo ""
