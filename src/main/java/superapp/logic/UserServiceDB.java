package superapp.logic;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import superapp.bounderies.UserBoundary;
import superapp.bounderies.UserIdBoundary;
import superapp.data.UserEntity;
import superapp.data.UserRole;
import superapp.dataAccess.UserCrud;


@Service
public class UserServiceDB implements ImprovedUsersService {
	private UserCrud userCrud;
	private String superapp;

	@Value("${spring.application.name}")
	public void setSuperapp(String superapp){this.superapp = superapp;}

	@Autowired
	public void setUserCrud(UserCrud userCrud) {
		this.userCrud = userCrud;
	}

	@Override
	@Transactional
	public UserBoundary createUser(UserBoundary user) {
		user.getUserId().setSuperapp(this.superapp);
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
			if (this.toEntityAsEnum(update.getRole()) == null)
				throw new UserBadRequestException("Incorrect user role");
			existing.setRole(this.toEntityAsEnum(update.getRole()));
		}
		if(update.getAvatar()!=null && !update.getAvatar().isBlank()){
			existing.setAvatar(update.getAvatar());
		}
		if(update.getUsername()!=null && !update.getUsername().isBlank()){
			existing.setUsername(update.getUsername());
		}
		existing = userCrud.save(existing);
		return this.toBoundary(existing);
	}

	@Override
	@Transactional
	@Deprecated
	public List<UserBoundary> getAllUsers() { throw new UserGoneException("Unavailable method");	}

	@Override
	@Transactional
	@Deprecated
	public void deleteAllUsers() { throw new UserGoneException("Unavailable method");}
	private UserRole toEntityAsEnum (String value) {
		if (value != null) {
			for (UserRole role : UserRole.values())
				if (value.equals(role.name()))
					return UserRole.valueOf(value);
		}
		return null;
	}
	private UserBoundary toBoundary(UserEntity entity) {
		UserBoundary boundary = new UserBoundary();
		boundary.setUserId(new UserIdBoundary(entity.getEmail(),entity.getSuperApp()));
		boundary.setRole(entity.getRole().name());
		boundary.setAvatar(entity.getAvatar());
		boundary.setUsername(entity.getUsername());
		return boundary;

	}

	private UserEntity toEntity(UserBoundary boundary) {

		UserEntity entity = new UserEntity();
		entity.setUserId(boundary.getUserId().getSuperapp()+"/"+boundary.getUserId().getEmail());
		entity.setUsername(boundary.getUsername());
		entity.setAvatar(boundary.getAvatar());
		entity.setRole(toEntityAsEnum(boundary.getRole()));

		return entity;
	}

	private String giveAllId(String superapp, String email){return superapp+"/"+email;}

	private void checkInputForNewUser(UserBoundary user){
		if (this.userCrud.findById(user.getUserId().getSuperapp()+"/"+user.getUserId().getEmail()).isPresent())
			throw new UserBadRequestException("User already Exists");
		if (!(user.getUserId().getEmail()).matches("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$"))
			throw new UserBadRequestException("New User Email is incorrect");
		if (user.getUsername() == null || user.getUsername().isBlank()
				|| user.getAvatar() == null || user.getAvatar().isBlank())
			throw new UserBadRequestException("Need to input an username and an avatar for new user");
		if(user.getRole()!=null && this.toEntityAsEnum(user.getRole()) == null)
			throw new UserBadRequestException("Incorrect user role");
	}
	public UserCrud getUserCrud(){ return this.userCrud;}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(String superapp, String email, int size, int page) {
		if (size<=0 || page <0)
			throw new UserBadRequestException("Page and size are incorrect, size need to be more then 0 and page 0 or above");

		UserRole userRole = this.userCrud.findById(giveAllId(superapp,email)).orElseThrow(
				() -> new UserNotFoundException("could not find user to login by id: "
						+ superapp+"/"+email)).getRole();
		if (userRole != UserRole.ADMIN)
			throw new UserUnauthorizedException("User Role is not allowed");
		return this.userCrud.findAll(PageRequest.of(page, size, Direction.DESC, "username","userId"))
				.stream()
				.map(this::toBoundary)
				.toList();
	}

	@Override
	@Transactional
	public void deleteAllUsers(String superapp, String email) {
		UserRole userRole = this.userCrud.findById(giveAllId(superapp,email)).orElseThrow(
				() -> new UserNotFoundException("could not find user to login by id: "
						+ superapp+"/"+email)).getRole();
		if (userRole != UserRole.ADMIN)
			throw new UserUnauthorizedException("User Role is not allowed");
		this.userCrud.deleteAll();
	}
}
