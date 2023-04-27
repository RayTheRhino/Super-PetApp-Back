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

		command.setCommandId(new CommandId("SuperPetApp",command.getCommandId().getMiniapp(),UUID.randomUUID().toString()));
		MiniappCommandEntity entity = this.toEntity(command);
		entity.setInvocationTimeStamp(new Date());
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
				.toList(); //TODO: maybe add a sorting by Time
//		Iterable<MiniappCommandEntity> iterable = this.miniappCommandCrud.findAll();
//		Iterator<MiniappCommandEntity> iterator = iterable.iterator();
//		List<MiniAppCommandBoundary> rv = new ArrayList<>();
//		while (iterator.hasNext()) {
//			MiniAppCommandBoundary boundary = this.toBoundary(iterator.next());
//			rv.add(boundary);
//		}
//		return rv;
	}
	@Override
	@Transactional(readOnly = true) 
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniappName) {
		List<MiniappCommandEntity> list = this.miniappCommandCrud.findAll();
		return list
				.stream()
				.filter(t->t.getCommandMiniApp().equals(miniappName))
				.map(this::toBoundary)
				.toList();  // TODO: maybe add a sorting by Time
//		Iterable<MiniappCommandEntity> iterable = this.miniappCommandCrud.findAll();
//		Iterator<MiniappCommandEntity> iterator = iterable.iterator();
//		List<MiniAppCommandBoundary> rv = new ArrayList<>();
//		while (iterator.hasNext()) {
//			MiniappCommandEntity entity = iterator.next();
//			if (entity.getCommandMiniApp().equals(miniappName))
//			{
//				MiniAppCommandBoundary boundary = this.toBoundary(entity);
//				rv.add(boundary);
//			}
//		}
//		return rv;
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
			String objectId = boundary.getCommandId().getSuperapp()+
							"/" + boundary.getCommandId().getMiniapp()+
							"/" + boundary.getCommandId().getInternalCommandId();
			entity.setCommandId(objectId);
		}
		else {
			String id = UUID.randomUUID().toString();
			entity.setCommandId("SuperPetApp/default/"+id);
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
