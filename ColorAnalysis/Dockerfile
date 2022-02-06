FROM openjdk:17-alpine3.14
MAINTAINER slashuzero
COPY target/ColorAnalysis-1.0.0.jar ColorAnalysis-1.0.0.jar
EXPOSE 8081:8081


ENTRYPOINT ["java","-jar","/ColorAnalysis-1.0.0.jar"]