# How to
## Run the application
- start the database docker by running `docker-compose up&`
- run the gradle task `fastCopyDistFolder` to copy all html sites into the backend
- start the application or run the gradle task `bootRun`
- the default user has username `user` and password `password`
- the default admin has username `admin` and password `password`
  The default user and admin only work on local machine
## Cleanup docker and the database
- To stop the database: `docker-compose down`
- To delete all volumes (the data in the database): `docker volume rm $(docker volume ls -q)`
- To delete all images/containers/networks/etc: `docker system prune -a`
# Technology stack
## Architecture
We decided to use a classical monolith since we are a very inexperienced team. However the frontend is still developed separately and then copied into the backend. The whole thing then gets packaged and shipped as one .jar file.
## SpringBoot
We use SpringBoot with all it's necessary sub components (until now web, JPA and security). Spring Web allows us to define HTTP endpoints in an easy and fast manner and provides us with powerful test tools. JPA is a database ORM from Hibernate. It allows us to map table columns to Java objects and write database queries in almost natural language. With just a few lines of code we can integrate any database and therefore JPA is very powerful once you understand the magic behind it. Spring security adds a security filter in front of our endpoints and allows for almost all authentication methods ( basic auth, formLogin, OAuth, etc)
## Database and surrounding ecosystem
We use docker to provide a database on our local development machines. This allows us to switch between databases easily or throw them away and set them up again in a few seconds if it is needed.
For the actual database we use postgreSQL. For no particular reason it could have been any other relational database as well. We decided against NoSQL databases as they are better used with dynamically typed languages.
For the integration tests we use the H2  in memory database since we didn't want to setup Testcontainers. Also they slow down the start of tests enormously. H2 is close enough to any other relational DB and since we don't use special functionality from postgres we should be fine.
Liquibase is used to create and migrate tables.
## Testing
The spring-starter-test comes with a lot of test libraries. A few notable are:
- JUnit5 as the test framework
- Mockito for mocking infrastructure and services
- hamcrest.Matchers which provide powerful assertion methods
