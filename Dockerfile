# Stage 1 – Build the application using Java 21
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN ./mvnw -v || true
RUN apt-get update && apt-get install -y maven

RUN mvn clean package -DskipTests

# Stage 2 – Run the application
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/target/smartbank.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
