package io.mountblue.reddit_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final SubRedditService subRedditService;

    @Autowired
    public PostController(PostService postService, SubRedditService subRedditService) {
        this.postService = postService;
        this.subRedditService = subRedditService;
    }

    @GetMapping("/new")
    public String createNewPost(Model model) {
        List<String> subRedditList = subRedditService.getAllSubRedditsByName();
        model.addAttribute("subRedditList", subRedditList);
        return "create-post";
    }
}