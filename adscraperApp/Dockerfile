FROM centos
RUN yum install -y java-1.8.0-openjdk
EXPOSE 8080
ADD target/*.jar /opt
ADD target/dependency /opt/dependency
ENTRYPOINT ["java","-jar","/opt/adscraperapp-0.1.0.jar"]
