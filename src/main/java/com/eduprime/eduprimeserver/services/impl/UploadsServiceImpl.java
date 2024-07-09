package com.eduprime.eduprimeserver.services.impl;

import com.eduprime.eduprimeserver.common.HttpStatusCodes;
import com.eduprime.eduprimeserver.common.HttpStatusMessages;
import com.eduprime.eduprimeserver.config.FileStorageProperties;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.exceptions.FileStorageException;
import com.eduprime.eduprimeserver.services.UploadsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class UploadsServiceImpl implements UploadsService {

    private Path fileStorageLocation;

    private final FileStorageProperties fileStorageProperties;
    @Override
    public BaseResponse<?> storeFile(MultipartFile file) {
        // Initialize file storage location
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir().getImage())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence %s ".formatted(fileName));
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

//            return fileName;
            return BaseResponse.of(fileName ,HttpStatusCodes.OK, HttpStatusMessages.OK +
                    " Uploads image successfully!");
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file %s. Please try again!".formatted(fileName), ex);
        }
    }
}
