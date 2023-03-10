package com.tweetApp.tweetApp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweetApp.tweetApp.entities.UserModel;
import com.tweetApp.tweetApp.entities.Users;
import com.tweetApp.tweetApp.repository.UsersRepository;

@Service
public class UsersService {

	@Autowired
	private UsersRepository usersRepository;

	public Users storeUserDetails(Users user) {
		usersRepository.save(user);
		return user;
	}

	public boolean checkEmailAndLoginId(Users user) {
		return user.getEmailId().equals(user.getLoginId());
	}

	public boolean checkExistOrNot(Users user) {
		return usersRepository.existsByLoginId(user.getLoginId());
	}

	public boolean checkUser(String userName, String password) {
		Users tempUser = usersRepository.findByLoginId(userName);
		return (tempUser != null && tempUser.getLoginId().equals(userName) && tempUser.getPassword().equals(password));
	}

	public UserModel getUser(String userName, String password) {
		Users u = usersRepository.findUserByUsernameAndPassword(userName, password);
		UserModel us = new UserModel();
		us.setId(u.getId());
		us.setContactNo(u.getContactNo());
		us.setEmailId(u.getEmailId());
		us.setFirstName(u.getFirstName());
		us.setLastName(u.getLastName());
		us.setImageurl(u.getImageurl());
		us.setLoginId(u.getLoginId());
		return us;
	}

	public boolean forgotPassword(String userName, String newPassword) {
		Users user = usersRepository.findByLoginId(userName);
		if (user != null) {
			user.setPassword(newPassword);
			usersRepository.save(user);
			return true;
		}
		return false;
	}

	public List<String> getAllUsers() {
		List<Users> users = usersRepository.findAll();
		List<String> result = new ArrayList<>();
		for (Users u : users) {
			result.add(u.getLoginId());
		}
		return result;
	}

	public Users getByUserName(String userName) {
		return usersRepository.findByLoginId(userName);

	}

	public UserModel getDetailsOfUser(String userName) {
		Users u = usersRepository.findByLoginId(userName);
		UserModel us = new UserModel();
		us.setId(u.getId());
		us.setContactNo(u.getContactNo());
		us.setEmailId(u.getEmailId());
		us.setFirstName(u.getFirstName());
		us.setLastName(u.getLastName());
		us.setImageurl(u.getImageurl());
		us.setLoginId(u.getLoginId());
		return us;
	}

	public static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}

}
