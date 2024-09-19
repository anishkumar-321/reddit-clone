package io.mountblue.reddit_project.controller;

import io.mountblue.reddit_project.model.User;
import io.mountblue.reddit_project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/showLoginPage")
    public String viewLoginPage(){
        return "login";
    }

    @GetMapping("/user/new")
    public String userPage(Model model){
        model.addAttribute("userRegistrationInfo",new User() );
        return "register";
    }

    @PostMapping("/user/submit")
    public String newUser(User user, Model model){
        Set<String> emailSet = userService.getEmailSet();
        Set<String> userNameSet = userService.getUserNameSet();

        if(emailSet.contains(user.getEmail()) || userNameSet.contains(user.getUsername())){
            model.addAttribute("error","email/username is already present");
            return "register";
        }

        userService.saveNewUser(user);
        return "login";
    }

}
