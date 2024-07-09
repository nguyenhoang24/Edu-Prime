package com.eduprime.eduprimeserver.controllers;

import com.eduprime.eduprimeserver.config.FileStorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class ReadFileController {

    private final FileStorageProperties fileStorageProperties;

    @GetMapping("/pdfs")
    public ResponseEntity<byte[]> viewPdf(@RequestParam String filePath) {
        try {
            String correctedFilePath = filePath; // Thêm "uploads/" vào đường dẫn
            log.info("????" + correctedFilePath);
            Path path = Paths.get(correctedFilePath);
            byte[] pdfContent = Files.readAllBytes(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/images/{fileName}")
    public ResponseEntity<Resource> viewImage(@PathVariable String fileName) {
        try {
            // Đường dẫn tới thư mục lưu trữ file
            Path fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir().getImage()).toAbsolutePath().normalize();
            Path filePath = fileStorageLocation.resolve(fileName).normalize();

            log.info("Loading image from path: " + filePath);

            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                String contentType = Files.probeContentType(filePath);

                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (MalformedURLException e) {
            log.error("Error reading file: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            log.error("Error determining file type: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
