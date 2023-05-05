package superapp.logic;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import superapp.bounderies.*;
import superapp.data.MiniappCommandEntity;
import superapp.dataAccess.MiniappCommandCrud;

@Service
public class MiniappCommandServiceDB implements MiniappCommandsService {
	private MiniappCommandCrud miniappCommandCrud;
	
	
	@Autowired
	public void setMiniappCommandCrud(MiniappCommandCrud miniappCommandCrud) {
		this.miniappCommandCrud = miniappCommandCrud;
	}

	@Override
	public Object invokeCommand(MiniAppCommandBoundary command) { // For now, it will just convert to entity and insert to db. Later add real logic...
		this.checkInputForNewCommand(command);
		command.setCommandId(new CommandId("SuperPetApp",command.getCommandId().getMiniapp(),UUID.randomUUID().toString()));
		command.setInvocationTimestamp(new Date());
		MiniappCommandEntity entity = this.toEntity(command);
		miniappCommandCrud.save(entity);
		command = this.toBoundary(entity);
		return command;

	}

	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAllCommands() {
		List<MiniappCommandEntity> list = this.miniappCommandCrud.findAll();
		return list
				.stream()
				.map(this::toBoundary)
				.toList();
	}
	@Override
	@Transactional(readOnly = true) 
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniappName) {
		List<MiniappCommandEntity> list = this.miniappCommandCrud.findAll();
		return list
				.stream()
				.filter(t->t.getCommandMiniApp().equals(miniappName))
				.map(this::toBoundary)
				.toList();

	}

	@Override
	@Transactional
	public void deleteAll() {
		this.miniappCommandCrud.deleteAll();

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
		// else, do nothing , there is already a treemap in constructor


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
			|| boundary.getInvokedBy().getUserId().getSuperapp() == null
			|| boundary.getInvokedBy().getUserId().getSuperapp().isBlank())
			throw new MiniappCommandBadRequestException("New command needs invoked identification, with user id including email and superapp name");
	}

	private String giveAllId(String superapp, String miniapp, String internalId){
		return superapp+"/" + miniapp+"/" + internalId;
	}

}
