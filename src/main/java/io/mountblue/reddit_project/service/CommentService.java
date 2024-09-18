package io.mountblue.reddit_project.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import io.mountblue.reddit_project.model.Comment;
import io.mountblue.reddit_project.model.Post;
import io.mountblue.reddit_project.repository.CommentRepository;
import io.mountblue.reddit_project.repository.PostRepository;


@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public Comment getCommentById(Long commentId){
        return commentRepository.getReferenceById(commentId);
    }

    public void saveComment(Long postId,String name, String email, String commentContent){
        Post post = postRepository.getReferenceById(postId);
        Comment comment = new Comment();
        comment.setComment(commentContent);
        comment.setEmail(email);
        comment.setName(name);
        comment.setPost(post);
        commentRepository.save(comment);
    }

    public void updateComment(Long commentId,String name , String email, String commentContent){
        Comment comment = commentRepository.getReferenceById(commentId);
        comment.setName(name);
        comment.setEmail(email);
        comment.setComment(commentContent);
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }

    public Long getPostIdByCommentId(Long commentId){
        Comment comment = commentRepository.getReferenceById(commentId);
        Post post  = comment.getPost();
        return post.getPostId();
    }
}
