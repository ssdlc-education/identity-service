#!/bin/bash
set -xeuo pipefail
docker-compose build
docker-compose up -d
./start_server_background.sh
echo "=== Running functional test ==="
docker-compose exec identity-functest mvn clean
docker-compose exec identity-functest mvn test -Dcucumber.options="--strict --tags @registerTest"
docker-compose exec identity-functest mvn test -Dcucumber.options="--strict --tags '@loginTest or @updateTest'"
