package io.mountblue.reddit_project.service;

import io.mountblue.reddit_project.model.Post;
import io.mountblue.reddit_project.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }


    public void saveCreatePost(Post post){
        postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }


    public void saveUpdatedPost(Post post, String title, String body){
        post.setBody(body);
        post.setTitle(title);
        postRepository.save(post);
    }

    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }
    public List<Post> getSortedPosts(String sort) {
        List<Post> posts = getAllPosts();

        switch (sort) {
            case "new":
                posts.sort(Comparator.comparing(Post::getCreatedAt).reversed());
                break;
            case "old":
                posts.sort(Comparator.comparing(Post::getCreatedAt));
                break;
            case "top":
                posts.sort(Comparator.comparing(Post::getTotalVotes).reversed());
                break;
            default:
                // No sorting
                break;
        }

        return posts;
    }
    public List<Post> fetchAllPostBySearch(String searchParam) {
        return postRepository.getAllPostsByRequirement(searchParam);
    }

    public List<Post> fetchAllPostBySubReddit(String subRedditName){
        return postRepository.getAllPostBySubReddit(subRedditName);
    }
}
