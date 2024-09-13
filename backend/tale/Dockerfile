FROM openjdk:17-jdk-alpine AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x ./gradlew

RUN ./gradlew dependencies --no-daemon

COPY src src

# RUN ./gradlew clean build --no-daemon
RUN ./gradlew bootJar --no-daemon

FROM openjdk:17-jdk-alpine

COPY --from=builder /app/build/libs/*.jar /home/server.jar

ENTRYPOINT [ "java", "-jar", "/home/server.jar" ]