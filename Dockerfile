FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/java-k8s-demo-1.0.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
