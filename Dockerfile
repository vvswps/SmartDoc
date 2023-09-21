FROM alpine:3.18

RUN apk add openjdk17-jre-headless

EXPOSE 8080

COPY target/SmartDocument-0.0.1-SNAPSHOT.jar /app.jar

CMD ["java", "-jar", "/app.jar"]
