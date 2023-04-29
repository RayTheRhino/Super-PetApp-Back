package superapp.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import superapp.bounderies.*;
import superapp.data.MiniappCommandEntity;

@Service
public class MiniappCommandServiceRdb implements MiniappCommandsService {
	private MiniappCommandCrud miniappCommandCrud;
	
	
	@Autowired
	public void setMiniappCommandCrud(MiniappCommandCrud miniappCommandCrud) {
		this.miniappCommandCrud = miniappCommandCrud;
	}

	@Override
	public Object invokeCommand(MiniAppCommandBoundary command) { // For now it will just convert to entity and insert to db. Later add real logic...
		
		try{
			MiniappCommandEntity entity = this.toEntity(command);
			if(entity!=null){ //TODO: change this, its always true
				entity.setInvocationTimeStamp(new Date());
				miniappCommandCrud.save(entity);
				command = this.toBoundary(entity);
				//For now just return the same object from request. Later change this to return the relevant object based on the request and logic
				return command;
			}
		}catch (Exception ex){
			System.out.println("Failed to invoke command: " + ex.getMessage());
		}


		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAllCommands() {
		Iterable<MiniappCommandEntity> iterable = this.miniappCommandCrud.findAll();
		Iterator<MiniappCommandEntity> iterator = iterable.iterator();
		List<MiniAppCommandBoundary> rv = new ArrayList<>();
		while (iterator.hasNext()) {
			MiniAppCommandBoundary boundary = this.toBoundary(iterator.next()); 
			rv.add(boundary);
		}
		return rv;
	}
	// TODO:the meaning of the to do is to throw exception
	@Override
	@Transactional(readOnly = true) 
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniappName) {
		Iterable<MiniappCommandEntity> iterable = this.miniappCommandCrud.findAll();
		Iterator<MiniappCommandEntity> iterator = iterable.iterator();
		List<MiniAppCommandBoundary> rv = new ArrayList<>();
		while (iterator.hasNext()) {
			MiniappCommandEntity entity = iterator.next();
			if (entity.getCommandMiniapp().equals(miniappName))
			{	
				MiniAppCommandBoundary boundary = this.toBoundary(entity); 
				rv.add(boundary);
			}
		}
		return rv;
	}

	@Override
	@Transactional
	public void deleteAll() {
		this.miniappCommandCrud.deleteAll();

	}
	
	private MiniAppCommandBoundary toBoundary(MiniappCommandEntity entity) {
		MiniAppCommandBoundary boundary = new MiniAppCommandBoundary();
		
		boundary.setCommandId(new CommandId(entity.getCommandSuperapp(),
											entity.getCommandMiniapp(),
											entity.getCommandId()));
		boundary.setCommand(entity.getCommand());
		boundary.setCommandAttribute(entity.getCommandAttribute());
		boundary.setInvocationTimeStamp(entity.getInvocationTimeStamp());
		boundary.setTargetObject(new TargetObject(
								 new ObjectId(entity.getTargetSuperapp(),
										 	  entity.getTargetObjectId())));
		boundary.setInvokedBy(new InvokedBy(new UserIdBoundary(entity.getInvokedByEmail(),entity.getInvokedBySuperapp())));
		return boundary;
		
	}
	
	private MiniappCommandEntity toEntity(MiniAppCommandBoundary boundary) {
		MiniappCommandEntity entity = new MiniappCommandEntity();

		if (boundary.getCommandId() != null) {
			entity.setCommandId(boundary.getCommandId().getInternalCommandId());
			entity.setCommandSuperapp(boundary.getCommandId().getSuperapp());
			entity.setCommandMiniapp(boundary.getCommandId().getMiniapp());
		}
		else {
			entity.setCommandId(UUID.randomUUID().toString());
			entity.setCommandSuperapp("SuperPetApp");
			entity.setCommandMiniapp("");
		}
		if (boundary.getCommand() != null)
			entity.setCommand(boundary.getCommand());
		else
			entity.setCommand("");
		if (boundary.getInvocationTimeStamp() != null)
			entity.setInvocationTimeStamp(boundary.getInvocationTimeStamp());
		else
			entity.setInvocationTimeStamp(new Date());
		if (boundary.getTargetObject() != null
				&& boundary.getTargetObject().getObjectId() != null
				&& boundary.getTargetObject().getObjectId().getInternalObjectId() != null)
			entity.setTargetObjectId(boundary.getTargetObject().getObjectId().getInternalObjectId());
		else
			entity.setTargetObjectId("");
		if (boundary.getTargetObject() != null
				&& boundary.getTargetObject().getObjectId() != null
				&& boundary.getTargetObject().getObjectId().getSuperapp() != null)
			entity.setTargetSuperapp(boundary.getTargetObject().getObjectId().getSuperapp());
		else
			entity.setTargetSuperapp("SuperPetApp");
		if (boundary.getCommandAttribute() != null)
			entity.setCommandAttribute(boundary.getCommandAttribute());
		// else, do nothing , there is already a treemap in constructor
		if (boundary.getInvokedBy() != null
				&& boundary.getInvokedBy().getUserId() != null
				&& boundary.getInvokedBy().getUserId().getEmail() != null)
			entity.setInvokedByEmail(boundary.getInvokedBy().getUserId().getEmail());
		else
			entity.setInvokedByEmail("");
		if (boundary.getInvokedBy() != null
				&& boundary.getInvokedBy().getUserId() != null
				&& boundary.getInvokedBy().getUserId().getEmail() != null)
			entity.setInvokedBySuperapp(boundary.getInvokedBy().getUserId().getSuperapp());
		else
			entity.setInvokedBySuperapp("SuperPetApp");
		return entity;

	}

}
