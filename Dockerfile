FROM openjdk:19
ARG JAR_FILE=target/*.jar
COPY $JAR_FILE app.jar
ENTRYPOINT ["java","-jar", "/app.jar"]

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]