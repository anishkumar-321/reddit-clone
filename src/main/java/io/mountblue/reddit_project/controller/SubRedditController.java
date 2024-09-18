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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Post> postsWithRelativeTime = posts.stream()
                .map(post -> {
                    post.setRelativeTime(calculateRelativeTime(post.getCreatedAt()));
                    return post;
                })
                .collect(Collectors.toList());
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
    private String calculateRelativeTime(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        if (duration.toMinutes() < 1) {
            return "Just now";
        } else if (duration.toMinutes() < 60) {
            return duration.toMinutes() + " minutes ago";
        } else if (duration.toHours() < 24) {
            return duration.toHours() + " hours ago";
        } else if (duration.toDays() == 1) {
            return "Yesterday";
        } else if (duration.toDays() < 30) {
            return duration.toDays() + " days ago";
        } else if (duration.toDays() < 365) {
            return (duration.toDays() / 30) + " months ago";
        } else {
            return (duration.toDays() / 365) + " years ago";
        }
    }


}