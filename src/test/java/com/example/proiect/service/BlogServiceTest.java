package com.example.proiect.service;

import com.example.proiect.exception.NotFoundException;
import com.example.proiect.model.Blog;
import com.example.proiect.model.CategoryEnum;
import com.example.proiect.model.User;
import com.example.proiect.repository.BlogRepository;
import com.example.proiect.repository.UserRepository;
import com.example.proiect.service.impl.BlogServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {

    @Mock
    BlogRepository blogRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    BlogServiceImpl blogService;

    @Test
    @DisplayName("Find all blogs")
    void findAll() {
        Blog blog1 = Blog.builder()
                .blogId(1L)
                .category(CategoryEnum.OTHER)
                .title("Blog1")
                .pictures(new ArrayList<>())
                .build();
        Blog blog2 = Blog.builder()
                .blogId(2L)
                .category(CategoryEnum.OTHER)
                .pictures(new ArrayList<>())
                .title("Blog2")
                .build();

        List<Blog> blogs = List.of(blog1, blog2);

        when(blogRepository.findAll()).thenReturn(blogs);

        List<Blog> result = blogService.findAll();
        assertEquals(2, result.size());
        verify(blogRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Find blog by id - happy flow")
    void findByIdHappyFlow() {
        Blog blog = Blog.builder()
                .blogId(1L)
                .category(CategoryEnum.OTHER)
                .title("Blog1")
                .pictures(new ArrayList<>())
                .build();

        when(blogRepository.findById(blog.getBlogId())).thenReturn(java.util.Optional.of(blog));

        Blog result = blogService.findById(blog.getBlogId());

        assertNotNull(result);
        assertEquals(blog.getTitle(), result.getTitle());
        verify(blogRepository, times(1)).findById(blog.getBlogId());

    }

    @Test
    @DisplayName("Find blog by id - blog not found")
    void findByIdThrowsException() {
        Long blogId = 1L;
        when(blogRepository.findById(blogId)).thenReturn(Optional.empty());

        NotFoundException result = assertThrows(NotFoundException.class, () -> blogService.findById(blogId));

        assertNotNull(result);
        assertEquals("Blog with id " + blogId + "not found!", result.getMessage());
    }

    @Test
    void isLiked() {
        Blog blog = Blog.builder()
                .blogId(1L)
                .category(CategoryEnum.OTHER)
                .title("Blog1")
                .pictures(new ArrayList<>())
                .build();

        User user = User.builder()
                .username("user1")
                .email("user1@yahoo.com")
                .password("password")
                .birthDate(LocalDate.parse("1999-09-18"))
                .lastName("user")
                .firstName("user")
                .likedBlogs(Set.of(blog))
                .build();

        when(blogRepository.findById(blog.getBlogId())).thenReturn(java.util.Optional.of(blog));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Boolean result = blogService.isLiked(blog.getBlogId(), user.getUsername());

        assertNotNull(result);
        assertTrue(result);

    }

    @Test
    void isNotLiked() {
        Blog blog = Blog.builder()
                .blogId(1L)
                .category(CategoryEnum.OTHER)
                .title("Blog1")
                .pictures(new ArrayList<>())
                .build();

        User user = User.builder()
                .username("user1")
                .email("user1@yahoo.com")
                .password("password")
                .birthDate(LocalDate.parse("1999-09-18"))
                .lastName("user")
                .firstName("user")
                .likedBlogs(new LinkedHashSet<>())
                .build();

        when(blogRepository.findById(blog.getBlogId())).thenReturn(java.util.Optional.of(blog));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Boolean result = blogService.isLiked(blog.getBlogId(), user.getUsername());

        assertNotNull(result);
        assertFalse(result);

    }

    @Test
    void isLikedThrowsException() {
        Blog blog = Blog.builder()
                .blogId(1L)
                .category(CategoryEnum.OTHER)
                .title("Blog1")
                .pictures(new ArrayList<>())
                .build();
        String username = "username";

        when(blogRepository.findById(blog.getBlogId())).thenReturn(java.util.Optional.of(blog));
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        NotFoundException result = assertThrows(NotFoundException.class, () -> blogService.isLiked(blog.getBlogId(), username));

        assertNotNull(result);
        assertEquals("User with username: " +  username + "not found", result.getMessage());

    }

    @Test
    void deleteById() {
        Blog blog = Blog.builder()
                .blogId(1L)
                .category(CategoryEnum.OTHER)
                .title("Blog1")
                .pictures(new ArrayList<>())
                .build();

        when(blogRepository.findById(blog.getBlogId())).thenReturn(Optional.of(blog));
        blogService.deleteById(blog.getBlogId());
        verify(blogRepository, times(1)).save(blog);
        verify(blogRepository, times(1)).deleteById(blog.getBlogId());

    }

}
