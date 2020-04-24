FROM openjdk:8-jdk-alpine

MAINTAINER pateluday07@gmail.com

VOLUME /tmp

COPY ./build/libs/user-management-mongodb-demo-0.0.1-SNAPSHOT.jar user-management-mongodb-demo-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-Dspring.data.mongodb.uri=mongodb://user_management_demo_mongodb/user-management-demo","-Djava.security.egd=file:/dev/./urandom","-jar","/user-management-mongodb-demo-0.0.1-SNAPSHOT.jar"]