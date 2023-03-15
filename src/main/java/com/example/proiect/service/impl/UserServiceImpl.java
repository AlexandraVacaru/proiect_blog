package com.example.proiect.service.impl;

import com.example.proiect.exception.NotFoundException;
import com.example.proiect.model.Blog;
import com.example.proiect.model.Role;
import com.example.proiect.model.RoleEnum;
import com.example.proiect.model.User;
import com.example.proiect.repository.RoleRepository;
import com.example.proiect.repository.UserRepository;
import com.example.proiect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<User> findAll() {
        List<User> users = new LinkedList<>();
        userRepository.findAll().iterator().forEachRemaining(users::add);
        return users;
    }

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set<Role> userRoles = roleRepository.findAll()
                .stream()
                .filter(x -> x.getName().equals(RoleEnum.ROLE_USER))
                .collect(Collectors.toSet());
        user.setRoles(userRoles);
        userRepository.save(user);
    }

    @Override
    public void saveWithoutHash(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()) {
            throw new NotFoundException("User with username: " +  username + "not found");
        }
        return userOptional.get();
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = findByUsername(authentication.getName());
        return authenticatedUser;
    }

    @Override
    public User findById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(!userOptional.isPresent()) {
            throw new NotFoundException("User with userId: " +  userId + "not found");
        }
        return userOptional.get();
    }

    @Override
    public Page<User> findPaginated(Pageable pageable) {
        List<User> users = findAll();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<User> list;
        if (users.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, users.size());
            list = users.subList(startItem, toIndex);
        }
        Page<User> userPage = new PageImpl<>(list, PageRequest.of(currentPage,
                pageSize), users.size());
        return userPage;
    }

    @Override
    @Transactional
    public void deleteById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new NotFoundException("User with userId: " +  userId + "not found");
        }
        User user = userOptional.get();
        Set<Role> roles = new LinkedHashSet<>();
        Set<Blog> likedBlogs = new LinkedHashSet<>();
        user.getRoles().iterator().forEachRemaining(roles::add);
        user.getLikedBlogs().iterator().forEachRemaining(likedBlogs::add);

        for(Role role : roles) {
            user.removeRole(role);
        }

        for(Blog blog: likedBlogs) {
            user.removeLikedBlog(blog);
        }

        userRepository.save(user);
        userRepository.deleteById(userId);
    }

}
