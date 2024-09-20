package io.mountblue.reddit_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import io.mountblue.reddit_project.model.Comment;
import io.mountblue.reddit_project.service.CommentService;

@RequestMapping("/comments")
@Controller
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add")
    public String addComment(@RequestParam("postId")Long postId, @RequestParam("name")String name, @RequestParam("email")String email, @RequestParam("comment")String comment){
        commentService.saveComment(postId,name,email,comment);
        return "redirect:/posts/"+postId;
    }

    @GetMapping("{commentId}/editComment")
    public String editComment(@PathVariable Long commentId, Model model){
        Comment comment = commentService.getCommentById(commentId);
        model.addAttribute("comment",comment);
        return "comment-updater";
    }

    @PostMapping("{commentId}/updateComment")
    public String updateComment(@PathVariable Long commentId, @RequestParam("name") String name , @RequestParam("email") String email, @RequestParam("comment")String comment){
        Long postId= commentService.getPostIdByCommentId(commentId);
        commentService.updateComment(commentId,name,email,comment);
        return "redirect:/posts/"+postId;
    }

    @GetMapping("{commentId}/deleteComment")
    public String deleteComment(@PathVariable Long commentId){
        Long postId= commentService.getPostIdByCommentId(commentId);
        commentService.deleteComment(commentId);
        return "redirect:/posts/"+postId;
    }
}
