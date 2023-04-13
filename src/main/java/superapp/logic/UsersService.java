package superapp.logic;

import java.util.List;

import org.springframework.stereotype.Service;
import superapp.bounderies.UserBoundary;

public interface UsersService {

	public UserBoundary createUser(UserBoundary user);
	
	public UserBoundary login(String userSuperApp, String userEmail);
	
	public UserBoundary update(String userSuperApp, String userEmail, UserBoundary update);
	
	public List<UserBoundary> getAllUsers();
	
	public void deleteAllUsers();

}
