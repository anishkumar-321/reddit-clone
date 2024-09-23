package io.mountblue.reddit_project.controller;

import io.mountblue.reddit_project.model.Post;
import io.mountblue.reddit_project.model.SubReddit;
import io.mountblue.reddit_project.model.User;
import io.mountblue.reddit_project.service.PostService;
import io.mountblue.reddit_project.service.SubRedditService;
import io.mountblue.reddit_project.service.UserService;
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


import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ViewController {
    private final SubRedditService subRedditService;
    private final PostService postService;
    private final VoteService voteService;
    private final UserService userService;


    @Autowired
    public ViewController(SubRedditService subRedditService, PostService postService, VoteService voteService, UserService userService){
        this.subRedditService = subRedditService;
        this.postService = postService;
        this.voteService = voteService;
        this.userService = userService;
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
    public String subRedditPageView(Model model, @RequestParam (name = "sort", required = false,defaultValue = "new") String sort,
                                    @RequestParam(name="search", required = false,defaultValue = "") String search,
                                    @RequestParam(name="subRedditName",required = false,defaultValue = "") String subRedditName,
                                    @RequestParam(name="userPost",required = false) boolean isUserPost ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("subReddit", new SubReddit());
        List<Post> posts;
        if(isUserPost)
        {
            String userName=SecurityContextHolder.getContext().getAuthentication().getName();
            User user=userService.getUserByUsername(userName);
            posts=user.getPosts();
        }
       else if (!subRedditName.isEmpty()) {
            String subRedditParam = URLDecoder.decode(subRedditName, StandardCharsets.UTF_8);
            posts = postService.fetchAllPostBySubReddit(subRedditParam);
        } else if (!search.isEmpty()) {
            String searchParam = URLDecoder.decode(search, StandardCharsets.UTF_8);
            posts = postService.fetchAllPostBySearch(searchParam);
        } else {
            posts = postService.getSortedPosts(sort);
        }

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
