package io.mountblue.reddit_project.controller;

import io.mountblue.reddit_project.model.Post;
import io.mountblue.reddit_project.model.SubReddit;
import io.mountblue.reddit_project.model.User;
import io.mountblue.reddit_project.service.PostService;
import io.mountblue.reddit_project.service.SubRedditService;
import io.mountblue.reddit_project.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import io.mountblue.reddit_project.model.Vote;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ViewController {
    private final SubRedditService subRedditService;
    private final PostService postService;
    private final VoteService voteService;


    @Autowired
    public ViewController(SubRedditService subRedditService, PostService postService, VoteService voteService){
        this.subRedditService = subRedditService;
        this.postService = postService;
        this.voteService = voteService;
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
  //  @GetMapping("/")


//    public String subRedditPageView(Model model){
//        model.addAttribute("subReddit",new SubReddit());
//        List<Post> posts= postService.getAllPosts();
//        List<Post> postsWithRelativeTime = posts.stream()
//                .map(post -> {
//                    post.setRelativeTime(subRedditService.calculateRelativeTime(post.getCreatedAt()));
//                    return post;
//                })
//                .collect(Collectors.toList());
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
//            System.out.println("authenticated");
//            User user = (User) authentication.getPrincipal();
//            model.addAttribute("user", user);
//        }
//
//        model.addAttribute("posts",posts);
//        model.addAttribute("subReddits",subRedditService.getAllSubReddits());
//        model.addAttribute("subRedditNamesList",subRedditService.getAllSubRedditsByName());
//        return "homepage";
//    }


    @GetMapping("/")
    public String subRedditPageView(Model model, @RequestParam (name = "sort", required = false,defaultValue = "new") String sort) {
        model.addAttribute("subReddit", new SubReddit());

        List<Post> posts = postService.getSortedPosts(sort);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("user", user);
        }
        List<Post> postsWithDetails = posts.stream().map(post -> {
            post.setRelativeTime(subRedditService.calculateRelativeTime(post.getCreatedAt()));
            post.setTotalVotes(post.getTotalVotes());

            if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
                User user = (User) authentication.getPrincipal();
                Vote vote = voteService.getVoteByPostAndUserId(post.getPostId(), user.getId());

                if (vote != null) {
                    if (vote.getVoteType() == 1) {
                        post.setUserUpvoted(true);
                    } else if (vote.getVoteType() == 0) {
                        post.setUserDownvoted(true);
                    }
                }
            }
            return post;
        }).collect(Collectors.toList());

        model.addAttribute("posts", postsWithDetails);
        model.addAttribute("subReddits", subRedditService.getAllSubReddits());
        model.addAttribute("subRedditNamesList", subRedditService.getAllSubRedditsByName());
        return "homepage";
    }

    @GetMapping("/showLoginPage")
    public String viewLoginPage(){
        return "login";
    }
}
