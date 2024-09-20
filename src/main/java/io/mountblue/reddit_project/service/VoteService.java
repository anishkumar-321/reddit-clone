package io.mountblue.reddit_project.service;

import io.mountblue.reddit_project.model.Post;
import io.mountblue.reddit_project.model.User;
import io.mountblue.reddit_project.model.Vote;
import io.mountblue.reddit_project.repository.PostRepository;
import io.mountblue.reddit_project.repository.VoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class VoteService {

   private final VoteRepository voteRepository;
   private final PostService postService;
    private final UserService userService;
    private final PostRepository postRepository;

   @Autowired
   public VoteService(VoteRepository voteRepository, PostService postService, UserService userService, PostRepository postRepository) {
        this.voteRepository = voteRepository;
        this.postService = postService;
        this.userService = userService;
        this.postRepository = postRepository;
   }

    public void voteDecider(Long postId,Long userId,String voteType){

        Vote existingVote = voteRepository.getVoteByPostAndUserId(postId, userId);
        Post post = postService.getPostById(postId);
        User user = userService.getUserById(userId);

        if (existingVote == null) {
            // First-time voting (no previous vote)
            Vote newVote = new Vote();
            newVote.setPost(post);
            newVote.setUser(user);

            if (voteType.equals("up")) {
                newVote.setVoteType(1); // Upvote
                post.setTotalVotes(post.getTotalVotes() + 1); // Increase totalVotes
            } else if (voteType.equals("down")) {
                newVote.setVoteType(0); // Downvote
                post.setTotalVotes(post.getTotalVotes() - 1); // Decrease totalVotes
            }

            voteRepository.save(newVote);
        }
        else {
            // The user has already voted; update or delete the vote
            if (existingVote.getVoteType() == 1 && voteType.equals("down")) {
                // Change from upvote to downvote
                existingVote.setVoteType(0);
                post.setTotalVotes(post.getTotalVotes() - 2);
                voteRepository.save(existingVote);// Decrease total by 2
            } else if (existingVote.getVoteType() == 0 && voteType.equals("up")) {
                // Change from downvote to upvote
                existingVote.setVoteType(1);
                post.setTotalVotes(post.getTotalVotes() + 2);
                voteRepository.save(existingVote);// Increase total by 2
            } else {
                // User clicked the same vote again (remove the vote)
                voteRepository.delete(existingVote);

                // Adjust totalVotes based on what the previous vote was
                if (existingVote.getVoteType() == 1) {
                    post.setTotalVotes(post.getTotalVotes() - 1); // Remove upvote
                } else {
                    post.setTotalVotes(post.getTotalVotes() + 1); // Remove downvote
                }
            }

            postRepository.save(post);
        }
   }


    public Vote getVoteByPostAndUserId(Long postId, Long userId){
        Vote VoteData=  voteRepository.getVoteByPostAndUserId(postId,userId);
        return VoteData;
    }



}
