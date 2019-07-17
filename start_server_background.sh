#!/usr/bin/expect -f

send_user "=== Starting backend ===\n"
spawn docker-compose exec identity-backend mvn clean package spotbugs:check jetty:run
set timeout 300
expect {
    timeout {send_user "It takes too long to start the server!\n"; exit 1}
    eof {send_user "The server cannot be started\n"; exit 1}
    "Started Jetty Server"
}

send_user "=== Backend is started ===\n"
send_user "=== Starting Frontend ===\n"
spawn docker-compose exec identity-frontend go run .
set timeout 30
expect {
    timeout {send_user "It takes too long to start the server!\n"; exit 1}
    eof {send_user "The server cannot be started\n"; exit 1}
    "Listening on"
}

send_user "=== Frontend is started ===\n"
