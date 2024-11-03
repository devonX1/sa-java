#!/usr/bin/env sh


echo "Running a Java Application with Restrictions on Heroku..."
# Запуск Java-приложения с использованием параметров контейнера
exec /usr/bin/java -XX:+UseContainerSupport -Xmx256m -Xss512k -XX:MetaspaceSize=100m -jar /serb-alert/app.jar
