package com.moviebooking.system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.Arrays;
import java.util.List;

@Service
public class FileUploadService {

    @Value("${app.upload.dir:${user.home}/moviebooking/uploads}")
    private String uploadDir;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "webp"
    );

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public String uploadMoviePoster(MultipartFile file) throws IOException {
        return uploadFile(file, "movies/posters");
    }

    public String uploadMovieBackdrop(MultipartFile file) throws IOException {
        return uploadFile(file, "movies/backdrops");
    }

    public String uploadTheaterImage(MultipartFile file) throws IOException {
        return uploadFile(file, "theaters");
    }

    public String uploadUserAvatar(MultipartFile file) throws IOException {
        return uploadFile(file, "users/avatars");
    }

    private String uploadFile(MultipartFile file, String category) throws IOException {
        // Validate file
        validateFile(file);

        // Create directories if not exist
        Path categoryDir = Paths.get(uploadDir, category);
        Files.createDirectories(categoryDir);

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + "." + extension;

        // Save file
        Path targetPath = categoryDir.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // Return URL path
        return "/uploads/" + category + "/" + uniqueFilename;
    }

    private void validateFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("File size exceeds maximum limit of 5MB");
        }

        String extension = getFileExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IOException("File type not allowed. Allowed types: " + ALLOWED_EXTENSIONS);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public boolean deleteFile(String filePath) {
        try {
            if (filePath.startsWith("/uploads/")) {
                Path path = Paths.get(uploadDir, filePath.substring("/uploads/".length()));
                return Files.deleteIfExists(path);
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    // Helper method to get full file path
    public String getFullPath(String relativePath) {
        if (relativePath.startsWith("http")) {
            return relativePath; // External URL
        }
        return relativePath; // Local path, will be served by Spring Boot
    }
}