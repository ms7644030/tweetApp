package com.tweetApp.tweetApp.model;

import javax.persistence.Embedded;

import com.tweetApp.tweetApp.entities.ProfileImage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private String username;
    private String timestamp;
    private String comment;
//    @Embedded
//    private ProfileImage profileImage;
   
}

