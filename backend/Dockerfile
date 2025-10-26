FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /src
COPY . .
RUN mvn clean -q package --debug

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /src/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
