package com.example.proiect.controller;

import com.example.proiect.model.User;
import com.example.proiect.service.RoleService;
import com.example.proiect.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@AllArgsConstructor
@Slf4j
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/user/list")
    public ModelAndView usersList(@RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(2);
        ModelAndView modelAndView = new ModelAndView("users");
        Page<User> userPage = userService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        modelAndView.addObject("userPage", userPage);
        return modelAndView;
    }

    @RequestMapping("/user/delete/{userId}")
    public String deleteById(@PathVariable("userId") Long userId,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size){
        userService.deleteById(userId);
        log.info("Successfully deleted user with id {}", userId);
        if (size.isPresent() && page.isPresent()) {
            return "redirect:/user/list?size=" + size.get() + "&page=" + page.get();
        }

        return "redirect:/user/list";

    }

    @GetMapping("user/editRoles/{username}")
    public String editUserRoles(@PathVariable("username") String username, Model model) {
        User user = userService.findByUsername(username);

        model.addAttribute("user", user);
        model.addAttribute("listRoles", roleService.findAllRoles());

        return "update-roles";
    }

    @PostMapping("user/save")
    public String saveUser(User user) {

        User currentUser = userService.findById(user.getUserId());
        currentUser.setRoles(user.getRoles());
        userService.saveWithoutHash(currentUser);
        log.info("Roles have been updated for user {}", currentUser.getUsername());
        return "redirect:/user/list";
    }
}
