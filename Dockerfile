FROM openjdk:17-oracle
VOLUME /var/lib/springapp/data
EXPOSE 8080
COPY ./target/spring-keycloak-poc-0.0.1-SNAPSHOT.jar spring-keycloak-poc-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","spring-keycloak-poc-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]
