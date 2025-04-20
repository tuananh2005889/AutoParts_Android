package com.BackEnd.controller;
import com.BackEnd.service.CloudinaryyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/upload")
@CrossOrigin(origins="*")
public class CloudinaryyController {

    private final CloudinaryyService cloudinaryyService;

    public CloudinaryyController(CloudinaryyService cloudinaryyService) {
        this.cloudinaryyService = cloudinaryyService;
    }

    @PostMapping("/multiple-image")
    public ResponseEntity<?> uploadImage(@RequestParam("files")List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "No files provided")
            );
        }
        try {
            // kiem tra dung dinh dang image khong
            for (MultipartFile file : files) {
                if (!file.getContentType().startsWith("image/")) {
                    return ResponseEntity.badRequest().body(
                            Map.of("error", "Only image files are allowed")
                    );
                }
            }

            List<String> imageUrl = cloudinaryyService.uploadImages(files);
            return ResponseEntity.ok().body(
                    Map.of("urls", imageUrl)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", "Upload failed", "message", e.getMessage())
            );
        }
    }
}
