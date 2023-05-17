package superapp.logic;

import superapp.bounderies.UserBoundary;

import java.util.List;

public interface ImprovedUsersService extends UsersService{

    public List<UserBoundary> getAllUsers(String superapp, String email, int size, int page);
    public void deleteAllUsers(String superapp, String email);
}
