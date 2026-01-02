package com.mingeso.apigateway.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
@CrossOrigin("*")
public class ImageController {

    private static final String UPLOAD_DIR = "/app/uploads";

    /**
     * Get a specific image by filename
     * GET /api/images/{filename}
     */
    @GetMapping("/{filename}")
    public ResponseEntity<?> getImage(@PathVariable String filename) {
        try {
            // Validate filename to prevent directory traversal
            if (filename.contains("..") || filename.contains("/")) {
                return ResponseEntity.badRequest().body("Invalid filename");
            }

            Path imagePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();

            // Verify the path is within the upload directory
            if (!imagePath.getParent().equals(Paths.get(UPLOAD_DIR).normalize())) {
                return ResponseEntity.badRequest().body("Invalid file path");
            }

            if (!Files.exists(imagePath)) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = Files.readAllBytes(imagePath);
            String mediaType = getMediaType(filename);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mediaType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(new ByteArrayResource(imageBytes));
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get media type based on file extension
     */
    private String getMediaType(String filename) {
        String lowercaseFilename = filename.toLowerCase();

        if (lowercaseFilename.endsWith(".jpg") || lowercaseFilename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowercaseFilename.endsWith(".png")) {
            return "image/png";
        } else if (lowercaseFilename.endsWith(".gif")) {
            return "image/gif";
        } else if (lowercaseFilename.endsWith(".webp")) {
            return "image/webp";
        }

        return "application/octet-stream";
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Image service is running");
    }
}
