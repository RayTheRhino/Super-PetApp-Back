package superapp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import superapp.bounderies.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class ApplicationTests {
	private RestTemplate restTemplate;
	private String url;
	private int port;
	private String appName;
	private String emailADMIN;


	@LocalServerPort
	public void setPort(int port) {
		this.port = port;

	}
	@BeforeEach
	public void init(){
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + this.port;

		CreateAdmin();
	}


	public void CreateAdmin() {
		emailADMIN = "admin1111.@gmail.com";
		// Create ADMIN
		NewUserBoundary newUser = setNewUserBoundaryForTesting(emailADMIN, "ADMIN", "Y", "yosi");

		// Add ADMIN
		UserBoundary user = this.restTemplate.postForObject(this.url + "/superapp/users", newUser, UserBoundary.class);
	}

	@AfterEach
	public void teardown() {
		this.restTemplate.delete(this.url + "/superapp/admin/users?userSuperapp=" + appName + "&userEmail=" + emailADMIN);
	}

	public void contextLoads() throws Exception {
		String st = "x";
		assertThat(st).isEqualTo("x");
	}

	@Value("${spring.application.name:SuperPetApp}")
	public void setSpringApplicatioName(String appName) {
		this.appName = appName;
	}

	public NewUserBoundary setNewUserBoundaryForTesting(String email, String role, String avatar, String username) {
		NewUserBoundary newUser = new NewUserBoundary();
		newUser.setEmail(email);
		newUser.setRole(role);
		newUser.setAvatar(avatar);
		newUser.setUsername(username);
		return newUser;
	}

	@Test
	public void addUser() {

		// Create user
		NewUserBoundary newUser = setNewUserBoundaryForTesting("c2@gmail.com", "SUPERAPP_USER", "yay", "yosi");

		// Add user
		UserBoundary user = this.restTemplate.postForObject(this.url + "/superapp/users", newUser, UserBoundary.class);

		// Tests
		assertThat((user.getUserId().getSuperapp()) != null);
		assertThat(newUser.getEmail()).isEqualTo(user.getUserId().getEmail());
		assertThat(newUser.getAvatar()).isEqualTo(user.getAvatar());
		assertThat(newUser.getRole()).isEqualTo(user.getRole());
		assertThat(newUser.getUsername()).isEqualTo(user.getUsername());
		//	assertThat("x".equals("y"));
	}


	@Test
	public void testLoginUser() {

		// Create user
		NewUserBoundary newUser = setNewUserBoundaryForTesting("c2@gmail.com", "SUPERAPP_USER", "yay", "yosi");

		// Add user
		UserBoundary user = this.restTemplate.postForObject(this.url + "/superapp/users", newUser, UserBoundary.class);

		// Login
		UserBoundary userLoggedIn = this.restTemplate
				.getForObject(this.url + "/superapp/users/login/{superapp}/{email}",
						UserBoundary.class, appName, "c2@gmail.com");

		// Tests
		assertThat(userLoggedIn.getUserId().getEmail()).isEqualTo(user.getUserId().getEmail());
	}


	@Test
	public void updateUser() {

		// Create user
		NewUserBoundary newUser = setNewUserBoundaryForTesting("c2@gmail.com", "SUPERAPP_USER", "yay", "yosi");

		// Add user
		UserBoundary user = this.restTemplate.postForObject(this.url + "/superapp/users", newUser, UserBoundary.class);

		// Change values
		user.setAvatar("yyy");

		// update
		this.restTemplate.put(this.url + "/superapp/users/{superapp}/{userEmail}",
				user, appName, "c2@gmail.com");

		//Get updated User
		UserBoundary userLoggedIn = this.restTemplate
				.getForObject(this.url + "/superapp/users/login/{superapp}/{email}",
						UserBoundary.class, appName, "c2@gmail.com");

		// Tests
		assertThat(userLoggedIn.getAvatar()).isEqualTo("yyy");
	}
}