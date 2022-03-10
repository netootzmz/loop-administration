FROM openjdk:8-alpine
VOLUME /tmp
EXPOSE 9095
ADD ./target/administration-1.0.jar administration.jar
ENTRYPOINT ["sh", "-c", "java -jar -Dspring.datasource.url=$DB_URL -Dspring.datasource.username=$DB_USER -Dspring.datasource.password=$DB_PASSWORD /administration.jar"]