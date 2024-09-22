package io.mountblue.reddit_project.controller;

import io.mountblue.reddit_project.model.User;
import io.mountblue.reddit_project.service.UserService;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new")
    public String userPage(Model model) {
        model.addAttribute("userRegistrationInfo", new User());
        return "register";
    }

    @PostMapping("/submit")
    public String newUser(User user, Model model) {
        System.out.println("Entering");
        Set<String> emailSet = userService.getEmailSet();
        Set<String> userNameSet = userService.getUserNameSet();

        if (emailSet.contains(user.getEmail()) || userNameSet.contains(user.getUsername())) {
            model.addAttribute("error", "email/username is already present");
            return "register";
        }
        userService.saveNewUser(user);
        return "login";
    }

//    @GetMapping("/profile")
//    public String viewUserProfile(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();
//        model.addAttribute("user", user);
//        return "user-view";
//    }
@GetMapping("/profile")
public String viewUserProfile(Model model) {
    // Get the current authentication object
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Retrieve the username from the authentication object
    String username = ((User) authentication.getPrincipal()).getUsername();

    // Reload the user from the database to ensure the most up-to-date data is fetched
    User user = userService.getUserByUsername(username);

    // Add the updated user data to the model
    model.addAttribute("user", user);

    return "user-view";
}

    @GetMapping("/profile/edit")
    public String editProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/profile/update")
    public String editProfile(@ModelAttribute("user") User updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        user.setUserInfo(updatedUser.getUserInfo());
        user.setGender(updatedUser.getGender());
        user.setAge(updatedUser.getAge());
        user.setUsername(updatedUser.getUsername());
        userService.updateUser(user);
        return "redirect:/user/profile";
    }

    @GetMapping("/profile/delete")
    public String deleteProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        userService.deleteUser(user.getId());
        SecurityContextHolder.clearContext();
        return "redirect:/logout";
    }
}
