
```
sms-processor/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── library/
│   │   │           └── sms_processor/
│   │   │               ├── SmsProcessorApplication.java
│   │   │               ├── controller/
│   │   │               │   └── SmsBatchController.java          # Optional REST endpoint
│   │   │               ├── model/
│   │   │               │   ├── OverdueBookInfo.java             # Phone number + due date
│   │   │               │   └── BatchResult.java                 # Result wrapper
│   │   │               └── service/
│   │   │                   └── SmsService.java                  # Core logic & scheduler
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/                                          # Optional: if needed
│   └── test/                                                    # Unit tests
│       └── java/
│           └── com/
│               └── library/
│                   └── sms_processor/
│                       └── SmsProcessorApplicationTests.java
```
