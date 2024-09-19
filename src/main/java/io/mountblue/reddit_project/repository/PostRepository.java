package io.mountblue.reddit_project.repository;

import io.mountblue.reddit_project.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
