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
    public void setObjectCrudCrud(ObjectCrud objectCrud) {
        this.objectCrud = objectCrud;
    }


    @Override
    @Transactional
    public ObjectBoundary CreateObject(ObjectBoundary object) {
        object.setObjectId(new ObjectId("SuperPetApp", UUID.randomUUID().toString()));
        object.setCreationTimestamp(new Date());
        SuperappObjectsEntity entity = this.toEntity(object);
        objectCrud.save(entity);

        return object;
    }

    @Override
    @Transactional
    public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary update) {

            SuperappObjectsEntity existing = this.objectCrud.findById(internalObjectId).orElseThrow(
            								() -> new SuperappObjectNotFoundException(
            										"could not find message for update by id: "
            										+ internalObjectId));
//            if (update.getObjectId() != null) {
//                if (update.getObjectId().getInternalObjectId() != null) {
//                    existing.setInternalObjectId(update.getObjectId().getInternalObjectId());
//                }
//                if (update.getObjectId().getSuperapp() != null) {
//                    existing.setSuperapp(update.getObjectId().getSuperapp());
//                }
//            } /// Dont change the id or superapp with update 
            if (update.getType() != null) {
                existing.setType(update.getType());
            }
            if (update.getAlias() != null) {
                existing.setAlias(update.getAlias());
            }
            existing.setActive(update.getActive());

//            if (update.getCreationTimestamp() != null) {
//                existing.setCreationTimestamp(update.getCreationTimestamp());
//            } /// Dont change the timestampcreation with update 
            if (update.getLocation() != null) {
                existing.setLat(update.getLocation().getLat());
                existing.setLng(update.getLocation().getLng());
            }
//            if (update.getCreatedBy() != null) {
//                if (update.getCreatedBy().getUserId() != null) {
//                    if (update.getCreatedBy().getUserId().getEmail() != null) {
//                        existing.setByEmail(update.getCreatedBy().getUserId().getEmail());
//                    }
//                    if (update.getCreatedBy().getUserId().getSuperapp() != null) {
//                        existing.setBySuperapp(update.getCreatedBy().getUserId().getSuperapp());
//                    }
//                }
//            } /// Dont change the createdby update 
            existing = objectCrud.save(existing);
            return this.toBoundary(existing);
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
        while (iterator.hasNext()) {
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

        boundary.setObjectId(new ObjectId(entity.getSuperapp(), entity.getInternalObjectId()));
        boundary.setType(entity.getType());
        boundary.setAlias(entity.getAlias());
        boundary.setActive(entity.getActive());
        boundary.setCreationTimestamp(entity.getCreationTimestamp());
        boundary.setLocation(new Location(entity.getLat(), entity.getLng()));
        boundary.setCreatedBy(new CreatedBy(new UserIdBoundary(entity.getByEmail(), entity.getBySuperapp())));
        boundary.setObjectDetails(entity.getObjectDetails());

        return boundary;

    }

    private SuperappObjectsEntity toEntity(ObjectBoundary boundary) {
        SuperappObjectsEntity entity = new SuperappObjectsEntity();

        // TODO:  throw exception
        entity.setInternalObjectId(boundary.getObjectId().getInternalObjectId());
        entity.setSuperapp(boundary.getObjectId().getSuperapp());
        if (boundary.getType() != null)
        	entity.setType(boundary.getType());
        else
        	entity.setType("");
        if (boundary.getAlias() != null)
        	entity.setAlias(boundary.getAlias());
        else
        	entity.setAlias("");
        if (boundary.getActive() != null)
        	entity.setActive(boundary.getActive());
        else
        	entity.setActive(false);
        if (boundary.getCreationTimestamp() != null)
        	entity.setCreationTimestamp(boundary.getCreationTimestamp());
        else
        	entity.setCreationTimestamp(new Date());
        if (boundary.getLocation() != null && boundary.getLocation().getLat() != null)
        	entity.setLat(boundary.getLocation().getLat());
        else
        	entity.setLat(0.0);
        if (boundary.getLocation() != null && boundary.getLocation().getLng() != null)
        	entity.setLng(boundary.getLocation().getLng());
        else
        	entity.setLng(0.0);
        if (boundary.getCreatedBy() != null 
        		&& boundary.getCreatedBy().getUserId() != null
        		&& boundary.getCreatedBy().getUserId().getEmail() != null)
        	entity.setByEmail(boundary.getCreatedBy().getUserId().getEmail());
        else
        	entity.setByEmail("");
        if (boundary.getCreatedBy() != null 
        	&& boundary.getCreatedBy().getUserId() != null
        	&& boundary.getCreatedBy().getUserId().getSuperapp() != null)
        	entity.setBySuperapp(boundary.getCreatedBy().getUserId().getSuperapp());
        else
        	entity.setBySuperapp("SuperPetApp");
        if (boundary.getObjectDetails() != null)
        	entity.setObjectDetails(boundary.getObjectDetails());
        // else, do nothing , the constructor already made a new TreeMap 
        return entity;

    }


}
