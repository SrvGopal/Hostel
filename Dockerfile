FROM maven:3.9.7-sapmachine-22 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:22-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
