# ELDYCARE Backend

---

Backend component of the Eldycare project. We will be following a **Microservice Event Driven Architecture**

## Compiling the images
Being in this diretory, run the following commands :
```bash
mvn -DskipTests clean install
.\mvnw -DskipTests clean install # if on windows
```
```bash
.\create-images.bat
```
```bash
docker-compose up
```

## Getting Started
- NEO4J Connection
  - Add these variables to your environment
    - `NEO4J_URI=bolt://localhost:7687`
    - `NEO4J_USERNAME=neo4j`
    - `NEO4J_PASSWORD=saga-pablo-lagoon-java-license-4169`
    - Note : change the password to whathever you have set in your local neo4j instance
    
- MongoDB Connection
  - Add these variables to your environment
    - `MongoDB_CLUSTER_URI=eldycare-cluster.xcn4yoa.mongodb.net`
    - `MongoDB_DATABASE=eldycare`
    - `MongoDB_USERNAME=yassinouhadi`
    - `MongoDB_PASSWORD=9ZdBVB78UH6Tb4O7`
    
## Components
- [x] API Gateway
  - [x] Filter to connect with the Authentication Service for JWT validation
- [x] Discovery Service
- [x] Authentication Service
  - [x] Connect with the User service
- [x] User Service
- [x] Notification Service
- [ ] Reminder Service

## Use Case
- Register a user 
  - Check if all the resources are created in : 
    - Neo4j
    - postgresql
- Login a user
  - get the jwt
- Use the user service to get the user information
  - put the jwt in `Authorization` header

