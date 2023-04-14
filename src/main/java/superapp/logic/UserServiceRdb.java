package superapp.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import superapp.bounderies.*;
import superapp.data.UserEntity;
import superapp.data.UserRole;

@Service
public class UserServiceRdb implements UsersService {
	private UserCrud userCrud;

	@Autowired
	public void setUserCrud(UserCrud userCrud) {
		this.userCrud = userCrud;
	}
		
	@Override
	@Transactional
	public UserBoundary createUser(UserBoundary user) {
		user.setUserId(new UserIdBoundary("", UUID.randomUUID().toString()));
		UserEntity entity = this.toEntity(user);
		entity = this.userCrud.save(entity);

		return this.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public UserBoundary login(String userSuperApp, String userEmail) {
		// TODO: finish
		return null;
	}

	@Override
	@Transactional
	public UserBoundary update(String userSuperApp, String userEmail, UserBoundary update) {
		// TODO: finish
		return null;
	}

	@Override
	@Transactional
	public List<UserBoundary> getAllUsers() {
		Iterable<UserEntity> iterable = this.userCrud.findAll();
		Iterator<UserEntity> iterator = iterable.iterator();
		List<UserBoundary> allUserList = new ArrayList<>();
		while(iterator.hasNext()){
			UserBoundary userBoundary = toBoundary(iterator.next());
			allUserList.add(userBoundary);
		}
		return allUserList;
	}

	@Override
	@Transactional
	public void deleteAllUsers() {
		this.userCrud.deleteAll();

	}
	private UserRole toEntityAsEnum (String value) {
		if (value != null) {
			return UserRole.valueOf(value);
		}else {
			return null;
		}
	}
	private UserBoundary toBoundary(UserEntity entity) {
		UserBoundary boundary = new UserBoundary();
		boundary.setUserId(new UserIdBoundary(entity.getEmail(),entity.getSuperapp()));
		boundary.setRole(entity.getRole().name());
		boundary.setAvatar(entity.getAvatar());
		boundary.setUsername(entity.getUserName());
		return boundary;

	}

	private UserEntity toEntity(UserBoundary boundary) {
		//TODO: exceptions
		//the meaning of the to do is to throw exception
		UserEntity entity = new UserEntity();
		entity.setUserName(boundary.getUsername());
		entity.setAvatar(boundary.getAvatar());
		entity.setRole(toEntityAsEnum(boundary.getRole()));
		entity.setEmail(boundary.getUserId().getEmail());
		return entity;

	}



}
