FROM gradle:8.7-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean bootJar --no-daemon

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
# RUN apt-get update && apt-get install -y git
COPY --from=builder /app/build/libs/*.jar app.jar

VOLUME /config-repository

ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["java", "-jar", "app.jar"]
