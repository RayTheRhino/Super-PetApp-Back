package superapp.logic;

import java.util.Date;
import java.util.List;
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
	private UserCrud userCrud;
	private ObjectsService objectsService;

	private ObjectCrud objectCrud;
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
	public void setObjectCrud(ObjectCrud objectCrud) {this.objectCrud = objectCrud;}

	@Autowired
	public void setUserCrud(UserCrud userCrud) {this.userCrud = userCrud;}

	@Autowired
	public void setObjectCrud(ObjectsService objectsServiceDB) {
		this.objectsService = objectsServiceDB;
	}

	@PostConstruct
	public void setup(){
		this.jackson = new ObjectMapper();
	}

	@Override
	@Deprecated
	public Object invokeCommand(MiniAppCommandBoundary command) { throw new MiniappCommandUnauthorizedException("Unavailable method");}

	private Object ConfigureCommand(MiniAppCommandBoundary command) {
		if(command.getCommand().equals("GetAllParkReviews")) {
			List<ObjectBoundary> list = this.objectsService.getAllObjects();
			List<ObjectBoundary> parkReviews = list.stream().filter(x -> x.getType().equals("park-review")).collect(Collectors.toList());
			return parkReviews;
		}
		if(command.getCommand().equals("GetAllShopReviews")){
			List<ObjectBoundary> list = this.objectsService.getAllObjects();
			List<ObjectBoundary> shopReviews = list.stream().filter(x -> x.getType().equals("shop-review")).collect(Collectors.toList());
			return shopReviews;
		}
		if(command.getCommand().equals("GetAllMessages")){
			List<ObjectBoundary> list = this.objectsService.getAllObjects();
			List<ObjectBoundary> messages = list.stream().filter(x -> x.getType().equals("message")).collect(Collectors.toList());
			return messages;
		}
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
		this.objectCrud.findByObjectIdAndActive(boundary.getTargetObject().getObjectId().giveAllId(),true).orElseThrow(()
				-> new SuperappObjectNotFoundException("No such object exists with this id"));
	}

	private String giveAllId(String superapp, String miniapp, String internalId){
		return superapp+"/" + miniapp+"/" + internalId;
	}

	@Override
	@Transactional
	public Object invokeCommand(MiniAppCommandBoundary command, boolean async) {
		this.checkInputForNewCommand(command);
		UserRole userRole = this.userCrud.findById(command.getInvokedBy().getUserId().getSuperapp()+"/"+command.getInvokedBy().getUserId().getEmail())
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
		}
		else {
			MiniappCommandEntity entity = this.toEntity(command);
			miniappCommandCrud.save(entity);
		}
		return ConfigureCommand(command);
	}

	@JmsListener(destination = "petcq")
	public void listenToCommand(String json){
		try {
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
		UserRole userRole = this.userCrud.findById(superapp+"/"+email).orElseThrow(
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

		UserRole userRole = this.userCrud.findById(superapp+"/"+email).orElseThrow(
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
		UserRole userRole = this.userCrud.findById(superapp+"/"+email).orElseThrow(
				() -> new UserNotFoundException("could not find user to login by id: "
						+ superapp +"/"+email)).getRole();
		if (userRole != UserRole.ADMIN)
			throw new SuperappObjectUnauthorizedException("User role is forbidden");
		this.miniappCommandCrud.deleteAll();
	}
}
