package superapp.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "UserEntity")
public class UserEntity {
	@Id
	private String userId; // userId = superapp + email
	private String username;
	private String avatar;
	private UserRole role;
	
	
	public UserEntity() {	
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSuperApp() { return userId.split("/")[0];}

	public String getEmail() { return userId.split("/")[1];}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserEntity{" +
				"userId='" + userId + '\'' +
				", userName='" + username + '\'' +
				", avatar='" + avatar + '\'' +
				", role=" + role +
				'}';
	}
}
