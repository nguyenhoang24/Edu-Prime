package com.eduprime.eduprimeserver.controllers;

import com.eduprime.eduprimeserver.services.UploadsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/uploads/")
@RequiredArgsConstructor
public class UploadsController {

    private final UploadsService uploadsService;

    @PostMapping("/images")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
//        String fileName = uploadsService.storeFile(file);
        return ResponseEntity.ok(this.uploadsService.storeFile(file));
    }
}
