package com.example.proiect.service.impl;

import com.example.proiect.exception.NotFoundException;
import com.example.proiect.model.Blog;
import com.example.proiect.model.Picture;
import com.example.proiect.repository.BlogRepository;
import com.example.proiect.repository.PictureRepository;
import com.example.proiect.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final BlogRepository blogRepository;
    private final PictureRepository pictureRepository;


    @Override
    @Transactional
    public void saveImageFile(Long blogId, MultipartFile[] images) throws IOException {

        Optional<Blog> blog = blogRepository.findById(blogId);

        if(blog.isPresent()) {
            List<Picture> pictures = blog.get().getPictures();

            if(pictures == null || pictures.size() == 0) {
                pictures = new ArrayList<>();
            }

            for (MultipartFile file: images) {
                Byte[] byteObjects = new Byte[file.getBytes().length];
                int i = 0; for (byte b : file.getBytes()){
                    byteObjects[i++] = b; }

                Picture picture = new Picture();
                picture.setPicture(byteObjects);
                picture.setBlog(blog.get());
                Picture savedPicture = pictureRepository.save(picture);
                pictures.add(savedPicture);
                blog.get().setPictures(pictures);
                blogRepository.save(blog.get());
            }

        } else {
            throw new NotFoundException("Blog with blogId " + blogId + " not found");
        }

    }

    @Override
    public Optional<Picture> findById(Long id) {
        return pictureRepository.findById(id);
    }

    @Override
    public void deleteById(Long pictureId) {
        Optional<Picture> picture = pictureRepository.findById(pictureId);
        if(picture.isEmpty()) {
            throw new NotFoundException("Picture not found!");
        }
        pictureRepository.deleteById(pictureId);
    }
}
