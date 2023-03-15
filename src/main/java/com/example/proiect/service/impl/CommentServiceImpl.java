package com.example.proiect.service.impl;

import com.example.proiect.exception.NotFoundException;
import com.example.proiect.model.Comment;
import com.example.proiect.repository.CommentRepository;
import com.example.proiect.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment findById(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isEmpty()) {
            log.info("Comment with id {} was not found", commentId);
            throw new NotFoundException("Comment not found");
        }
        return commentOptional.get();
    }

    @Override
    public void deleteById(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isEmpty()) {
            log.info("Comment with id {} was not found", commentId);
            throw new NotFoundException("Comment not found");
        }
        commentRepository.delete(commentOptional.get());
    }
}
