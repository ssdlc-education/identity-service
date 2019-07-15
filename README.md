# Identity Management Backend Service

[![Build Status](https://travis-ci.com/verizon-media-2019-ais3/identity-backend.svg?branch=master)](https://travis-ci.com/verizon-media-2019-ais3/identity-backend)

## Running
To make your life easier, you can follow the steps below to run the service with docker.
Follow the instruction [here](https://docs.docker.com/compose/install/) to install `docker` and `docker-compose`.
Build the services. 
```
docker-compose build
```
If it fails with error message like 
```
INSTALL FAILED: Insufficient free disk space
```
, it may be because the docker overlay occupies the space.  
Try to stop the services and prune the unused containers:
```
docker-compose down
docker container prune
```
Run the services.
```
docker-compose up -d
```
Get the shell of the backend service.
```
docker-compose exec identity-backend /bin/bash
```
Compile and run the backend server.
```
mvn clean jetty:run
```
Get the mysql shell of the backend database service.
```
docker-compose exec identity-db mysql -uidentity -pinsecure_password
```
Get the shell of the frontend service.
```
docker-compose exec identity-frontend /bin/bash
```
Build and run the frontend server
```
go build
./frontend
```
When you are done, stop the container.
```
docker-compose down
```
