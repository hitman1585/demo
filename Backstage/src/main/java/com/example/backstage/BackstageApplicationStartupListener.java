package com.example.backstage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

@Component
class BackstageApplicationStartupListener {

    private OpenApiGenerator openApiGenerator;
    private ServiceMetadata serviceMetadata;

    @Autowired
    public void ApplicationStartupListener(OpenApiGenerator openApiGenerator, ServiceMetadata serviceMetadata) {
        this.openApiGenerator = openApiGenerator;
        this.serviceMetadata = serviceMetadata;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        System.out.println("Application started. Generating OpenAPI Specification...");

        try {
            // Step 1: Generate OpenAPI spec using OpenApiGenerator
            String openApiSpec = openApiGenerator.generate();

            // Step 2: Save the OpenAPI specification to a file
            //saveOpenApiSpecToFile(openApiSpec);
            String backstageYaml = generateMetadataYaml(openApiSpec);
            saveYamlToFile(backstageYaml);

        } catch (Exception e) {
            System.err.println("Failed to generate OpenAPI Specification:");
            e.printStackTrace();
        }
    }

    private void saveOpenApiSpecToFile(String openApiSpec) throws IOException {
        // Define the file path and name (You can modify the file path as needed)
        File file = new File("openapi-spec_pankaj.json");

        // Create the file if it doesn't exist
        if (!file.exists()) {
            file.createNewFile();
        }

        // Write the OpenAPI specification JSON to the file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(openApiSpec);
            System.out.println("OpenAPI specification saved to file: " + file.getAbsolutePath());
        }
    }

//    private String createBackstageYaml(String openApiSpec) {
//        return "apiVersion: backstage.io/v1alpha1\n" +
//                "kind: API\n" +
//                "metadata:\n" +
//                "  name: petstore\n" +
//                "  description: The Petstore API\n" +
//                "spec:\n" +
//                "  type: openapi\n" +
//                "  lifecycle: production\n" +
//                "  owner: petstore@example.com\n" +
//                "  definition:\n" +
//                "    $text: file://openapi-spec.json";
//    }

    public String generateMetadataYaml(String openApiSpec) {
        StringBuilder yamlContent = new StringBuilder();
        yamlContent.append("apiVersion: backstage.io/v1alpha1\n")
                .append("kind: API\n")
                .append("metadata:\n")
                .append("  name: ").append(serviceMetadata.getName()).append("\n")
                .append("  description: ").append(serviceMetadata.getDescription()).append("\n")
                .append("spec:\n")
                .append("  type: openapi\n")
                .append("  lifecycle: ").append(serviceMetadata.getLifecycle()).append("\n")
                .append("  owner: ").append(serviceMetadata.getOwner()).append("\n")
                .append("  definition: |\n")
                .append("    ").append(openApiSpec).append("\n");

        return  yamlContent.toString();
    }

    private void saveYamlToFile(String backstageYaml) throws IOException {
        String fileName = serviceMetadata.getName() + "-backstage-api-metadata.yaml";
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        } else {
            String existingContent = Files.readString(file.toPath());
            if (existingContent.equals(backstageYaml)) {
                System.out.println("Content same");
            } else {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(backstageYaml);
                    System.out.println("Backstage metadata YAML saved to file: " + file.getAbsolutePath());
                }
            }
        }
    }
}
