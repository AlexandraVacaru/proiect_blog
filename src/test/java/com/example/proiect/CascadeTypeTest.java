package com.example.proiect;

import com.example.proiect.model.Blog;
import com.example.proiect.model.CategoryEnum;
import com.example.proiect.model.User;
import org.junit.Ignore;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("h2")
@Sql(scripts = "schema.sql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CascadeTypeTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    @Order(1)
    public void saveBlog() {
        User user = entityManager.find(User.class, 1000L);
        Blog blog = Blog.builder()
                .title("testBlog")
                .content("BlogContent")
                .category(CategoryEnum.OTHER)
                .date(LocalDateTime.now())
                .user(user)
                .build();

        entityManager.persist(blog);
        entityManager.flush();
        entityManager.clear();

    }

    @Test
    @Order(2)
    @Ignore
    public void updateBlog() {
        Blog blog = entityManager.find(Blog.class, 1000L);
        blog.setTitle("UpdatedBlog");
        entityManager.merge(blog);
        entityManager.flush();
    }

    @ParameterizedTest
    @Order(3)
    @ValueSource(longs = {1000, 2000})
    public void orphanRemoval(long id) {
        Blog blog = entityManager.find(Blog.class, id);
        blog.setComments(null);
        entityManager.persist(blog);
        entityManager.flush();
    }

}
