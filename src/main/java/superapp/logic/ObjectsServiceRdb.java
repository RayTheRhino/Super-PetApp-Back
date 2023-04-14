package superapp.logic;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import superapp.bounderies.CreatedBy;
import superapp.bounderies.Location;
import superapp.bounderies.ObjectBoundary;
import superapp.bounderies.ObjectId;
import superapp.bounderies.UserIdBoundary;
import superapp.data.SuperappObjectsEntity;

@Service
public class ObjectsServiceRdb implements ObjectsService {
	private ObjectCrud objectCrud;

	@Autowired
	public void setMiniappCommandCrud(ObjectCrud objectCrud) {
		this.objectCrud = objectCrud;
	}

	
	@Override
	@Transactional
	public ObjectBoundary CreateObject(ObjectBoundary object) {
		object.setObjectId(new ObjectId("Super Pet App",UUID.randomUUID().toString()));
		object.setCreationTimestamp(new Date());
		SuperappObjectsEntity entity = this.toEntity(object);
		objectCrud.save(entity);

		return object;
	}

	@Override
	@Transactional
	public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary update) {
		//SuperappObjectsEntity exiating = this.objectCrud.findById(id).orElse(()->new Supe)


		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ObjectBoundary> getSpecificObject(String objectSuperApp, String internalObjectId) {
		return this.objectCrud.findById(internalObjectId).map(this::toBoundary);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjects() {
		Iterable<SuperappObjectsEntity> iterable = this.objectCrud.findAll();
		Iterator<SuperappObjectsEntity> iterator = iterable.iterator();
		List<ObjectBoundary> allObjList = new ArrayList<>();
		while(iterator.hasNext()) {
			ObjectBoundary boundary = toBoundary(iterator.next()); 
			allObjList.add(boundary);
		}
		return allObjList;
	}

	@Override
	@Transactional()
	public void deleteAllObjects() {
		this.objectCrud.deleteAll();
	}
	
	private ObjectBoundary toBoundary(SuperappObjectsEntity entity) {
		ObjectBoundary boundary = new ObjectBoundary();
		
		boundary.setObjectId(new ObjectId(entity.getSuperapp(),entity.getInternalObjectId()));
		boundary.setType(entity.getType());
		boundary.setAlias(entity.getAlias());
		boundary.setActive(entity.getActive());
		boundary.setCreationTimestamp(entity.getCreationTimestamp());
		boundary.setLocation(new Location(entity.getLat(),entity.getLng()));
		boundary.setCreatedBy(new CreatedBy(new UserIdBoundary(entity.getByEmail(),entity.getBySuperapp())));
		boundary.setObjectDetails(entity.getObjectDetails());

		return boundary;
		
	}
	
	private SuperappObjectsEntity toEntity(ObjectBoundary boundary) {
		SuperappObjectsEntity entity = new SuperappObjectsEntity();
		
		// TODO: discuss which attributes to entity will be default values
		//the meaning of the to do is to throw exception
		entity.setInternalObjectId(boundary.getObjectId().getInternalObjectId());
		entity.setSuperapp(boundary.getObjectId().getSuperapp());
		entity.setType(boundary.getType());
		entity.setAlias(boundary.getAlias());
		entity.setActive(boundary.getActive());
		entity.setCreationTimestamp(boundary.getCreationTimestamp());
		entity.setLat(boundary.getLocation().getLat());
		entity.setLng(boundary.getLocation().getLng());
		entity.setByEmail(boundary.getCreatedBy().getUserId().getEmail());
		entity.setBySuperapp(boundary.getCreatedBy().getUserId().getSuperapp());
		entity.setObjectDetails(boundary.getObjectDetails());

		return entity;
		
	}
	
	
	
	

}
