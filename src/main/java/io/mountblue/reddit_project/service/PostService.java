package io.mountblue.reddit_project.service;

import io.mountblue.reddit_project.model.Post;
import io.mountblue.reddit_project.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public void saveCreatePost(Post post) {
        postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }


    public void saveUpdatedPost(Post post, String title, String body, MultipartFile imageFile) throws IOException {
        post.setBody(body);
        post.setTitle(title);
        if (imageFile != null && !imageFile.isEmpty()) {
            post.setImage(imageFile.getBytes());
        }
        postRepository.save(post);
    }

    public void deletePostById(Long id) {
        postRepository.deletePostById(id);
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
                break;
        }

        return posts;
    }

    public List<Post> fetchAllPostBySearch(String searchParam) {
        return postRepository.getAllPostsByRequirement(searchParam);
    }

    public List<Post> fetchAllPostBySubReddit(String subRedditName) {
        return postRepository.getAllPostBySubReddit(subRedditName);
    }
}
