# Spring Boot URL Shortening Service

> A simple URL Shortener API that helps shorten long URLs

## Table of Contents

- [General Info](#general-information)
- [Technologies Used](#technologies-used)
- [Features](#features)
- [Setup](#setup)
- [Usage](#usage)
- [Project Status](#project-status)
- [Acknowledgements](#acknowledgements)
- [License](#license)

## General Information

A simple RESTful API built with Spring Boot and MySQL that allows users to shorten long URLs using Base62 encoding. This 
API provide endpoints to create, retrieve, update, and delete short URLs. It also provides statistics on the number of 
times a short URL has been accessed.

## Technologies Used

- Java 21.0.11
- Spring Boot 4.0.6
- MySQL 8.0.45

## Features

- Create a new short URL.
- Retrieve an original URL from a short URL.
- Update an existing short URL.
- Delete an existing short URL.
- Get statistics on the short URL (number of times accessed).

# Setup

To run this project locally, you'll need:

- Java 21 or higher
- MySQL native install or using Docker

How to install:

1. Clone the repository

    ```bash
    git clone https://github.com/krisnaajiep/springboot-url-shortening-service
    ```

2. Change the current working directory.

   ```bash
   cd springboot-url-shortening-service

3. Copy and rename `.env.example` file.

   ```bash
   cp .env.example .env
   ```

4. Adjust environment variables in `.env` for database configuration.

    ```bash
    # Docker
    MYSQL_IMAGE_TAG=mysql:8.0.45-debian
    
    # MySQL
    MYSQL_ROOT_PASSWORD=
    MYSQL_DATABASE=url_shortening_db
    MYSQL_USER=url_shortening
    MYSQL_PASSWORD=
    
    # Spring Data
    SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/${MYSQL_DATABASE}
    SPRING_DATASOURCE_PASSWORD=${MYSQL_PASSWORD}
    SPRING_DATASOURCE_USERNAME=${MYSQL_USER}
    ```
    
    If you have docker installed locally, you can run `docker compose up` to start the database server using docker.
    If not, make sure mysql server is running locally on your machine and its configuration matches the `.env` file. 

5. Build the project.

    ```bash
    mvn clean package
    ```
   
6. Start the application.

    ```bash
    java -jar target/springboot-url-shortening-service-1.0.0.jar
    ```

## Usage

Once the application is running, you can interact with it using the following API endpoints:

- `POST /shorten`: Create a new short URL for the provided original URL.
- `GET /shorten/{shortCode}`: Retrieve the Short URL by short code parameter.
- `PUT /shorten/{shortCode}`: Update an existing original URL of Short URL by short code parameter.
- `DELETE /shorten/{shortCode}`: Delete an existing Short URL by short code parameter.
- `GET /shorten/{shortCode}/stats`: Get statistics for a short URL by short code parameter.

API documentation:

- [**Swagger UI**](https://krisnaajiep.github.io/springboot-url-shortening-service/)
- [**OpenAPI Specification**](https://github.com/krisnaajiep/springboot-url-shortening-service/blob/dev/docs/openapi.yaml)

## Project Status

Project is: _complete_.

[![CI](https://github.com/krisnaajiep/springboot-url-shortening-service/actions/workflows/maven.yml/badge.svg)](https://github.com/krisnaajiep/springboot-url-shortening-service/actions/workflows/maven.yml)

## Acknowledgements

This project was inspired by [roadmap.sh](https://roadmap.sh/projects/url-shortening-service).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.