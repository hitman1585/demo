package com.example.backstage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class OpenApiGenerator {

    private final RestTemplate restTemplate;
    private final String apiDocsUrl;

    @Autowired
    public OpenApiGenerator(RestTemplate restTemplate,
                            @Value("${springdoc.api-docs.path:/v3/api-docs}") String apiDocsPath,
                            @Value("8080") int serverPort) {
        this.restTemplate = restTemplate;
        // Construct the full URL for the OpenAPI docs endpoint
        this.apiDocsUrl = "http://localhost:" + serverPort + apiDocsPath + ".yaml";
    }

    public String generate() {
        try {
            // Fetch OpenAPI schema from the running service
            String openApiSpec = restTemplate.getForObject(apiDocsUrl, String.class);
            if (openApiSpec == null || openApiSpec.isEmpty()) {
                throw new IllegalStateException("Failed to fetch OpenAPI specification");
            }
            return openApiSpec;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching OpenAPI specification", e);
        }
    }
}
