package io.mountblue.reddit_project.controller;

import io.mountblue.reddit_project.model.Post;
import io.mountblue.reddit_project.model.SubReddit;
import io.mountblue.reddit_project.service.PostService;
import io.mountblue.reddit_project.service.SubRedditService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ViewController {
    private final SubRedditService subRedditService;
    private final PostService postService;

    public ViewController(SubRedditService subRedditService, PostService postService){
        this.subRedditService = subRedditService;
        this.postService = postService;
    }

    @GetMapping("/about-reddit")
    public String aboutReddit(){
        return "about-reddit";
    }
    @GetMapping("/content-policy")
    public String contentPolicy(){
        return "content-policy";
    }
    @GetMapping("/privacy-policy")
    public String privacyPolicy(){
        return "privacy-policy";
    }
    @GetMapping("/user-agreement")
    public String userAgreement(){
        return "user-agreement";
    }
    @GetMapping("/")
    public String subRedditPageView(Model model){
        model.addAttribute("subReddit",new SubReddit());
        List<Post> posts= postService.getAllPosts();
        List<Post> postsWithRelativeTime = posts.stream()
                .map(post -> {
                    post.setRelativeTime(subRedditService.calculateRelativeTime(post.getCreatedAt()));
                    return post;
                })
                .collect(Collectors.toList());
        model.addAttribute("posts",posts);
        model.addAttribute("subReddits",subRedditService.getAllSubReddits());
        model.addAttribute("subRedditNamesList",subRedditService.getAllSubRedditsByName());
        return "homepage";
    }
    @GetMapping("/showLoginPage")
    public String viewLoginPage(){
        return "login";
    }
}
