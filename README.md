# Spring Boot Microservices Practice
This repository contains a practice project for Spring Boot demonstrating a microservices architecture. It consists of three different services orchestrated by a fourth service using Apache Camel.

## Services
1. **Service 1**: Enigma Step One
2. **Service 2**: Enigma Step Two
3. **Service 3**: Enigma Step Three

The orchestrator service leverages Apache Camel to coordinate interactions between the three services.

## Getting Started
To begin working with this project, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/SebasDosman/enigma_api_apache_camel
   ```
   Navigate to each service's directory and install dependencies.

2. Run each service independently:
   
   - Service 1:
   ```bash
   cd ResolveEnigmaApiStepOne/
   mvn spring-boot:run
   ```

   - Service 2:
   ```bash
   cd ResolveEnigmaApiStepTwo/
   mvn spring-boot:run
   ```
   
   - Service 3:
   ```bash
   cd ResolveEnigmaApiStepThree/
   mvn spring-boot:run
   ``` 
   
   - Orchestrator Service:
   ```bash
   cd ResolveEnigmaApiOrchestor/
   mvn spring-boot:run
   ```

## Running the Services
Each service can be started by navigating to its directory and running the appropriate command. The orchestrator service should be started last, as it depends on the other services.

## Contributing
Contributions are encouraged and welcome! Please review our CONTRIBUTING.md file for guidelines on how to contribute to this project.

## License
This project is licensed under the MIT License - see the LICENSE.md file for details. Feel free to fork and modify this project for your own practice or use. Enjoy!
