#
# Build stage
#
FROM maven:3.8.4-openjdk-17 AS maven
WORKDIR /usr/src/app
COPY . /usr/src/app
# build the app (no dependency download here). Since testing will be an extra step in the pipeline we skip it here.
RUN mvn clean package -Dmaven.test.skip

#
# Package stage
#
FROM openjdk:17-oracle
ARG JAR_FILE=apotheke_stoller-0.0.1.jar
WORKDIR /opt/app
# Copy the .jar from the maven stage to the /opt/app directory of the current stage.
COPY --from=maven /usr/src/app/target/${JAR_FILE} /opt/app/
ENTRYPOINT ["java","-jar","apotheke_stoller-0.0.1.jar"]

#
# Notice that we are using two FROM in the Dockerfile, which we call multi-stage builds. Multi-stage builds can help to optimize the docker image. # We copy the built jar file from stage one which is maven and store only the jar file in the current working directory. Then, we discard the local # Maven repositories and class files generated in the target directory.
#
