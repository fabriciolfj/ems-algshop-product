package com.algaworks.algashop.product.catalog;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.mongodb.MongoDBContainer;

@TestConfiguration
public class TestcontainerMongoDbConfig {

    private static final MongoDBContainer MONGO_DB_CONTAINER = new MongoDBContainer(
            "mongo:8")
            .withCommand("--replSet", "rs0");

    static  {
        MONGO_DB_CONTAINER.start();

        try {
            MONGO_DB_CONTAINER.execInContainer(
                    "mongosh",
                    "--eval",
                    """
                        rs.initiate({
                            _id: "rs0",
                            members: [ { _id:0, host: "localhost:27017" } ]
                        })
                    """
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    @ServiceConnection
    public MongoDBContainer mongoDBContainer() {
        return MONGO_DB_CONTAINER;
    }
}
