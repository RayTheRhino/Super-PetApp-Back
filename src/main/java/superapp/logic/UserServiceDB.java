package superapp.logic;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import superapp.bounderies.UserBoundary;
import superapp.bounderies.UserIdBoundary;
import superapp.data.UserEntity;
import superapp.data.UserRole;
import superapp.dataAccess.UserCrud;


@Service
public class UserServiceDB implements UsersService {
	private UserCrud userCrud;

	@Autowired
	public void setUserCrud(UserCrud userCrud) {
		this.userCrud = userCrud;
	}
		
	@Override
	@Transactional
	public UserBoundary createUser(UserBoundary user) {
		if (this.userCrud.findById(user.getUserId().getSuperapp()+"/"+user.getUserId().getEmail()).isPresent())
			throw new UserBadRequestException("User already Exists");
		if ((user.getUserId().getEmail()).matches("(^[a-zA-Z0-9]*)@([a-zA-Z]*).com"))
			throw new UserBadRequestException("New User Email is incorrect");
		if (user.getUserName() == null || user.getUserName().isEmpty()
				|| user.getAvatar() == null || user.getAvatar().isEmpty())
			throw new UserBadRequestException("Need to input an username and an avatar for new user");
		UserEntity entity = this.toEntity(user);
		entity = this.userCrud.save(entity);
		user = toBoundary(entity);
		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public UserBoundary login(String userSuperApp, String userEmail) {
		return this.userCrud.findById(userSuperApp+"/"+userEmail).map(this::toBoundary).orElseThrow(
				() -> new UserNotFoundException("could not find user to login by id: "
						+ userSuperApp+"/"+userEmail));
	}

	@Override
	@Transactional
	public UserBoundary update(String userSuperApp, String userEmail, UserBoundary update) {
		UserEntity existing = this.userCrud.findById(userSuperApp+"/"+userEmail).orElseThrow(
							  () -> new UserNotFoundException("could not find user to update by id: "
							  + userSuperApp+"/"+userEmail));
		if(update.getRole()!=null){
			existing.setRole(this.toEntityAsEnum(update.getRole()));
		}
		if(update.getAvatar()!=null){
			existing.setAvatar(update.getAvatar());
		}
		if(update.getUserName()!=null){
			existing.setUserName(update.getUserName());
		}
		existing = userCrud.save(existing);
		return this.toBoundary(existing);
	}

	@Override
	@Transactional
	public List<UserBoundary> getAllUsers() {
		List<UserEntity> list = this.userCrud.findAll();
		return list
				.stream()
				.map(this::toBoundary)
				.toList();
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
		boundary.setUserId(new UserIdBoundary(entity.getEmail(),entity.getSuperApp()));
		boundary.setRole(entity.getRole().name());
		boundary.setAvatar(entity.getAvatar());
		boundary.setUserName(entity.getUserName());
		return boundary;

	}

	private UserEntity toEntity(UserBoundary boundary) {//TODO: clean unnecessary if and else, for values that cant be default

		UserEntity entity = new UserEntity();
		if (boundary.getUserId().getEmail() == null)
			boundary.getUserId().setEmail("example@email.com");
		entity.setUserId(boundary.getUserId().getSuperapp()+"/"+boundary.getUserId().getEmail());

		if (boundary.getUserName() != null)
			entity.setUserName(boundary.getUserName());
		else
			entity.setUserName("");// TODO: Check if we need to get rid of default values if at creation we will throw exception
		if (boundary.getAvatar() != null)
			entity.setAvatar(boundary.getAvatar());
		else
			entity.setAvatar("");// TODO: Check if we need to get rid of default values if at creation we will throw exception
		if (boundary.getRole() != null)
			entity.setRole(toEntityAsEnum(boundary.getRole()));
		else
			entity.setRole(UserRole.MINIAPP_USER);

		return entity;

	}



}
