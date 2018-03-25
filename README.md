# Description
Back-end part of application for CRUD stocks maintenance. 

# Overview
Project consists of two parts:
* back-end that is build on top of Spring Boot
* front-end that is build using Angular and Material

Front-end part can be found via [this link](https://github.com/DimaLegeza/stocks-frontend)

# API
Method | Endpoint | Description
------ | -------- | -----------
GET | /stocks | Endpoint to get list of stocks<br>* **page** - to provide page number for pageable response<br>* **size** - number of elements per page<br>* **sort** - optional parameter, contains name and ordering direction, e.g.: ```&sort=name,desc```<br> * **q** - special parameter for filtering, supports one or multiple criterions with 'equals', 'like', 'greater' and 'smaller' comparators
GET | /stocks/{id} | Gets complete stock entity. [Bad request](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400) code will be returned in case stock id could not be found in database
POST | /stocks | Creates new stock in database
PUT | /stocks/{id} | Update existing stock in database. [Bad request](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400) code will be returned in case stock id could not be found in database 

# Dependencies
Production environment: JRE 8
Development environment: JDK 8

# Technological stack
REST back-end was build on top of spring-boot-starter, so following technologies used:
* Spring web for REST API and serving endpoints
* Spring JPA with Hibernate for persistence layer
* H2 database with query compatibility to Postgres
* Liquibase as DB DDL and DML versioning mechanism

# Running on developer machine
**Running with IDE:**
* Execute StocksApplication

**Running using gradle:**
* run ```gradlew clean bootRun```

# Docker
As production-ready solution, back-end docker image can be build on top of alpine-openjdk8 image

**Building Docker image:**
```gradlew clean distDocker```

**Running Docker image:**
Project has pre-build docker-compose file that will run both back-end and front-end in integrated environment.
To run with docker-compose:
```docker-compose up```
To run back-end separately:
```docker run --name=stocks-backend -p 8080:8080 com.dlegeza/stocks-backend:1.0.0```