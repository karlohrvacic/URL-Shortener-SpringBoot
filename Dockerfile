FROM arm64v8/eclipse-temurin:17-ubi9-minimal

RUN mkdir /app

COPY target/urlShortener-1.3.0.jar /app/app.jar

WORKDIR /app

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]