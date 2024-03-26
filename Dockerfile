FROM eclipse-temurin:21
LABEL authors="tutran"

WORKDIR /app

COPY target/SpringBlog-0.0.1-SNAPSHOT.jar /app/SpringBlog.jar

ENTRYPOINT ["java", "-jar", "SpringBlog.jar"]