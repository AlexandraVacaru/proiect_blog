package com.example.proiect;

import com.example.proiect.model.Blog;
import com.example.proiect.model.CategoryEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("mysql")
@Rollback(false)
public class EntityManagerTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    public void findBlog() {
        Blog blogFound = entityManager.find(Blog.class, 2L);
        assertEquals(blogFound.getTitle(), "blog1");
    }

    @Test
    public void updateBlog() {
        Blog blogFound = entityManager.find(Blog.class, 2L);
        blogFound.setCategory(CategoryEnum.OTHER);
        entityManager.persist(blogFound);
        entityManager.flush();
        assertEquals(blogFound.getCategory(), CategoryEnum.OTHER);
    }

}
