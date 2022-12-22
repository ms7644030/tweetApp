package com.tweetApp.tweetApp.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetApp.tweetApp.entities.Tweets;
import com.tweetApp.tweetApp.entities.Users;
import com.tweetApp.tweetApp.exceptions.InvalidUsernameException;
import com.tweetApp.tweetApp.exceptions.TweetDoesNotExistException;
import com.tweetApp.tweetApp.model.Comment;
import com.tweetApp.tweetApp.model.TweetResponse;
import com.tweetApp.tweetApp.repository.TweetRepository;
import com.tweetApp.tweetApp.repository.UsersRepository;

@Service
public class TweetsService {

	public static final String THIS_TWEET_DOES_NOT_EXIST_ANYMORE = "This tweet does not exist anymore.";
	@Autowired
	private TweetRepository tweetRepository;

	@Autowired
	private UsersRepository usersRepository;

	public List<Tweets> getAllTweets() {
		return tweetRepository.findAll();
	}

	public List<TweetResponse> getUserTweets(String username) throws InvalidUsernameException {
		if (!StringUtils.isBlank(username)) {
			List<Tweets> tweets = tweetRepository.findByUsername(username);
			return tweets.stream().map(tweet -> {
				Integer likesCount = tweet.getLikes().size();
				Integer commentsCount = tweet.getReplies().size();
				return new TweetResponse(tweet.getTweetId(), username, tweet.getTweetText(), tweet.getFirstName(),
						tweet.getLastName(), tweet.getTweetDate(), likesCount, commentsCount, tweet.getImageurl(),
						tweet.getReplies());
			}).collect(Collectors.toList());
		} else {
			throw new InvalidUsernameException("Username/loginId provided is invalid");
		}

	}

	public List<TweetResponse> getUserTweetsByTweetId(String tweetId) throws InvalidUsernameException {
		if (!StringUtils.isBlank(tweetId)) {
			List<Tweets> tweets = tweetRepository.findByTweetId(tweetId);
			return tweets.stream().map(tweet -> {
				Integer likesCount = tweet.getLikes().size();
				Integer commentsCount = tweet.getReplies().size();
				return new TweetResponse(tweet.getTweetId(), tweet.getUsername(), tweet.getTweetText(),
						tweet.getFirstName(), tweet.getLastName(), tweet.getTweetDate(), likesCount, commentsCount,
						tweet.getImageurl(), tweet.getReplies());// , tweet.getComments());
			}).collect(Collectors.toList());
		} else {
			throw new InvalidUsernameException("Username/loginId provided is invalid");
		}

	}

	// Method to post a new tweet
	public Tweets postNewTweet(String username, String tweetText) {
		Tweets newTweet = new Tweets();
		newTweet.setTweetId(UUID.randomUUID().toString());
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
		String formattedDate = myDateObj.format(myFormatObj);
		newTweet.setTweetDate(formattedDate);
		Users user = usersRepository.findByLoginId(username);
		newTweet.setFirstName(user.getFirstName());
		newTweet.setLastName(user.getLastName());
		newTweet.setUsername(user.getLoginId());
		newTweet.setImageurl(user.getImageurl());
		newTweet.setTweetText(tweetText);
		tweetRepository.save(newTweet);
		return newTweet;
	}

	public Tweets updateTweet(String userId, String tweetId, String updatedTweetText)
			throws TweetDoesNotExistException {

		Optional<Tweets> originalTweetOptional = Optional
				.ofNullable(tweetRepository.findUserByUsernameAndTweetId(userId, tweetId));
		if (originalTweetOptional.isPresent()) {
			Tweets tweet = originalTweetOptional.get();
			tweet.setTweetText(updatedTweetText);
			return tweetRepository.save(tweet);
		} else {
			throw new TweetDoesNotExistException(THIS_TWEET_DOES_NOT_EXIST_ANYMORE);
		}

	}

	public void deleteTweet(String userName, String tweetId) throws TweetDoesNotExistException {
		if (tweetRepository.findByUsername(userName) != null && tweetRepository.existsById(tweetId)
				&& !StringUtils.isBlank(tweetId)) {
			tweetRepository.deleteById(tweetId);
		} else {

			throw new TweetDoesNotExistException(THIS_TWEET_DOES_NOT_EXIST_ANYMORE);
		}
	}

	public void likeTweet(String username, String tweetId) throws TweetDoesNotExistException {
		Optional<Tweets> tweetOptional = tweetRepository.findById(tweetId);
		if (tweetOptional.isPresent()) {
			Tweets tweet = tweetOptional.get();
			tweet.getLikes().add(username);
			tweetRepository.save(tweet);
		} else {
			throw new TweetDoesNotExistException(THIS_TWEET_DOES_NOT_EXIST_ANYMORE);
		}
	}

	public void disLikeTweet(String username, String tweetId) throws TweetDoesNotExistException {
		Optional<Tweets> tweetOptional = tweetRepository.findById(tweetId);
		if (tweetOptional.isPresent()) {
			Tweets tweet = tweetOptional.get();
			tweet.getLikes().remove(username);
			tweetRepository.save(tweet);
		} else {
			throw new TweetDoesNotExistException(THIS_TWEET_DOES_NOT_EXIST_ANYMORE);
		}
	}

	public boolean checkLikedOrNot(String username, String tweetId) throws TweetDoesNotExistException {
		Optional<Tweets> tweetOptional = tweetRepository.findById(tweetId);

		if (!tweetOptional.isPresent()) {
			throw new TweetDoesNotExistException(THIS_TWEET_DOES_NOT_EXIST_ANYMORE);
		}
		Tweets tweet = tweetOptional.get();
		return tweet.getLikes().contains(username);
	}

	public void replyTweet(String username, String tweetId, String tweetReply) throws TweetDoesNotExistException {
		Optional<Tweets> tweetOptional = tweetRepository.findById(tweetId);
		Users user = usersRepository.findByLoginId(username);
		if (tweetOptional.isPresent()) {
			Tweets tweet = tweetOptional.get();

			List<Comment> c = tweet.getReplies();
			LocalDateTime myDateObj = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
			String formattedDate = myDateObj.format(myFormatObj);
			Comment s = new Comment(username, formattedDate, tweetReply);
			if (c != null) {

				c.add(s);
				tweet.setReplies(c);
			} else {
				List<Comment> cs = new ArrayList<>();
				cs.add(s);
				tweet.setReplies(cs);
			}

			tweetRepository.save(tweet);
		} else {
			throw new TweetDoesNotExistException(THIS_TWEET_DOES_NOT_EXIST_ANYMORE);
		}
	}

	public List<Tweets> findByTweetId(String tweetId) {
		return tweetRepository.findByTweetId(tweetId);
	}

}
