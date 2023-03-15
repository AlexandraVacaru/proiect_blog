package com.example.proiect.service;

import com.example.proiect.ProiectApplication;
import com.example.proiect.exception.NotFoundException;
import com.example.proiect.model.*;
import com.example.proiect.repository.BlogRepository;
import com.example.proiect.repository.CommentRepository;
import com.example.proiect.repository.RoleRepository;
import com.example.proiect.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ProiectApplication.class})
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("h2")
public class BlogServiceIntegrationTest {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    BlogService blogService;

    @BeforeEach
    public void setup() {
        Role role_user = Role.builder()
                .name(RoleEnum.ROLE_USER)
                .build();
        Role role_admin = Role.builder()
                .name(RoleEnum.ROLE_ADMIN)
                .build();

        Set<Role> allRoles = Set.of(role_admin, role_user);

        User user1 = User.builder()
                .username("user1")
                .email("user1@yahoo.com")
                .password("password")
                .birthDate(LocalDate.parse("1999-09-18"))
                .lastName("user")
                .firstName("user")
                .roles(allRoles)
                .build();
        User user2 = User.builder()
                .username("user2")
                .email("user2@yahoo.com")
                .password("password")
                .birthDate(LocalDate.parse("1999-09-18"))
                .lastName("user")
                .firstName("user")
                .roles(Set.of(role_user))
                .build();

        Blog blog1 = Blog.builder()
                .blogId(1L)
                .category(CategoryEnum.OTHER)
                .title("Blog1")
                .content("blogContent")
                .pictures(new ArrayList<>())
                .user(user1)
                .build();

        Blog blog2 = Blog.builder()
                .blogId(2L)
                .category(CategoryEnum.OTHER)
                .pictures(new ArrayList<>())
                .content("blogContent")
                .title("Blog2")
                .user(user2)
                .build();

        Comment comment1 = Comment.builder()
                .content("comment1")
                .user(user1)
                .blog(blog1)
                .build();

        Comment comment2 = Comment.builder()
                .content("comment2")
                .user(user1)
                .blog(blog2)
                .build();

        roleRepository.save(role_admin);
        roleRepository.save(role_user);
        userRepository.save(user1);
        userRepository.save(user2);
        blogRepository.save(blog1);
        blogRepository.save(blog2);
        commentRepository.save(comment1);
        commentRepository.save(comment2);

    }

    @Test
    void findAll() {
        List<Blog> result = blogService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findById() {
        Long blogId = 1L;
        Blog result = blogService.findById(blogId);
        assertNotNull(result);
        assertEquals(CategoryEnum.OTHER, result.getCategory());
        assertEquals("Blog1", result.getTitle());
    }

    @Test
    void save() {
        Optional<User> user = userRepository.findById(1L);
        if(user.isEmpty()) {
            throw new NotFoundException("user not found");
        }
        Blog blog = Blog.builder()
                .blogId(1L)
                .category(CategoryEnum.OTHER)
                .title("Blog1")
                .content("blogContent")
                .pictures(new ArrayList<>())
                .user(user.get())
                .build();
        Blog result = blogService.save(blog);
        assertNotNull(result);
        assertEquals(blog.getTitle(), result.getTitle());
    }

    @Test
    void findPaginated() {
        Page<Blog> result = blogService.findPaginated(PageRequest.of(1,4));
        assertNotNull(result);
        assertEquals(2,result.getTotalElements());
    }

    @Test
    void findByIdHappyFlow() {
        Blog result = blogService.findById(1L);
        assertNotNull(result);
        assertEquals(1, result.getBlogId());
        assertEquals("Blog1", result.getTitle());
    }

    @Test
    void findByIdThrowsException() {
        NotFoundException result = assertThrows(NotFoundException.class, () -> blogService.findById(100L));
        assertNotNull(result);
        assertEquals("Blog with id " + 100L + "not found!", result.getMessage());

    }

    @Test
    void getNoLikes() {
        Long result = blogService.getNoLikes(1L);
        assertNotNull(result);
        assertEquals(0L, result);
    }

}
