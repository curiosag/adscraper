FROM openjdk:8-jre-alpine
RUN addgroup -g 1000 -S gonzo && \
    adduser -u 1000 -S gonzo -G gonzo
USER gonzo
EXPOSE 8080
ADD target/*.jar /opt/
ADD target/dependency /opt/dependency/
ENTRYPOINT ["java","-jar","/opt/adscraperapp-0.1.0.jar"]
