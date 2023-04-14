package superapp.data;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="UserEntity")
public class UserEntity {
	@Id private String email;
	private String superapp;
	private String userName;
	private String avatar;
	@Enumerated(EnumType.STRING)
	private UserRole role;
	
	
	public UserEntity() {	
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSuperapp() {
		return superapp;
	}

	public void setSuperapp(String superapp) {
		this.superapp = superapp;
	}

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
				"email='" + email + '\'' +
				", superapp='" + superapp + '\'' +
				", userName='" + userName + '\'' +
				", avatar='" + avatar + '\'' +
				", role=" + role +
				'}';
	}
}
