package in.co.gw.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Value("${profile.image-dir}")
    private String IMAGE_DIR;

    @Value("${static.image-dir}")
    private String staticImagedir;

    @Value("${app.base-url:https://localhost:8083}")
    private String baseUrl;

    @GetMapping("")
    public ResponseEntity<?> getImageNoFilename() {
        return ResponseEntity.badRequest().body(Map.of("error", "Filename is required"));
    }

    @GetMapping("/")
    public ResponseEntity<?> getImageEmptyPath() {
        return ResponseEntity.badRequest().body(Map.of("error", "Filename is required"));
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<?> getImage(@PathVariable String filename) throws Exception {
        System.out.println("filename : " + filename);
        if (filename == null || filename.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Filename is required"));
        }
        Path filePath = Paths.get(IMAGE_DIR).resolve(filename);
        System.out.println("ImageController.getImage()");
        System.err.println("file path : " + filePath);

        if (!filePath.toFile().exists()) {
            return ResponseEntity.status(404).body(Map.of("error", "Image not found"));
        }

        System.out.println(baseUrl);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = getContentType(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    private String getContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            default:
                return "application/octet-stream";
        }
    }
}
