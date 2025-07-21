FROM openjdk:21-jdk-slim

ENV TZ=Asia/Bishkek

WORKDIR /app

COPY target/MiniTwitter-1.0.jar /app/mini-twitter.jar

CMD ["java", "-jar", "mini-twitter.jar"]