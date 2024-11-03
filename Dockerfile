FROM amazoncorretto:21.0.5
ARG JAR_FILE=target/*.jar
RUN mkdir -p /serb-alert
COPY ${JAR_FILE} /serb-alert/app.jar
COPY ./entrypoint.sh /serb-alert/entrypoint.sh
RUN chmod +x /serb-alert/entrypoint.sh
CMD ["/serb-alert/entrypoint.sh"]