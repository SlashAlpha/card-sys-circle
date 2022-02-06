FROM openjdk:17-alpine3.14
MAINTAINER slashuzero

COPY target/StraightAnalysis-1.0.0.jar StraightAnalysis-1.0.0.jar
EXPOSE 8084:8084


ENTRYPOINT ["java","-jar","/StraightAnalysis-1.0.0.jar"]