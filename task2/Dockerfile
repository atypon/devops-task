FROM maven:3.5.4-jdk-8-alpine as mvn_builder_stage1
EXPOSE 8080
WORKDIR /myapp
COPY scripts/devops-task/APP/src /myapp/src
COPY scripts/devops-task/APP/src /myapp/pom.xml
RUN mvn clean install

FROM openjdk:8-jre-alpine as mvn_runner_stage2

ARG JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n"
ARG network="host"
ARG IMAGE=""
ARG REPO=""
ARG PATH=""
ARG TAG="v1.0"

WORKDIR /myrunningapp
EXPOSE 8080
COPY --from=mvn_builder_stage1 /myapp .
CMD java $JAVA_OPTS -D -jar target/app-*.jar