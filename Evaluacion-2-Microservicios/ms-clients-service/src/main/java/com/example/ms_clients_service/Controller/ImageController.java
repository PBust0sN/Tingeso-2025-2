package com.example.ms_clients_service.Controller;

import com.example.ms_clients_service.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/images")
@CrossOrigin("*")
public class ImageController {
    
    @Autowired
    private ImageService imageService;
    
    /**
     * Get a specific image by filename
     * GET /api/images/{filename}
     */
    @GetMapping("/{filename}")
    public ResponseEntity<?> getImage(@PathVariable String filename) {
        try {
            ByteArrayResource resource = imageService.getImage(filename);
            String mediaType = imageService.getMediaType(filename);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mediaType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid filename: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get list of all available images
     * GET /api/images/
     */
    @GetMapping("/")
    public ResponseEntity<List<String>> getAllImages() {
        try {
            List<String> images = imageService.getAllImages();
            return ResponseEntity.ok(images);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Image service is running");
    }
}
