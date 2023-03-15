package com.example.proiect.service;

import com.example.proiect.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogService {
    List<Blog> findAll();
    Blog save(Blog blog);
    Page<Blog> findPaginated(Pageable pageable);
    Page<Blog> findPaginatedAndSorted(Pageable pageable);
    Blog findById(Long blogId);
    void deleteById(Long blogId);
    List<Blog> findLastPosted();
    Page<Blog> findByTitle(String searchInput, Pageable pageable);

    Boolean isLiked(Long blogId, String username);
    Long getNoLikes(Long blogId);
}
