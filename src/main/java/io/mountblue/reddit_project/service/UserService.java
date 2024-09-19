package io.mountblue.reddit_project.service;

import io.mountblue.reddit_project.model.User;
import io.mountblue.reddit_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class UserService {
    private  final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;

    @Autowired
    public  UserService(UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Set<String> getEmailSet(){
        return userRepository.emailSet();
    }

    public Set<String> getUserNameSet(){
        return userRepository.userNameSet();
    }

    public void saveNewUser(User user){
        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        int randomAvatarIndex = (int) (Math.random() * 15) + 1;
        String avatarPath = "/images/avatar/" + randomAvatarIndex + ".svg";
        user.setAvatar(avatarPath);

        userRepository.save(user);
    }
}
