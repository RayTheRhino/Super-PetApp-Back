package superapp.logic;

import java.util.List;

import org.springframework.stereotype.Service;
import superapp.bounderies.UserBoundary;

@Service
public class UserServiceRdb implements UsersService {
	
	
		
	@Override
	public UserBoundary createUser(UserBoundary user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserBoundary login(String userSuperApp, String userEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserBoundary update(String userSuperApp, String userEmail, UserBoundary update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserBoundary> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllUsers() {
		// TODO Auto-generated method stub

	}

}
