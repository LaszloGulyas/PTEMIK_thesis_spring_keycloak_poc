FROM openjdk:17-oracle
VOLUME /var/lib/springapp/data
EXPOSE 8080
COPY ./target/spring-rest-server-0.0.1-SNAPSHOT.jar spring-rest-server-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","spring-rest-server-0.0.1-SNAPSHOT.jar"]
