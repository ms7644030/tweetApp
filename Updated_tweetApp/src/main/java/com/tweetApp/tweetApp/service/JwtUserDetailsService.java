package com.tweetApp.tweetApp.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tweetApp.tweetApp.entities.Users;
import com.tweetApp.tweetApp.repository.UsersRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	@Autowired
	private UsersRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Users foundedUser = userRepository.findByLoginId(username);
		if (foundedUser == null)
			return null;
		String name = foundedUser.getLoginId();
		String pwd = foundedUser.getPassword();
		return new User(name, pwd, new ArrayList<>());
	}
}
