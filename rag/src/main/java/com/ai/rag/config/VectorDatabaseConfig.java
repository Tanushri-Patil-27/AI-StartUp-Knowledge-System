package com.ai.rag.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class VectorDatabaseConfig {

    /*
     * Qdrant is configured through application.properties.
     *
     * Spring AI automatically creates the VectorStore bean
     * using the Qdrant configuration.
     *
     * Required properties:
     *
     * spring.ai.vectorstore.qdrant.host=localhost
     * spring.ai.vectorstore.qdrant.port=6334
     * spring.ai.vectorstore.qdrant.collection-name=startup_knowledge
     * spring.ai.vectorstore.qdrant.initialize-schema=true
     * spring.ai.vectorstore.qdrant.use-tls=false
     */
}