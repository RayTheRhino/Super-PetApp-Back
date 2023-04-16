package superapp.logic;

import java.util.List;
import java.util.Optional;

import superapp.bounderies.UserBoundary;

public interface UsersService {

	public UserBoundary createUser(UserBoundary user);
	
	public Optional<UserBoundary> login(String userSuperApp, String userEmail);
	
	public UserBoundary update(String userSuperApp, String userEmail, UserBoundary update) throws Exception;
	
	public List<UserBoundary> getAllUsers();
	
	public void deleteAllUsers();

}
