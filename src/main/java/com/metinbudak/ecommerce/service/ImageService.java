package com.metinbudak.ecommerce.service;

import com.metinbudak.ecommerce.exception.ImageNotFoundException;
import com.metinbudak.ecommerce.repository.ImageRepository;
import com.metinbudak.ecommerce.repository.domain.Image;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class ImageService {

    private Path rootLocation;
    private ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.rootLocation = Path.of(System.getProperty("user.dir"), "images");
        this.imageRepository = imageRepository;
    }

    public long saveImage(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), rootLocation.resolve(filename));
        Image image = new Image(filename);
        return imageRepository.save(image).getId();
    }

    public Resource loadImage(long imageId) {
        try {
            Image image = imageRepository.findById(imageId).orElseThrow(() -> new ImageNotFoundException());

            Path targetLocation = rootLocation.resolve(image.getLocation());

            if (Files.exists(targetLocation) && Files.isReadable(targetLocation)) {
                return new FileSystemResource(targetLocation);
            } else {
                throw new ImageNotFoundException();
            }
        } catch (Exception e) {
            throw new ImageNotFoundException();
        }
    }
}
