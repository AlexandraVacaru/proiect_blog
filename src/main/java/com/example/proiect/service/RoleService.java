package com.example.proiect.service;

import com.example.proiect.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    List<Role> findAllRoles();
    Optional<Role> findById(Long id);
}
