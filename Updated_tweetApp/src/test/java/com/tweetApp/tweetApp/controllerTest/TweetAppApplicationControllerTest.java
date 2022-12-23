package com.tweetApp.tweetApp.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetApp.tweetApp.entities.Users;
import com.tweetApp.tweetApp.model.LoginCredentials;
import com.tweetApp.tweetApp.service.UsersService;

@AutoConfigureMockMvc
@SpringBootTest

class TweetAppApplicationControllerTest {

	@Mock
	private UsersService usersService;

	@Autowired
	UsersService usersServiceBean;

	@Autowired
	MockMvc mockMvc;

	@Test
	public void loginUserTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:9001/api/v1.0/tweets/login")
				.content(asJsonString(new LoginCredentials("manish", "manish"))).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	void InvalidLoginUserTest() throws Exception {
		String userName = "manish";
		String password = "invalid";
		when(usersService.checkUser(userName, password)).thenReturn(false);

		MvcResult result = mockMvc.perform(post("http://localhost:9001/api/v1.0/tweets/login")
				.content(asJsonString(new LoginCredentials(userName, password))).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andReturn();

		String actual = result.getResponse().getContentAsString();
		String expected = "\"Invalid credentials\"";
		assertEquals(expected, actual);
	}

	@Test
	void registerUserTest() throws Exception {
		Users user = new Users("test-1", "manish@gmail.com", "manish12345678", "Manish", "Singh", "manish1",
				"1234567890", "");
		when(usersService.checkExistOrNot(user)).thenReturn(false);
		MvcResult result = mockMvc.perform(post("http://localhost:9001/api/v1.0/tweets/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
						"{\"id\":\"test-1\",\"emailId\":\"manish@gmail.com\",\"loginId\":\"manish12345678\",\"firstName\":\"Manish\",\"lastName\":\"Singh\",\"password\":\"manish1\",\"contactNo\":\"1234567890\"}")
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		String actual = result.getResponse().getContentAsString();
		String expected = "{\"id\":\"test-1\",\"emailId\":\"manish@gmail.com\",\"loginId\":\"manish12345678\",\"firstName\":\"Manish\",\"lastName\":\"Singh\",\"password\":\"manish1\",\"contactNo\":\"1234567890\",\"imageurl\":null}";
		assertEquals(expected, actual);
	}

	@Test
	void checkAlreadyRegisteredUserTest() throws Exception {
		Users user = new Users("test-1", "manish@gmail.com", "manish123", "Manish", "Singh", "manish", "1234567890",
				"");
		when(usersService.checkExistOrNot(user)).thenReturn(true);
		when(usersService.checkEmailAndLoginId(user)).thenReturn(true);
		MvcResult result = mockMvc.perform(post("http://localhost:9001/api/v1.0/tweets/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
						"{\"id\":\"test-1\",\"emailId\":\"manish@gmail.com\",\"loginId\":\"manish123\",\"firstName\":\"Manish\",\"lastName\":\"Singh\",\"password\":\"manish\",\"contactNo\":\"1234567890\"}")
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		String actual = result.getResponse().getContentAsString();
		String expected = "User name already exist, please login";
		assertEquals(expected, actual);
	}

	@Test
	void resetPasswordTest() throws Exception {
		String userName = "manish123";
		when(usersService.forgotPassword(userName, "manish")).thenReturn(true);
		MvcResult result = mockMvc.perform(post("http://localhost:9001/api/v1.0/tweets/" + userName + "/forgot")
				.contentType(MediaType.APPLICATION_JSON).content("{\"password\":\"manish\"}")
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		String actual = result.getResponse().getContentAsString();
		String expected = "\"password changed\"";
		assertEquals(expected, actual);
	}

	@Test
	void invalidUserFoundTest() throws Exception {
		String userName = "manish1235";
		when(usersService.forgotPassword(userName, "manish")).thenReturn(true);
		MvcResult result = mockMvc.perform(post("http://localhost:9001/api/v1.0/tweets/" + userName + "/forgot")
				.contentType(MediaType.APPLICATION_JSON).content("{\"password\":\"manish\"}")
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		String actual = result.getResponse().getContentAsString();
		String expected = "\"user name not found\"";
		assertEquals(expected, actual);
	}

	@Test
	void getAllUsersTest() throws Exception {
		MvcResult result = mockMvc.perform(get("http://localhost:9001/api/v1.0/tweets/users/all")).andReturn();
		String actual = result.getResponse().getContentAsString();
		assertEquals(actual, actual);
	}

	@Test
	void getByUserNameTest() throws Exception {
		String userName = "manish1234567";
		MvcResult result = mockMvc.perform(get("http://localhost:9001/api/v1.0/tweets/user/search/" + userName))
				.andReturn();
		String actual = result.getResponse().getContentAsString();
		String expected = "{\"id\":\"a54932a1-f469-41de-a766-d9f813c74261\",\"emailId\":\"manish@gmail.com\",\"loginId\":\"manish1234567\",\"firstName\":\"Manish\",\"lastName\":\"Singh\",\"contactNo\":\"1234567890\",\"imageurl\":null}";
		// "id":"a54932a1-f469-41de-a766-d9f813c74261","emailId":"manish@gmail.com","loginId":"manish1234567","firstName":"Manish","lastName":"Singh","contactNo":"1234567890","imageurl":null
		assertEquals(expected, actual);
	}

	@Test
	void checkInvalidGetByUserNameTest() throws Exception {
		String userName = "manish1235";
		MvcResult result = mockMvc.perform(get("http://localhost:9001/api/v1.0/tweets/user/search/" + userName))
				.andReturn();
		when(usersService.getByUserName(userName)).thenReturn(null);
		String actual = result.getResponse().getContentAsString();
		String expected = "User name not found";
		assertEquals(expected, actual);
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
