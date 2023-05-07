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
		checkInputForNewUser(user);
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
		UserEntity existing = this.userCrud.findById(this.giveAllId(userSuperApp,userEmail)).orElseThrow(
							  () -> new UserNotFoundException("could not find user to update by id: "
							  + userSuperApp+"/"+userEmail));
		if(update.getRole()!=null){
			if (this.toEntityAsEnum(update.getRole()) != null)
				throw new UserBadRequestException("Incorrect user role");
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

	private UserEntity toEntity(UserBoundary boundary) {

		UserEntity entity = new UserEntity();

		entity.setUserId(boundary.getUserId().getSuperapp()+"/"+boundary.getUserId().getEmail());

		entity.setUserName(boundary.getUserName());

		entity.setAvatar(boundary.getAvatar());

		entity.setRole(toEntityAsEnum(boundary.getRole()));


		return entity;

	}

	private String giveAllId(String superapp, String email){
		return superapp+"/"+email;
	}

	private void checkInputForNewUser(UserBoundary user){
		if (this.userCrud.findById(user.getUserId().getSuperapp()+"/"+user.getUserId().getEmail()).isPresent())
			throw new UserBadRequestException("User already Exists");
		if (!(user.getUserId().getEmail()).matches("(^[a-zA-Z0-9]*)@([a-zA-Z]*).com"))
			throw new UserBadRequestException("New User Email is incorrect");
		if (user.getUserName() == null || user.getUserName().isBlank()
				|| user.getAvatar() == null || user.getAvatar().isBlank())
			throw new UserBadRequestException("Need to input an username and an avatar for new user");
		if(user.getRole()!=null && this.toEntityAsEnum(user.getRole()) == null)
			throw new UserBadRequestException("Incorrect user role");
	}


}
