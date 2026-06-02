package com.krisnaajiep.urlshortening.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public interface ITContainers {
    @Container
    @ServiceConnection
    MySQLContainer mysqlContainer = new MySQLContainer(DockerImageName.parse("mysql:8.0.45-debian"))
            .withConnectTimeoutSeconds(300);
}
