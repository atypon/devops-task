FROM maven:3.5.4-jdk-8-alpine as mvn_builder_stage1
EXPOSE 8080
WORKDIR /myapp
COPY APP/src /myapp/src
COPY APP/pom.xml /myapp/pom.xml
RUN mvn clean install

FROM openjdk:8-jre-alpine as mvn_runner_stage2
WORKDIR /myrunningapp
ARG hostname=saifnuaimii/app:v1.0.0
EXPOSE 8080
COPY --from=mvn_builder_stage1 /myapp .
CMD java $JAVA_OPTS -D -jar target/app-*.jar