package io.mountblue.reddit_project.repository;

import io.mountblue.reddit_project.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
