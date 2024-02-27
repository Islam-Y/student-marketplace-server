package org.startit.objectservice.service;

import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.startit.objectservice.entity.PhotoEntity;
import org.startit.objectservice.mapper.FileResponseMapper;
import org.startit.objectservice.repository.PhotoRepo;
import org.startit.objectservice.transfer.FileResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ObjectService {
    private final MinioService minioService;
    private final FileResponseMapper fileResponseMapper;
    private final PhotoRepo photoRepo;

    public FileResponse addFile(MultipartFile file, String itemId) {
        Path path = Path.of(file.getOriginalFilename());
        try {
            minioService.upload(path, file.getInputStream(), file.getContentType());
            var metadata = minioService.getMetadata(path);
            log.info("this file {} storage in bucket: {} on date: {}", metadata.name(), metadata.bucketName(), metadata.createdTime());

            if (photoRepo.findByItemId(itemId).isPresent()) {
                throw new IllegalStateException("The item with given itemId already has an image associated!");
            }

            var photoEntity = new PhotoEntity();
            photoEntity.setFilename(metadata.name());
            photoEntity.setItemId(itemId);
            photoRepo.save(photoEntity);
            return fileResponseMapper.fileAddResponse(metadata);
        } catch (IOException | MinioException ex) {
            throw new IllegalStateException(ex.getMessage());
        }

    }

    public FileResponse getFile(String itemId) throws MinioException, NoSuchElementException {
        var filename = photoRepo.findByItemId(itemId).orElseThrow().getFilename();

        Path path = Path.of(filename);
        var metadata = minioService.getMetadata(path);

        InputStream inputStream = minioService.get(path);
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        return FileResponse.builder()
                .filename(metadata.name())
                .fileSize(metadata.length())
                .contentType(metadata.contentType())
                .createdTime(metadata.createdTime())
                .stream(inputStreamResource)
                .build();
    }
}
