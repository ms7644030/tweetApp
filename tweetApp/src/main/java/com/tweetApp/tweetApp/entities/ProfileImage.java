package com.tweetApp.tweetApp.entities;

import javax.persistence.Lob;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Component
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class ProfileImage {
	
	
	private String user;
	private String type;
    //image bytes can have large lengths so we specify a value
    //which is more than the default length for picByte column
	@Lob
	private byte[] picByte;
	
	
	
}
