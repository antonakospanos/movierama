FROM maven:3.5-jdk-11 as build-stage
COPY . /usr/src/myapp
RUN mvn -f /usr/src/myapp/pom.xml clean package

# Shipping image: Tomcat + movierama artifact
FROM tomcat:9-jre11
RUN apt-get update && apt-get install -y vim less telnet
LABEL maintainer="antonakospanos.gmail.com"

# Provision Tomcat with movierama webapp and server confs
COPY src/main/resources/ssl/movierama.keystore $CATALINA_HOME/conf/localhost-rsa.jks
COPY src/conf/tomcat/server.xml $CATALINA_HOME/conf/server.xml
COPY src/conf/tomcat/tomcat-users.xml $CATALINA_HOME/conf/tomcat-users.xml
COPY src/conf/tomcat/setenv.sh $CATALINA_HOME/bin/setenv.sh
COPY --from=build-stage /usr/src/myapp/target/movierama*.jar $CATALINA_HOME/webapps/movierama.jar

# Add env variables
ENV SPRING_CONFIG_LOCATION $CATALINA_HOME/conf/

# Expose Ports
EXPOSE 8080
EXPOSE 80
EXPOSE 443
