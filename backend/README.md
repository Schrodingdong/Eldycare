# ELDYCARE Backend

---

Backend component of the Eldycare project. We will be following a **Microservice Event Driven Architecture**

## Getting Started
- NEO4J Connection
  - Add these variables to your environment
    - `NEO4J_URI=bolt://localhost:7687`
    - `NEO4J_USERNAME=neo4j`
    - `NEO4J_PASSWORD=saga-pablo-lagoon-java-license-4169`
    - Note : change the password to whathever you have set in your local neo4j instance

## Components
- [x] API Gateway
  - [x] Filter to connect with the Authentication Service for JWT validation
- [x] Discovery Service
- [x] Authentication Service
  - [x] Connect with the User service
- [x] User Service
- [x] Notification Service
- [ ] Reminder Service


