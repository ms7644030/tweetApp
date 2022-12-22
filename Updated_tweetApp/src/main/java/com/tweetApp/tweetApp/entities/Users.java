package com.tweetApp.tweetApp.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Component
@Entity
@Table(name = "USERS_DB")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Users {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String emailId;
    @Column
    private String loginId;
    private String firstName;
    private String lastName;
    private String password;
    private String contactNo;
    @Nullable
//    @Embedded
//    private ProfileImage profileImage;
    private String imageurl;

}
