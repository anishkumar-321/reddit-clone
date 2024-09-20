package io.mountblue.reddit_project.repository;

import io.mountblue.reddit_project.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    @Query("SELECT DISTINCT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :searchParam, '%')) OR LOWER(p.subReddit.name) LIKE LOWER(CONCAT('%', :searchParam, '%')) OR LOWER(p.user.username) LIKE LOWER(CONCAT('%', :searchParam, '%'))")
    List<Post> getAllPostsByRequirement(@Param("searchParam") String searchParam);

    @Query("select DISTINCT p FROM Post p WHERE LOWER(p.subReddit.name) LIKE LOWER(:subRedditName)")
    List<Post> getAllPostBySubReddit(@Param("subRedditName") String subRedditName);
}
