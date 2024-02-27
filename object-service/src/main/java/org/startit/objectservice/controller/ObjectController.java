package org.startit.objectservice.controller;

import com.jlefebure.spring.boot.minio.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.startit.objectservice.transfer.FileResponse;
import org.startit.objectservice.service.ObjectService;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/objects", produces = {"application/json", "application/xml", "application/hal+json"})
public class ObjectController {

    private final ObjectService fileStorageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> fileUpload(@RequestPart("photo") MultipartFile file,
                                                   @RequestPart("itemId") String itemId) {
        try {
            FileResponse response = fileStorageService.addFile(file, itemId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/download/{itemId}")
    public ResponseEntity<Object> downloadFile(@PathVariable String itemId) {
        try {
            FileResponse source = fileStorageService.getFile(itemId);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(source.getFileSize())
                    .header("Content-disposition", "attachment; filename=" + source.getFilename())
                    .body(source.getStream());
        } catch (MinioException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}