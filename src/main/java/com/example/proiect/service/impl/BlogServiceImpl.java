package com.example.proiect.service.impl;

import com.example.proiect.exception.NotFoundException;
import com.example.proiect.model.Blog;
import com.example.proiect.model.User;
import com.example.proiect.repository.BlogRepository;
import com.example.proiect.repository.UserRepository;
import com.example.proiect.service.BlogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("blogService")
@AllArgsConstructor
@Slf4j
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Override
    public List<Blog> findAll() {
        List<Blog> blogs = new LinkedList<>();
        blogRepository.findAll().iterator().forEachRemaining(blogs::add);
        return blogs;
    }

    @Override
    public Blog save(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public Page<Blog> findPaginated(Pageable pageable) {
        List<Blog> blogs = findAll();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Blog> list;
        if (blogs.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, blogs.size());
            list = blogs.subList(startItem, toIndex);
        }
        Page<Blog> blogPage = new PageImpl<>(list, PageRequest.of(currentPage,
                pageSize), blogs.size());
        return blogPage;
    }

    @Override
    public Page<Blog> findPaginatedAndSorted(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Blog findById(Long blogId) {
        Optional<Blog> blogOptional = blogRepository.findById(blogId);
        if(blogOptional.isEmpty()) {
            log.info("Error when trying to find the blog with id {}", blogId);
            throw new NotFoundException("Blog with id " + blogId + "not found!");
        }
        log.info("Returned blog with id {}", blogId);
        return blogOptional.get();
    }

    @Override
    public void deleteById(Long blogId) {
        Optional<Blog> blogOptional = blogRepository.findById(blogId);
        if(blogOptional.isEmpty()) {
            throw new NotFoundException("Blog with id: " + blogId + "not found");
        }

        Blog blog = blogOptional.get();
        Set<User> users = new LinkedHashSet<>();
        if(blog.getUsersLike() != null) {
            blog.getUsersLike().iterator().forEachRemaining(users::add);
        }

        for(User user : users) {
            blog.removeUser(user);
        }
        blogRepository.save(blog);
        blogRepository.deleteById(blogId);
        log.info("Successfully deleted blog with id {}", blogId);
    }

    @Override
    public List<Blog> findLastPosted() {
        List<Blog> allBlogsSorted = findAll().stream()
                .sorted(Comparator.comparing(Blog::getDate))
                .collect(Collectors.toList());
        if (allBlogsSorted.size() >= 3 ) {
            return allBlogsSorted.subList(0,3);
        }
        return allBlogsSorted;
    }

    @Override
    public Page<Blog> findByTitle(String searchInput, Pageable pageable) {
        return blogRepository.findByTitle(searchInput, pageable);
    }

    @Override
    public Boolean isLiked(Long blogId, String username) {
        Blog blog = findById(blogId);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()) {
            throw new NotFoundException("User with username: " +  username + "not found");
        }

        return userOptional.get().getLikedBlogs().contains(blog);
    }

    @Override
    public Long getNoLikes(Long blogId) {
        return blogRepository.getNoLikes(blogId);
    }

}
