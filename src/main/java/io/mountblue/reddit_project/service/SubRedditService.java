package io.mountblue.reddit_project.service;


import io.mountblue.reddit_project.model.SubReddit;
import io.mountblue.reddit_project.repository.SubRedditRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubRedditService {
    private final SubRedditRepository subRedditRepository;

    public SubRedditService(SubRedditRepository subRedditRepository){
        this.subRedditRepository = subRedditRepository;
    }

    public void createSubReddit(SubReddit sudReddit){
        subRedditRepository.save(sudReddit);
    }

    public List<SubReddit> getAllSubReddits() {
        return subRedditRepository.findAll();
    }
    public SubReddit getSubReddit(String subredditName){
        return subRedditRepository.findByName(subredditName);
    }

    public List<String> getAllSubRedditsByName() {
        return subRedditRepository.findAllByName();
    }
}
