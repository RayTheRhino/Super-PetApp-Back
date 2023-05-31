package superapp.logic;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	private ObjectMapper jackson;
	private Log logger = LogFactory.getLog(UserServiceDB.class);

	@Value("${spring.application.name}")
	public void setSuperapp(String superapp){this.superapp = superapp;}

	@PostConstruct
	public void setup(){
		this.jackson = new ObjectMapper();
	}

	@Autowired
	public void setUserCrud(UserCrud userCrud) {
		this.userCrud = userCrud;
	}

	@Override
	@Transactional
	public UserBoundary createUser(UserBoundary user) {
		long start = 0;
		if (this.logger.isTraceEnabled())
			start = this.useLogger("createUser", user.toString()); // TODO: before or after checking
		try {
			user.getUserId().setSuperapp(this.superapp);
			checkInputForNewUser(user);
			UserEntity entity = this.toEntity(user);
			entity = this.userCrud.save(entity);
			user = toBoundary(entity);
			return user;
		} finally {
			if (this.logger.isTraceEnabled())
				logTime("createUser",start);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public UserBoundary login(String userSuperApp, String userEmail) {
		long start = 0;
		if (this.logger.isTraceEnabled())
			start = this.useLogger("login",userSuperApp,userEmail);
		try {
			return this.userCrud.findById(userSuperApp + "/" + userEmail).map(this::toBoundary).orElseThrow(
					() -> new UserNotFoundException("could not find user to login by id: "
							+ userSuperApp + "/" + userEmail));
		}finally {
			if (this.logger.isTraceEnabled())
				logTime("login",start);
		}
	}

		@Override
		@Transactional
		public UserBoundary update(String userSuperApp, String userEmail, UserBoundary update) {
			long start = 0;
			if (this.logger.isTraceEnabled())
				start = this.useLogger("update",userSuperApp,userEmail,update.toString());
			try {
				UserEntity existing = this.userCrud.findById(this.giveAllId(userSuperApp, userEmail)).orElseThrow(
						() -> new UserNotFoundException("could not find user to update by id: "
								+ userSuperApp + "/" + userEmail));
				if (update.getRole() != null) {
					if (this.toEntityAsEnum(update.getRole()) == null)
						throw new UserBadRequestException("Incorrect user role");
					existing.setRole(this.toEntityAsEnum(update.getRole()));
				}
				if (update.getAvatar() != null && !update.getAvatar().isBlank()) {
					existing.setAvatar(update.getAvatar());
				}
				if (update.getUsername() != null && !update.getUsername().isBlank()) {
					existing.setUsername(update.getUsername());
				}
				existing = userCrud.save(existing);
				return this.toBoundary(existing);
			} finally {
				if (this.logger.isTraceEnabled())
					logTime("update",start);
			}
		}

		@Override
		@Transactional
		@Deprecated
		public List<UserBoundary> getAllUsers() { throw new UserGoneException("Unavailable method");
		}

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
			long start = 0;
			if (this.logger.isTraceEnabled())
				start = this.useLogger("getAllUsers",superapp,email,String.valueOf(size),String.valueOf(page));
			try {
				if (size <= 0 || page < 0)
					throw new UserBadRequestException("Page and size are incorrect, size need to be more then 0 and page 0 or above");

				UserRole userRole = this.userCrud.findById(giveAllId(superapp, email)).orElseThrow(
						() -> new UserNotFoundException("could not find user to login by id: "
								+ superapp + "/" + email)).getRole();
				if (userRole != UserRole.ADMIN)
					throw new UserUnauthorizedException("User Role is not allowed");
				return this.userCrud.findAll(PageRequest.of(page, size, Direction.DESC, "username", "userId"))
						.stream()
						.map(this::toBoundary)
						.toList();
			} finally {
				if (this.logger.isTraceEnabled())
					logTime("getAllUsers",start);
			}
		}

		@Override
		@Transactional
		public void deleteAllUsers(String superapp, String email) {
			long start = 0;
			if (this.logger.isTraceEnabled())
				start = this.useLogger("deleteAllUsers",superapp,email);
			try {
				UserRole userRole = this.userCrud.findById(giveAllId(superapp, email)).orElseThrow(
						() -> new UserNotFoundException("could not find user to login by id: "
								+ superapp + "/" + email)).getRole();
				if (userRole != UserRole.ADMIN)
					throw new UserUnauthorizedException("User Role is not allowed");
				this.userCrud.deleteAll();
			} finally {
				if (this.logger.isTraceEnabled())
					logTime("deleteAllUsers",start);
			}
		}

		private long useLogger(String function, String... args) {
			StringBuilder buffer = new StringBuilder();
			for ( String arg : args)
				buffer.append(arg).append(" ");
			try {
				String json = this.jackson.writeValueAsString(buffer);
				String invocationDetails = UserServiceDB.class.getName() + "."+function+"(" + json + ")";
				this.logger.trace(invocationDetails);
			} catch (Exception e) {
				this.logger.error(e.getMessage());
				e.printStackTrace();
			}
			this.logger.trace(function +" - begins");
			return System.currentTimeMillis();
		}
		private void logTime(String function,long start){
			long elapsed = System.currentTimeMillis() - start;
			this.logger.debug(function+" - ended after " + elapsed + "ms / "+elapsed/1000.0+"sec");
		}
}
