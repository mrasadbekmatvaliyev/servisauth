# 1-bosqich: Build bosqichi
FROM gradle:8.10.0-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# 2-bosqich: Run bosqichi
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

# 8080-portni ochamiz
EXPOSE 8080

# Jar faylni ishga tushirish
ENTRYPOINT ["java", "-jar", "app.jar"]
