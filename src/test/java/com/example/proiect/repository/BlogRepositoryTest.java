package com.example.proiect.repository;


import com.example.proiect.exception.NotFoundException;
import com.example.proiect.model.Blog;
import com.example.proiect.model.CategoryEnum;
import com.example.proiect.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("mysql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class BlogRepositoryTest {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    public void addBlog() {
        Optional<User> user = userRepository.findById(1000L);
        if(user.isEmpty()) {
            throw new NotFoundException("user not found");
        }
        Blog blog = Blog.builder()
                .blogId(1L)
                .title("test")
                .content("BlogContent")
                .category(CategoryEnum.OTHER)
                .date(LocalDateTime.now())
                .user(user.get())
                .build();
        Blog result = blogRepository.save(blog);
        assertNotNull(result);
        assertEquals(blog.getTitle(), result.getTitle());
    }

    @Test
    @Order(2)
    public void findByName() {
        List<Blog> blogs = new LinkedList<>();
        blogRepository.findByTitle("blog", PageRequest.of(1, 4))
                .iterator().forEachRemaining(blogs::add);
        assertFalse(blogs.isEmpty());
        log.info("findByName ...");
        blogs.forEach(blog -> log.info(blog.getTitle()));
    }

    @Test
    @Order(3)
    public void getNoLikes() {
        log.info("get noLikes ");
        Long noLikes = blogRepository.getNoLikes(1000L);
        log.info("Having no likes");
        assertEquals(1, noLikes);
        log.info("{} likes for blog 1", noLikes);
    }


}
