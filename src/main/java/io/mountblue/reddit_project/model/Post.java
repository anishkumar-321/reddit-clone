package io.mountblue.reddit_project.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String body;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name="up_vote")
    private int upVote;

    @Column(name="down_vote")
    private int downVote;

    public boolean isUserUpvoted() {
        return userUpvoted;
    }

    public void setUserUpvoted(boolean userUpvoted) {
        this.userUpvoted = userUpvoted;
    }

    @Transient
    private boolean userUpvoted;

    public boolean isUserDownvoted() {
        return userDownvoted;
    }

    public void setUserDownvoted(boolean userDownvoted) {
        this.userDownvoted = userDownvoted;
    }

    @Transient
    private boolean userDownvoted;

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    @Column(name="total_votes",nullable = false)
    private int totalVotes=0;

    @ManyToOne
    @JoinColumn(name = "subreddit_id")
    private SubReddit subReddit;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Transient
    private String relativeTime;

    @OneToMany(mappedBy="post", cascade=CascadeType.ALL)
    private List<Comment> comments= new ArrayList<>();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name="author")
    private User user;

    @OneToMany(mappedBy="post",cascade=CascadeType.ALL)
     private List<Vote>votes;

    public List<Vote> getVotes(){
        return votes;
    }

    public void setVotes(List<Vote>votes){
        this.votes=votes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public SubReddit getSubReddit() {
        return subReddit;
    }

    public void setSubReddit(SubReddit subReddit) {
        this.subReddit = subReddit;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(String relativeTime) {
        this.relativeTime = relativeTime;
    }
}