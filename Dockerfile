FROM openjdk:8

# api
COPY ./target/pipa-0.0.1-SNAPSHOT.jar /opt/pipa.jar

# Ports we will expose
EXPOSE 8080

CMD ["java", "-jar", "/opt/pipa.jar"]
