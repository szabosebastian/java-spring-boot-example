## Use an official Maven image as the base image
#FROM maven:3.8.4-openjdk-11-slim AS build
## Set the working directory in the container
#WORKDIR /app
## Copy the pom.xml and the project files to the container
#COPY pom.xml .
#COPY src ./src
## Build the application using Maven
#RUN mvn clean package -DskipTests
## Use an official OpenJDK image as the base image
#FROM openjdk:21-jdk
## Set the working directory in the container
#WORKDIR /app
## Copy the built JAR file from the previous stage to the container
#ARG JAR_FILE=*.jar
#COPY target/${JAR_FILE} my-application.jar
## Set the command to run the application
#CMD ["java", "-jar", "my-application.jar"]

#FROM openjdk:21-jdk
#ARG JAR_FILE=*.jar
#COPY target/${JAR_FILE} parking-space.jar
#ENTRYPOINT ["java", "-jar", "parking-space.jar"]


## Use an official OpenJDK runtime as a parent image
#FROM maven:3.9.6-amazoncorretto-21
#
## Set the working directory in the container
#WORKDIR /app
#
## Copy the Maven executable to the container image
#COPY mvnw .
#COPY .mvn .mvn
#
## Copy the project files
#COPY pom.xml .
#COPY src src
#
## Build the application
#RUN chmod +x mvnw
#RUN ./mvnw clean install -DskipTests
#
## Run the application
#CMD ["java", "-jar", "target/java-spring-boot-example-1.0.0-SNAPSHOT.jar"]

# Build stage
FROM maven:3.9.6-amazoncorretto-21 AS build
COPY pom.xml /app/
COPY src /app/src
RUN mvn -f /app/pom.xml clean package

# Run stage
FROM maven:3.9.6-amazoncorretto-21
COPY --from=build /app/target/java-spring-boot-example-1.0.0-SNAPSHOT.jar /app/java-spring-boot-example-1.0.0-SNAPSHOT.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/java-spring-boot-example-1.0.0-SNAPSHOT.jar"]
