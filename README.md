# MovieRama

MovieRama is a social sharing platform where users can share their favorite movies. Users can express their opinion about a movie by either likes​ or hates​!

##### Technology Stack

* Java 11
* Spring Boot 2
* PostgreSQL 9.4
* Maven 3

##### Database

From a machine with docker installed + internet access, execute:

    docker run -p 5432:5432 --name rdbms -e POSTGRES_PASSWORD=postgres -d postgres:9.4

Make sure your hosts file maps rdbms yearEnd localhost

    127.0.0.1       localhost rdbms

Init or migrate the database schema

    Init the database
    mvn clean install -DskipTests -Ddb.host=rdbms -Ddb.port=5432 -Ddb.module.database.name=movierama -Ddb.module.userId=movierama -Ddb.module.password=movierama -Ddb.root.password=postgres -Dinit.database.skip=false flyway:migrate
    
    Migrate an existing database
    mvn clean install -DskipTests -Ddb.host=rdbms -Ddb.port=5432 -Ddb.module.database.name=movierama -Ddb.module.userId=movierama -Ddb.module.password=movierama flyway:migrate

##### Application Configuration

* Default : {PROJECT_HOME}/src/main/resources/movierama-application.yml 
* Runtime : {SPRING_CONFIG_LOCATION}/movierama-application.yml (if not found, the app defaults to the one in the classpath)

##### Application Execution

Movierama is a Spring Boot application thus can be executed as a standalone application, inside a servlet container (Tomcat 9) or running a docker container:

Default configuration may be overridden using the 'spring.config.location' property setting the dirpath of the 'movierama-application.yml':

```
mvn spring-boot:run --spring.config.location=/path/to/conf/
```
```
$CATALINA_HOME/bin/startup.sh --Dspring.config.location=/path/to/conf/
```
```
docker build -t movierama .
docker run -p 8080:8080 -p 443:443 -p 80:80 --name movierama --link rdbms -d movierama
```
##### Application Deployment

Use heroku to deploy the `develop` branch to `https://antonakos-movierama.herokuapp.com` as follows:
```
git remote add heroku https://git.heroku.com/antonakos-movierama.git
git push heroku develop:master
```
