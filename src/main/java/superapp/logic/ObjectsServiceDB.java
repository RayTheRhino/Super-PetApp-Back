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
import superapp.dataAccess.ObjectCrud;

@Service
public class ObjectsServiceDB implements ObjectServiceWithBindingFunctionality {
    private ObjectCrud objectCrud;

    @Autowired
    public void setObjectCrud(ObjectCrud objectCrud) {
        this.objectCrud = objectCrud;
    }


    @Override
    @Transactional
    public ObjectBoundary CreateObject(ObjectBoundary object) {


        object.setObjectId(new ObjectId("SuperPetApp", UUID.randomUUID().toString()));
        object.setCreationTimestamp(new Date());
        SuperappObjectsEntity entity = this.toEntity(object);
        objectCrud.save(entity);
        object = toBoundary(entity);

        return object;
    }

    @Override
    @Transactional
    public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary update) {

            SuperappObjectsEntity existing = this.objectCrud.findById(objectSuperApp+"/"+internalObjectId).orElseThrow(
            								() -> new SuperappObjectNotFoundException(
            										"could not find message for update by id: "
            										+ internalObjectId));
            if (update.getType() != null) {
                existing.setType(update.getType());
            }
            if (update.getAlias() != null) {
                existing.setAlias(update.getAlias());
            }
            if (update.getActive() != null)
                existing.setActive(update.getActive());

            if (update.getLocation() != null) {
                existing.setLat(update.getLocation().getLat());
                existing.setLng(update.getLocation().getLng());
            }
            if (!update.getObjectDetails().isEmpty())
                existing.setObjectDetails(update.getObjectDetails());

            existing = objectCrud.save(existing);
            return this.toBoundary(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public ObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId) {
        return this.objectCrud.findById(objectSuperApp+"/"+internalObjectId).map(this::toBoundary).orElseThrow(
                () -> new SuperappObjectNotFoundException("Could not find object by id: "+objectSuperApp+"/"+internalObjectId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> getAllObjects() {
        List<SuperappObjectsEntity> list = this.objectCrud.findAll();
        return list
                .stream()
                .map(this::toBoundary)
                .toList();
    }

    @Override
    @Transactional()
    public void deleteAllObjects() {
        this.objectCrud.deleteAll();
    }

    private ObjectBoundary toBoundary(SuperappObjectsEntity entity) {
        ObjectBoundary boundary = new ObjectBoundary();

        boundary.setObjectId(new ObjectId(entity.getObjectSuperapp(), entity.getObjectInternalId()));
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

        entity.setObjectId(boundary.getObjectId().giveAllId());
        entity.setType(boundary.getType());
        entity.setAlias(boundary.getAlias());

        if (boundary.getActive() != null)
        	entity.setActive(boundary.getActive());
        else
        	entity.setActive(false);
        entity.setCreationTimestamp(boundary.getCreationTimestamp());
        if (boundary.getLocation() != null && boundary.getLocation().getLat() != null)
        	entity.setLat(boundary.getLocation().getLat());
        else
        	entity.setLat(0.0);
        if (boundary.getLocation() != null && boundary.getLocation().getLng() != null)
        	entity.setLng(boundary.getLocation().getLng());
        else
        	entity.setLng(0.0);

        entity.setByEmail(boundary.getCreatedBy().getUserId().getEmail());

        entity.setBySuperapp(boundary.getCreatedBy().getUserId().getSuperapp());

        if (boundary.getObjectDetails() != null)
        	entity.setObjectDetails(boundary.getObjectDetails());
        // else, do nothing , the constructor already made a new TreeMap 
        return entity;

    }

    @Override
    @Transactional
    public void ObjectBindingChild(ObjectId parentId, ObjectId childId) {

        if (parentId.giveAllId().equals(childId.giveAllId()))
            throw new SuperappObjectBadRequestException("Can't bind object to itself");
        SuperappObjectsEntity parentObject =
                this.objectCrud
                        .findById(parentId.giveAllId())
                        .orElseThrow(()->new SuperappObjectNotFoundException("could not find origin message by id: " +
                                parentId.giveAllId()));

        SuperappObjectsEntity childObject =
                this.objectCrud
                        .findById(childId.giveAllId())
                        .orElseThrow(()->new SuperappObjectNotFoundException("could not find response message by id: " +
                                childId.giveAllId()));
        if(parentObject.getChildren().contains(childObject))
            throw new SuperappObjectBadRequestException("Object child already exists in children list");
        parentObject.addChild(childObject);

        this.objectCrud.save(parentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> getChildren(String superapp, String internalObjectId) {
        SuperappObjectsEntity parentObject =
                this.objectCrud
                        .findById(this.giveFullId(superapp,internalObjectId))
                        .orElseThrow(()->new SuperappObjectNotFoundException("could not find origin message by id: " +
                                this.giveFullId(superapp,internalObjectId)));
        List<SuperappObjectsEntity> responsesEntities = parentObject.getChildren();
        List<ObjectBoundary> rv = new ArrayList<>();
        for (SuperappObjectsEntity entity : responsesEntities) {
            rv.add(this.toBoundary(entity));
        }
        return rv;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> getParents(String superapp, String internalObjectId) {
        SuperappObjectsEntity childObject =
                this.objectCrud
                        .findById(this.giveFullId(superapp,internalObjectId))
                        .orElseThrow(()->new SuperappObjectNotFoundException("could not find origin message by id: " +
                                this.giveFullId(superapp,internalObjectId)));
        List<SuperappObjectsEntity> rv = this.objectCrud.findAllByChildrenContains(childObject);

        return rv.stream()
                .map(this::toBoundary)
                .toList();
    }

    private String giveFullId(String superapp, String intrenalId){
        return superapp+"/"+intrenalId;
    }
    private void checkInputForNewCommand(ObjectBoundary boundary){

        if ( boundary.getAlias() == null || boundary.getAlias().isEmpty())
            throw new SuperappObjectBadRequestException("Need to input the alias of object");
        if (boundary.getType() == null || boundary.getType().isEmpty())
            throw new SuperappObjectBadRequestException("Need to input the type of object");
        if (boundary.getCreatedBy() == null
                || boundary.getCreatedBy().getUserId() == null
                || boundary.getCreatedBy().getUserId().getEmail() == null
                || boundary.getCreatedBy().getUserId().getEmail().isBlank()
                || boundary.getCreatedBy().getUserId().getSuperapp() == null
                || boundary.getCreatedBy().getUserId().getSuperapp().isBlank())
            throw new SuperappObjectBadRequestException("Need to input the alias and type of object");

    }
}
