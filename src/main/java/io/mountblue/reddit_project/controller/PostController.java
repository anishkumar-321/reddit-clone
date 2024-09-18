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
    @PostMapping("/createFirstPart")
    public String savePost(@RequestParam("title") String title,
                           @RequestParam("body") String body,
                           @RequestParam("subRedditName") String subRedditName,
                           @RequestParam("image") MultipartFile imageFile,
                           Model model) throws IOException {
        System.out.println(subRedditName);
        Post post = new Post();
        post.setTitle(title);
        SubReddit subReddit = subRedditService.getSubReddit(subRedditName);
        System.out.println(subReddit);
        post.setSubReddit(subReddit);
        System.out.println(post.getSubReddit());
        post.setBody(body);

        if (!imageFile.isEmpty()) {
            post.setImage(imageFile.getBytes());
        }

        post.setCreatedAt(LocalDateTime.now());
        postService.saveCreatePost(post);
        return "redirect:/sub";
    }
}