# FROM openjdk:17-jdk-slim
# VOLUME /tmp
# COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar
# ENTRYPOINT ["java","-jar","/app.jar"]


# 1️⃣ 使用 Maven 官方 image 作為 build 環境
FROM maven:3.9.6-eclipse-temurin-17 AS build

# 2️⃣ 將專案程式碼複製進來
COPY . /app
WORKDIR /app

# 3️⃣ 使用 Maven 打包 (會下載依賴)
RUN mvn clean package -DskipTests

# 4️⃣ 使用輕量級 JDK 作為最終運行環境
FROM eclipse-temurin:17-jdk-jammy

# 5️⃣ 複製前面 build 出來的 jar
COPY --from=build /app/target/*.jar app.jar

# 6️⃣ 設定 container 啟動時執行 Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
