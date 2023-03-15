package com.example.proiect.repository;

import com.example.proiect.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "schema.sql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    public void addUser() {
        User user = User.builder()
                .userId(1L)
                .username("username1")
                .firstName("firstName1")
                .lastName("lastName1")
                .email("email1@yahoo.com")
                .birthDate(LocalDate.parse("1999-09-09"))
                .build();
        User result = userRepository.save(user);
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
    }

    @Test
    @Order(2)
    public void findByUsername() {
        Optional<User> user = userRepository.findByUsername("userTest1");
        assertNotNull(user.get());
        assertEquals(1000L, user.get().getUserId());
    }
}
