package com.example.proiect.service;

import com.example.proiect.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<User> findAll();
    void save(User user);
    void saveWithoutHash(User user);
    User findByUsername(String username);
    User getAuthenticatedUser();
    User findById(Long userId);

    Page<User> findPaginated(Pageable pageable);

    void deleteById(Long userId);
}
