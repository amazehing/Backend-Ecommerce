package com.metinbudak.ecommerce.controller;

import com.metinbudak.ecommerce.service.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;

@RestController
public class ImageController {

    private ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/images")
    public ResponseEntity<Long> saveImage(@RequestParam("file") MultipartFile file) throws IOException {
        long imageId = imageService.saveImage(file);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(imageId).toUri();

        return ResponseEntity.created(location).body(imageId);
    }

    @GetMapping("/images/{id}")
    @ResponseBody
    public ResponseEntity<Resource> loadImage(@PathVariable long id) {
        Resource file = imageService.loadImage(id);

        // Determine the content type based on file extension or content type
        MediaType mediaType;
        try {
            String contentType = Files.probeContentType(file.getFile().toPath());
            mediaType = MediaType.parseMediaType(contentType);
        } catch (IOException e) {
            // If content type cannot be determined, default to a generic image type
            mediaType = MediaType.IMAGE_JPEG;
        }

        return ResponseEntity
                .ok()
                .contentType(mediaType) // Adjust content type based on your image type
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

}
