package com.tweetApp.tweetApp.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class UserModel {
	@Id
	private String id;
	private String emailId;
	private String loginId;
	private String firstName;
	private String lastName;
	private String contactNo;
	private String Imageurl;
}
