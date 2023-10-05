FROM openjdk:17
WORKDIR /app
COPY /target/commerce-1.0.0.jar /app/
ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=default", "/app/commerce-1.0.0.jar" ]