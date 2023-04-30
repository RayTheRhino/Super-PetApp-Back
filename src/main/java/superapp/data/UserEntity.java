package superapp.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import jakarta.persistence.Entity;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;

//@Entity
//@Table(name="UserEntity")
@Document(collection = "UserEntity")
public class UserEntity {
	@Id
	private String userId; // userId = superapp + email
	private String userName;
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
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
				", userName='" + userName + '\'' +
				", avatar='" + avatar + '\'' +
				", role=" + role +
				'}';
	}
}
