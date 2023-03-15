package com.example.proiect.controller;

import com.example.proiect.model.User;
import com.example.proiect.repository.RoleRepository;
import com.example.proiect.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    RoleRepository roleRepository;


    @Test
    @WithMockUser(username = "user_1", password = "user", roles = "USER")
    void getUserByUsername() throws Exception {
        User user1 = User.builder()
                .username("user1")
                .email("user1@yahoo.com")
                .password("password")
                .birthDate(LocalDate.parse("1999-09-18"))
                .lastName("user")
                .firstName("user")
                .build();

        when(userService.findByUsername(user1.getUsername())).thenReturn(user1);

        mockMvc.perform(get("/user/{username}", "user1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-profile"))
                .andExpect(model().attribute("user", user1))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user_1", password = "user", roles = "USER")
    void editUserByUsername() throws Exception {
        User user1 = User.builder()
                .username("user_1")
                .email("user1@yahoo.com")
                .password("password")
                .birthDate(LocalDate.parse("1999-09-18"))
                .lastName("user")
                .firstName("user")
                .build();

        when(userService.findByUsername(user1.getUsername())).thenReturn(user1);

        mockMvc.perform(get("/user/edit/{username}", "user_1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-user-profile"))
                .andExpect(model().attribute("user", user1))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user_1", password = "user", roles = "USER")
    void addContactInfo() throws Exception {
        User user1 = User.builder()
                .username("user_1")
                .email("user1@yahoo.com")
                .password("password")
                .birthDate(LocalDate.parse("1999-09-18"))
                .lastName("user")
                .firstName("user")
                .build();

        when(userService.findByUsername(user1.getUsername())).thenReturn(user1);

        mockMvc.perform(get("/user/contactInfo/{username}", "user_1"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-contact-info"))
                .andExpect(model().hasNoErrors())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }
}
