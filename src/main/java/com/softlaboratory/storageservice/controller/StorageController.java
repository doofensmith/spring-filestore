package com.softlaboratory.storageservice.controller;

import com.softlaboratory.storageservice.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @GetMapping(value = "/get-all")
    public ResponseEntity<Object> getAllFile() {
        try {
            return storageService.getAllFile();
        }catch (Exception e) {
            throw e;
        }
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<Object> create(@RequestParam(name = "file") MultipartFile file) throws IOException {
        try {
            return storageService.uploadFile(file);
        } catch (Exception e) {
            throw e;
        }
    }

}
