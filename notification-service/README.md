# Notification Service
## Endpoints
### REST
- `POST http://localhost:8082/notification/send`
### Websocket
- `ws://localhost:8082/notifications-ws/**`
## How to use the websocket ?
- Connect to the websocket using the **websocket endpoint** above
- Send a message with the following structure to the **REST endpoint**
```json
{
   "elderEmail   ": string, 
   "alertMessage ": string,
   "alertType    ": ["CARDIAC", "FALL"],
   "alertTime    ": string,
   "location     ": string,
//   "relativeEmail": string, // will be extracted from db
}
```
- Use the websocket web client we have provided to see the messages 
  - Connect to the websocket with the "connect" button
  - enter an email to subscribe to it => it will subscribe to the topic `topic/alert/{email}`
- The clients (the relatives) should subscribe to the notification topic of the elder they are related to