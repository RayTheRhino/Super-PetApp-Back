package superapp.logic;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import superapp.bounderies.*;
import superapp.data.MiniappCommandEntity;
import superapp.data.UserRole;
import superapp.dataAccess.MiniappCommandCrud;
import superapp.dataAccess.ObjectCrud;
import superapp.dataAccess.UserCrud;

@Service
public class MiniappCommandServiceDB implements ImprovedMiniappCommandService {
	private MiniappCommandCrud miniappCommandCrud;
	private UserServiceDB userServiceDB;
	private ObjectsServiceDB objectsServiceDB;

	private ObjectMapper jackson;
	private JmsTemplate jmsTemplate;
	private String superapp;

	@Value("${spring.application.name}")
	public void setSuperapp(String superapp){this.superapp = superapp;}

	@Autowired
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
		this.jmsTemplate.setDeliveryDelay(3000L);
	}

	@Autowired
	public void setMiniappCommandCrud(MiniappCommandCrud miniappCommandCrud) {this.miniappCommandCrud = miniappCommandCrud;}

	@Autowired
	public void setUserServiceDB(UserServiceDB userServiceDB) {this.userServiceDB = userServiceDB;}

	@Autowired
	public void setObjectsServiceDB(ObjectsServiceDB objectsServiceDB) {
		this.objectsServiceDB = objectsServiceDB;
	}

	@PostConstruct
	public void setup(){
		this.jackson = new ObjectMapper();
	}

	@Override
	@Deprecated
	public Object invokeCommand(MiniAppCommandBoundary command) { throw new MiniappCommandUnauthorizedException("Unavailable method");}

	private Object ConfigureCommand(MiniAppCommandBoundary command) {
		int size = 10, page = 0;
		for (Map.Entry<String,Object> entry: command.getCommandAttributes().entrySet()) {
			if (entry.getKey().matches("page") && entry.getValue() instanceof Integer)
				page = (int) entry.getValue();
			if (entry.getKey().matches("size") && entry.getValue() instanceof Integer)
				size = (int) entry.getValue();
		}
		String typeObject = switch (command.getCommand()) {
			case "GetAllParkReviews" -> "park-review";
			case "GetAllShopReviews" -> "shop-review";
			case "GetAllMessages" -> "messages";
			default -> "none";
		};
		if (!typeObject.equals("none"))
			return this.objectsServiceDB.getObjectsByType(typeObject,
					command.getInvokedBy().getUserId().getSuperapp(),
					command.getInvokedBy().getUserId().getEmail(),size,page);

		return command;
	}

	@Override
	@Transactional(readOnly = true)
	@Deprecated
	public List<MiniAppCommandBoundary> getAllCommands() {
		throw new MiniappCommandUnauthorizedException("Unavailable method");}

	@Override
	@Transactional(readOnly = true)
	@Deprecated
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniappName) {
		throw new MiniappCommandUnauthorizedException("Unavailable method");}

	@Override
	@Transactional
	@Deprecated
	public void deleteAll() {
		throw new MiniappCommandUnauthorizedException("Unavailable method");
	}

	private MiniAppCommandBoundary toBoundary(MiniappCommandEntity entity) {
		MiniAppCommandBoundary boundary = new MiniAppCommandBoundary();

		boundary.setCommandId(new CommandId(entity.getCommandSuperApp(),
				entity.getCommandMiniApp(),
				entity.getCommandInternalId()));
		boundary.setCommand(entity.getCommand());
		boundary.setCommandAttributes(entity.getCommandAttributes());
		boundary.setInvocationTimestamp(entity.getInvocationTimestamp());
		boundary.setTargetObject(new TargetObject(
				new ObjectId(entity.getTargetSuperapp(),
						entity.getTargetObjectId())));
		boundary.setInvokedBy(new InvokedBy(new UserIdBoundary(entity.getInvokedByEmail(),entity.getInvokedBySuperapp())));
		return boundary;

	}

	private MiniappCommandEntity toEntity(MiniAppCommandBoundary boundary) {
		MiniappCommandEntity entity = new MiniappCommandEntity();

		entity.setCommandId(giveAllId(boundary.getCommandId().getSuperapp(),
				boundary.getCommandId().getMiniapp(),
				boundary.getCommandId().getInternalCommandId()));

		entity.setCommand(boundary.getCommand());

		entity.setInvocationTimestamp(boundary.getInvocationTimestamp());
		entity.setTargetObjectId(boundary.getTargetObject().getObjectId().getInternalObjectId());
		entity.setTargetSuperapp(boundary.getTargetObject().getObjectId().getSuperapp());

		entity.setInvokedByEmail(boundary.getInvokedBy().getUserId().getEmail());

		entity.setInvokedBySuperapp(boundary.getInvokedBy().getUserId().getSuperapp());

		if (boundary.getCommandAttributes() != null)
			entity.setCommandAttributes(boundary.getCommandAttributes());
		return entity;
	}

	private void checkInputForNewCommand(MiniAppCommandBoundary boundary){
		if (boundary.getCommand() == null || boundary.getCommand().isBlank())
			throw new MiniappCommandBadRequestException("New command needs command input");
		if (boundary.getTargetObject() == null
				|| boundary.getTargetObject().getObjectId() == null
				|| boundary.getTargetObject().getObjectId().getInternalObjectId() == null
				|| boundary.getTargetObject().getObjectId().getInternalObjectId().isBlank()
				|| boundary.getTargetObject().getObjectId().getSuperapp() == null
				|| boundary.getTargetObject().getObjectId().getSuperapp().isBlank())
			throw new MiniappCommandBadRequestException("New command needs target object, with object id including internal id and superapp name");
		if (boundary.getInvokedBy() == null
				|| boundary.getInvokedBy().getUserId() == null
				|| boundary.getInvokedBy().getUserId().getEmail() == null
				|| boundary.getInvokedBy().getUserId().getEmail().isBlank()
				|| !boundary.getInvokedBy().getUserId().getEmail().matches("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
				|| boundary.getInvokedBy().getUserId().getSuperapp() == null
				|| boundary.getInvokedBy().getUserId().getSuperapp().isBlank())
			throw new MiniappCommandBadRequestException("New command needs invoked identification, with user id including email and superapp name");
		this.objectsServiceDB.getObjectCrud().findByObjectIdAndActive(boundary.getTargetObject().getObjectId().giveAllId(),true).orElseThrow(()
				-> new SuperappObjectNotFoundException("No such object exists with this id"));
	}

	private String giveAllId(String superapp, String miniapp, String internalId){
		return superapp+"/" + miniapp+"/" + internalId;
	}

	@Override
	@Transactional
	public Object invokeCommand(MiniAppCommandBoundary command, boolean async) {
		this.checkInputForNewCommand(command);
		UserRole userRole = this.userServiceDB.getUserCrud().findById(command.getInvokedBy().getUserId().getSuperapp()+"/"+command.getInvokedBy().getUserId().getEmail())
				.orElseThrow(() -> new UserNotFoundException("No such user exists with this id")).getRole();
		if (userRole != UserRole.MINIAPP_USER)
			throw new MiniappCommandUnauthorizedException("User Role not allowed for method");
		command.setCommandId(new CommandId(this.superapp,command.getCommandId().getMiniapp(),UUID.randomUUID().toString()));
		command.setInvocationTimestamp(new Date());

		if (async){
			try {
				String json = this.jackson.writeValueAsString(command);
				this.jmsTemplate
						.convertAndSend("petcq", json);
			}catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			MiniappCommandEntity entity = this.toEntity(command);
			miniappCommandCrud.save(entity);
		}
//		return ConfigureCommand(command); // check how to process the command , in the meantime, return the command boundary
		return command;
	}

	@JmsListener(destination = "petcq")
	public void listenToCommand(String json){
		try {
			// For now, command process is only to save it
			MiniAppCommandBoundary theCommand = this.jackson
					.readValue(json, MiniAppCommandBoundary.class);
			MiniappCommandEntity entity = this.toEntity(theCommand);
			this.miniappCommandCrud.save(entity);
		}catch (Exception e){
			e.printStackTrace(System.err);
		}
	}


	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAllCommands(String superapp, String email, int size, int page) {
		if (size<=0 || page <0)
			throw new MiniappCommandBadRequestException("Page and size are incorrect, size need to be more then 0 and page 0 or above");
		UserRole userRole = this.userServiceDB.getUserCrud().findById(superapp+"/"+email).orElseThrow(
				() -> new UserNotFoundException("could not find user to login by id: "
						+ superapp +"/"+email)).getRole();
		if (userRole != UserRole.ADMIN)
			throw new SuperappObjectUnauthorizedException("User role is forbidden");

		return this.miniappCommandCrud.findAll(PageRequest.of(page, size, Direction.DESC, "invocationTimestamp","commandId"))
				.stream()
				.map(this::toBoundary)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniappName, String superapp, String email, int size, int page) {
		if (size<=0 || page <0)
			throw new MiniappCommandBadRequestException("Page and size are incorrect, size need to be more then 0 and page 0 or above");

		UserRole userRole = this.userServiceDB.getUserCrud().findById(superapp+"/"+email).orElseThrow(
				() -> new UserNotFoundException("could not find user to login by id: "
						+ superapp +"/"+email)).getRole();
		if (userRole != UserRole.ADMIN)
			throw new SuperappObjectUnauthorizedException("User role is forbidden");

		return this.miniappCommandCrud.findAllByCommandIdContains(miniappName, PageRequest.of(page, size, Direction.DESC, "invocationTimestamp","commandId"))
				.stream()
				.map(this::toBoundary)
				.toList();
	}

	@Override
	@Transactional
	public void deleteAll(String superapp, String email) {
		UserRole userRole = this.userServiceDB.getUserCrud().findById(superapp+"/"+email).orElseThrow(
				() -> new UserNotFoundException("could not find user to login by id: "
						+ superapp +"/"+email)).getRole();
		if (userRole != UserRole.ADMIN)
			throw new SuperappObjectUnauthorizedException("User role is forbidden");
		this.miniappCommandCrud.deleteAll();
	}
}
