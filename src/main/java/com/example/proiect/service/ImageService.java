package com.example.proiect.service;

import com.example.proiect.model.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ImageService {

    void saveImageFile(Long productId, MultipartFile[] images) throws IOException;

    Optional<Picture> findById(Long id);

    void deleteById(Long pictureId);
}
