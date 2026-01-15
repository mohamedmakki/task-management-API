FROM eclipse-temurin:17.0.11_9-jdk-ubi9-minimal AS build

WORKDIR /app

# Copy Maven wrapper and config first (for caching)
COPY mvnw pom.xml ./
COPY .mvn .mvn
# Make mvnw executable (for Linux environments such as github runners)
RUN chmod +x mvnw

# Download app dependencies
RUN ./mvnw -B dependency:go-offline

# Copy source code and build
COPY src src
RUN ./mvnw -B package -DskipTests


#Run stage which will use the JAR artefact built in the build stage
FROM eclipse-temurin:17.0.11_9-jdk-ubi9-minimal AS run

RUN groupadd -r spring && useradd -r -g spring spring

# Create /data and /logs directories and give spring user write access to them
RUN mkdir -p /logs /data && chown -R spring:spring /logs /data

USER spring:spring

COPY --from=build /app/target/*.jar /app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]