package com.tweetApp.tweetApp.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.tweetApp.tweetApp.model.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Entity
@Table(name = "TWEETS_DB")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Tweets {
    @Id
    private String tweetId;
    @Column(name = "username",insertable = false ,updatable= false)
    private String username;
    private String tweetText;
    private String firstName;
    private String lastName;
    private String tweetDate;
//    @Embedded
//    private ProfileImage profileImage;
    private String imageurl;
    @ElementCollection(targetClass=String.class)
    private List<String> likes = new ArrayList<>();
   @Embedded
   @ElementCollection(targetClass=Comment.class)
   private List<Comment> comments = new ArrayList<>();

}
