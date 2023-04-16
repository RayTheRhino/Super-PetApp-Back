package superapp.logic;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import superapp.bounderies.UserBoundary;
import superapp.bounderies.UserIdBoundary;
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
		user.setUserId(new UserIdBoundary(user.getUserId().getEmail(), "SuperPetApp"));
		UserEntity entity = this.toEntity(user);
		entity = this.userCrud.save(entity);

		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserBoundary> login(String userSuperApp, String userEmail) {
		return this.userCrud.findById(userEmail).map(this::toBoundary);
	}

	@Override
	@Transactional
	public UserBoundary update(String userSuperApp, String userEmail, UserBoundary update) {
		UserEntity existing = this.userCrud.findById(userEmail).orElseThrow(
							  () -> new UserNotFoundException("could not find message for update by id: "
							  + userEmail)); 
		if(update.getUserId()!=null){
			if(update.getRole()!=null){
				existing.setRole(this.toEntityAsEnum(update.getRole()));
			}
			if(update.getAvatar()!=null){
				existing.setAvatar(update.getAvatar());
			}
			if(update.getUserName()!=null){
				existing.setUserName(update.getUserName());
			}
		}
		if(userSuperApp!=null){
			existing.setSuperapp(userSuperApp);
		}
		if(userEmail!=null){
			existing.setEmail(userEmail);
		}
		existing = userCrud.save(existing);
		return this.toBoundary(existing);
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
		boundary.setUserName(entity.getUserName());
		return boundary;

	}

	private UserEntity toEntity(UserBoundary boundary) {
		//TODO:  throw exception
		UserEntity entity = new UserEntity();
		
		if (boundary.getUserName() != null)
			entity.setUserName(boundary.getUserName());
		else
			entity.setUserName("");
		if (boundary.getAvatar() != null)
			entity.setAvatar(boundary.getAvatar());
		else
			entity.setAvatar("");
		if (boundary.getRole() != null)
			entity.setRole(toEntityAsEnum(boundary.getRole()));
		else
			entity.setRole(null);
		if (boundary.getUserId() != null 
				&& boundary.getUserId().getSuperapp() != null)
			entity.setSuperapp(boundary.getUserId().getSuperapp());
		else
			entity.setSuperapp("SuperPetApp");
		if (boundary.getUserId() != null 
				&& boundary.getUserId().getEmail() != null)
			entity.setEmail(boundary.getUserId().getEmail());
		else
			entity.setEmail("");
		return entity;

	}



}
