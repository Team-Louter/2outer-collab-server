package com.louter.collab.common.service.impl;

import com.louter.collab.common.properties.FileStorageProperties;
import com.louter.collab.common.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Basic file type validation (Block dangerous extensions)
            String fileExtension = StringUtils.getFilenameExtension(fileName);
            if (fileExtension != null && isDangerousExtension(fileExtension)) {
                throw new RuntimeException("Sorry! File type not allowed: " + fileExtension);
            }

            String newFileName = UUID.randomUUID().toString() + "_" + fileName;
            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return newFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    private boolean isDangerousExtension(String extension) {
        String ext = extension.toLowerCase();
        return ext.equals("exe") || ext.equals("sh") || ext.equals("bat") || ext.equals("cmd") 
                || ext.equals("jsp") || ext.equals("php") || ext.equals("js") || ext.equals("html");
    }
}
