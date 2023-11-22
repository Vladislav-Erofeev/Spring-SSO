FROM openjdk
ADD ./target/OauthAuthorizationServer-0.0.1-SNAPSHOT.jar prog.jar
ENTRYPOINT ["java", "-jar", "prog.jar"]