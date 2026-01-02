package com.example.ms_clients_service.Service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ImageService {
    
    private static final String UPLOAD_DIR = "/app/uploads";
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
    
    /**
     * Get image file by filename
     */
    public ByteArrayResource getImage(String filename) throws IOException {
        // Validate filename to prevent directory traversal
        if (filename.contains("..") || filename.contains("/")) {
            throw new IllegalArgumentException("Invalid filename");
        }
        
        Path imagePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
        
        // Verify the path is within the upload directory
        if (!imagePath.getParent().equals(Paths.get(UPLOAD_DIR).normalize())) {
            throw new IllegalArgumentException("Invalid file path");
        }
        
        if (!Files.exists(imagePath)) {
            throw new IOException("File not found: " + filename);
        }
        
        byte[] imageBytes = Files.readAllBytes(imagePath);
        return new ByteArrayResource(imageBytes);
    }
    
    /**
     * Get list of all available images
     */
    public List<String> getAllImages() throws IOException {
        List<String> images = new ArrayList<>();
        Path uploadPath = Paths.get(UPLOAD_DIR);
        
        if (!Files.exists(uploadPath)) {
            return images;
        }
        
        try (Stream<Path> paths = Files.list(uploadPath)) {
            paths.filter(Files::isRegularFile)
                 .filter(this::isImageFile)
                 .forEach(path -> images.add(path.getFileName().toString()));
        }
        
        return images;
    }
    
    /**
     * Check if file is an image
     */
    private boolean isImageFile(Path path) {
        String filename = path.getFileName().toString().toLowerCase();
        for (String ext : ALLOWED_EXTENSIONS) {
            if (filename.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get media type based on file extension
     */
    public String getMediaType(String filename) {
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
}
