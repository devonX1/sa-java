FROM openjdk:22-jdk

WORKDIR /app
--Команда copy копирует и переименовывает файл
COPY target/*.jar sa-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "sa-0.0.1-SNAPSHOT.jar"]