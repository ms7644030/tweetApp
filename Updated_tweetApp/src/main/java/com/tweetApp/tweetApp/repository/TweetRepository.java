package com.tweetApp.tweetApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tweetApp.tweetApp.entities.Tweets;

@Repository
public interface TweetRepository extends JpaRepository<Tweets, String> {

	List<Tweets> findByUsername(String username);

	@Query(value = "SELECT t FROM Tweets t WHERE t.username=?1 ")
	Tweets findByTweetUsername(String username);

	@Query("SELECT t FROM Tweets t WHERE t.username =?1 AND t.tweetId = ?2 ")
	Tweets findUserByUsernameAndTweetId(String username, String tweetId);

	@Query(value = "SELECT t FROM Tweets t WHERE t.tweetId=?1 ")
	List<Tweets> findByTweetId(String tweetId);
}
