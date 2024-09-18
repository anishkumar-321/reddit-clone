package io.mountblue.reddit_project.controller;

import io.mountblue.reddit_project.model.Post;
import io.mountblue.reddit_project.model.SubReddit;
import io.mountblue.reddit_project.service.PostService;
import io.mountblue.reddit_project.service.SubRedditService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class SubRedditController {
    private final SubRedditService subRedditService;
    private final PostService postService;

    public SubRedditController(SubRedditService subRedditService, PostService postService){
        this.subRedditService = subRedditService;
        this.postService = postService;
    }

    @GetMapping("/sub")
    public String subRedditPageView(Model model){
        model.addAttribute("subReddit",new SubReddit());
        List<Post> posts= postService.getAllPosts();
        model.addAttribute("posts",posts);
        model.addAttribute("subReddits",subRedditService.getAllSubReddits());
        model.addAttribute("subRedditNamesList",subRedditService.getAllSubRedditsByName());
        return "view-sub";
    }

    @PostMapping("/sub/new")
    public String subRedditCreate(@ModelAttribute SubReddit subReddit, Model model){
        subReddit.setCreatedAt(LocalDateTime.now());
        subReddit.setName("r/" + subReddit.getName());
        int randomAvatarIndex = (int) (Math.random() * 15) + 1;

        // Create the avatar path dynamically
        String avatarPath = "/images/avatar/" + randomAvatarIndex + ".svg";
        subReddit.setAvatar(avatarPath);

        subRedditService.createSubReddit(subReddit);
        return "redirect:/sub";
    }
}