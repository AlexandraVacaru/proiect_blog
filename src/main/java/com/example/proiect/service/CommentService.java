package com.example.proiect.service;

import com.example.proiect.model.Comment;

public interface CommentService {
    Comment save(Comment comment);

    Comment findById(Long commentId);

    void deleteById(Long commentId);
}
