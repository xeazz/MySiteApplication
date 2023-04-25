FROM openjdk:17-jdk-slim-buster
EXPOSE 5500
ADD target/MySite-0.0.1-SNAPSHOT.jar myapplication.jar
CMD ["java", "-jar", "/myapplication.jar"]