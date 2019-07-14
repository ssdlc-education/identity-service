# Frontend service

## Build and Run
```
go build ./request_handler.go
./request_handler
```
It will start a server listening on `localhost:5000`, using `identity-backend:8080` as backend address.  
You can also specify the backend address manually:
```
./request_handler --backend identity-backend:8080
```
