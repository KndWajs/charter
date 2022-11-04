#FROM openjdk:17.0.2-slim as builder
#WORKDIR application
#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} application.jar
#RUN java -Djarmode=layertools -jar application.jar extract
#
#FROM openjdk:17.0.2-slim
#WORKDIR application
#COPY --from=builder application/dependencies/ ./
#COPY --from=builder application/spring-boot-loader/ ./
#COPY --from=builder application/snapshot-dependencies/ ./
#COPY --from=builder application/application/ ./
#ENTRYPOINT ["java","-jar","application.jar", "-Dspring.profiles.active=dev"]


FROM openjdk:17.0.2-slim
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8055
ENTRYPOINT ["java","-Dspring.profiles.active=dev", "-jar","/app.jar"]
