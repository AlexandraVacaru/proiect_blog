package com.example.proiect.controller;

import com.example.proiect.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_ADMIN')")
    @RequestMapping("/comment/delete/{commentId}")
    public String deleteById(@PathVariable("commentId") Long commentId,
                             @RequestParam("blogId") Long blogId,
                             @RequestParam("username") String username){
        commentService.deleteById(commentId);
        log.info("User {} deleted comment with id {} from blog {}", username, commentId, blogId);
        return "redirect:/blog/" + blogId;
    }
}
