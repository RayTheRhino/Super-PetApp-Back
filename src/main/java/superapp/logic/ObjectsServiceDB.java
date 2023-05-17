package superapp.logic;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import superapp.bounderies.CreatedBy;
import superapp.bounderies.Location;
import superapp.bounderies.ObjectBoundary;
import superapp.bounderies.ObjectId;
import superapp.bounderies.UserIdBoundary;
import superapp.data.SuperappObjectsEntity;
import superapp.data.UserRole;
import superapp.dataAccess.ObjectCrud;
import superapp.dataAccess.UserCrud;

@Service
public class ObjectsServiceDB implements ObjectServiceWithBindingFunctionality {
    private ObjectCrud objectCrud;
    private UserCrud userCrud;
    private String superapp;

    @Value("${spring.application.name}")
    public void setSuperapp(String superapp){this.superapp = superapp;}
    @Autowired
    public void setObjectCrud(ObjectCrud objectCrud) {this.objectCrud = objectCrud;}
    @Autowired
    public void setUserCrud(UserCrud userCrud) {this.userCrud = userCrud;}


    @Override
    @Transactional
    public ObjectBoundary CreateObject(ObjectBoundary object) {
        object.getObjectId().setSuperapp(this.superapp);
        checkInputForNewObject(object);
        UserRole userRole = this.userCrud.findById(object.getCreatedBy().getUserId().getSuperapp()+"/"+
                object.getCreatedBy().getUserId().getEmail()).orElseThrow(() -> new UserNotFoundException("could not find user by id")).getRole();

        if (userRole != UserRole.SUPERAPP_USER)
            throw new SuperappObjectUnauthorizedException("User Role is not allowed");
        object.setObjectId(new ObjectId(UUID.randomUUID().toString()));
        object.setCreationTimestamp(new Date());
        SuperappObjectsEntity entity = this.toEntity(object);
        objectCrud.save(entity);
        object = toBoundary(entity);
        return object;
    }

    @Override
    @Transactional
    @Deprecated
    public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary update) {
        throw new SuperappObjectGoneException("Unavailable method");   }

    @Override
    @Transactional(readOnly = true)
    @Deprecated
    public ObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId) {
        throw new SuperappObjectGoneException("Unavailable method"); }

    @Override
    @Transactional(readOnly = true)
    @Deprecated
    public List<ObjectBoundary> getAllObjects() {
        throw new SuperappObjectGoneException("Unavailable method");  }

    @Override
    @Transactional()
    @Deprecated
    public void deleteAllObjects() { throw new SuperappObjectGoneException("Unavailable method");   }

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
    public void ObjectBindingChild(ObjectId parentId, ObjectId childId, String userSuperapp, String email) { // TODO: need to update for user role check
        //TODO: prevent child to be a parent of the his parent
        UserRole userRole = this.userCrud.findById(userSuperapp+"/"+email).orElseThrow(
                () -> new UserNotFoundException("could not find user by id: "
                        + userSuperapp+"/"+email)).getRole();
        if (userRole != UserRole.SUPERAPP_USER)
            throw new SuperappObjectUnauthorizedException("User Role is not allowed");

        if (parentId.giveAllId().equals(childId.giveAllId()))
            throw new SuperappObjectBadRequestException("Can't bind object to itself");
        SuperappObjectsEntity parentObject =
                this.objectCrud
                        .findById(parentId.giveAllId())
                        .orElseThrow(()->new SuperappObjectNotFoundException("could not find parent object by id: " +
                                parentId.giveAllId()));

        SuperappObjectsEntity childObject =
                this.objectCrud
                        .findById(childId.giveAllId())
                        .orElseThrow(()->new SuperappObjectNotFoundException("could not find child object by id: " +
                                childId.giveAllId()));
        if(parentObject.getChildren().contains(childObject))
            throw new SuperappObjectBadRequestException("Object child already exists in children list");
        parentObject.addChild(childObject);
        childObject.addParent(parentObject);
        this.objectCrud.save(childObject);
        this.objectCrud.save(parentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> getChildren(String superapp, String internalObjectId, String userSuperapp, String email,
                                            int size, int page) { // TODO: nned to update for user role check
        UserRole userRole = this.userCrud.findById(superapp+"/"+email).orElseThrow(
                () -> new UserNotFoundException("could not find user to login by id: "
                        + superapp+"/"+email)).getRole();
        if (userRole == UserRole.ADMIN)
            throw new SuperappObjectUnauthorizedException("User Role is not allowed");
        else if (userRole == UserRole.SUPERAPP_USER) {
            SuperappObjectsEntity parentObject =
                    this.objectCrud
                            .findById(this.giveFullId(superapp, internalObjectId))
                            .orElseThrow(() -> new SuperappObjectNotFoundException("could not find parent object by id: " +
                                    this.giveFullId(superapp, internalObjectId)));
            List<SuperappObjectsEntity> rv = this.objectCrud.findAllByParentsContains(parentObject).orElseThrow(() ->
                    new SuperappObjectNotFoundException("could not find parent object id: " + parentObject.getObjectId()));
            // TODO: change to return empty list instead of exception
            return rv.stream()
                    .map(this::toBoundary)
                    .toList();
        }
        else { // MINIAPP_USER
            SuperappObjectsEntity parentObject =
                    this.objectCrud
                            .findByObjectIdAndActive(this.giveFullId(superapp, internalObjectId),true)
                            .orElseThrow(() -> new SuperappObjectNotFoundException("could not find parent object by id: " +
                                    this.giveFullId(superapp, internalObjectId)));
            List<SuperappObjectsEntity> rv = this.objectCrud.findAllByParentsContainsAndActive(parentObject,true).orElseThrow(() ->
                    new SuperappObjectNotFoundException("could not find parent object id: " + parentObject.getObjectId()));
            //TODO: change to return empty list instead of exception
            return rv.stream()
                    .map(this::toBoundary)
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> getParents(String superapp, String internalObjectId, String userSuperapp, String email,
                                           int size, int page) { // TODO: nned to update for user role check
        UserRole userRole = this.userCrud.findById(superapp+"/"+email).orElseThrow(
                () -> new UserNotFoundException("could not find user to login by id: "
                        + superapp+"/"+email)).getRole();
        if (userRole == UserRole.ADMIN)
            throw new SuperappObjectUnauthorizedException("User Role is not allowed");
        else if (userRole == UserRole.SUPERAPP_USER) {
            SuperappObjectsEntity childObject =
                    this.objectCrud
                            .findById(this.giveFullId(superapp, internalObjectId))
                            .orElseThrow(() -> new SuperappObjectNotFoundException("could not find child object by id: " +
                                    this.giveFullId(superapp, internalObjectId)));
            List<SuperappObjectsEntity> rv = this.objectCrud.findAllByChildrenContains(childObject).orElseThrow(() ->
                    new SuperappObjectNotFoundException("could not find child object id: " + childObject.getObjectId()));
            //TODO: change to return empty list instead of exception
            return rv.stream()
                    .map(this::toBoundary)
                    .toList();
        }
        else{//MINIAPP_USER
            SuperappObjectsEntity childObject =
                    this.objectCrud
                            .findByObjectIdAndActive(this.giveFullId(superapp, internalObjectId),true)
                            .orElseThrow(() -> new SuperappObjectNotFoundException("could not find child object by id: " +
                                    this.giveFullId(superapp, internalObjectId)));
            List<SuperappObjectsEntity> rv = this.objectCrud.findAllByChildrenContainsAndActive(childObject,true).orElseThrow(() ->
                    new SuperappObjectNotFoundException("could not find child object id: " + childObject.getObjectId()));
            //TODO: change to return empty list instead of exception

            return rv.stream()
                    .map(this::toBoundary)
                    .toList();
        }
    }

    private String giveFullId(String superapp, String intrenalId){
        return superapp+"/"+intrenalId;
    }
    private void checkInputForNewObject(ObjectBoundary boundary){

        if ( boundary.getAlias() == null || boundary.getAlias().isBlank())
            throw new SuperappObjectBadRequestException("Need to input the alias of object");
        if (boundary.getType() == null || boundary.getType().isBlank())
            throw new SuperappObjectBadRequestException("Need to input the type of object");
        if (boundary.getCreatedBy() == null
                || boundary.getCreatedBy().getUserId() == null
                || boundary.getCreatedBy().getUserId().getEmail() == null
                || boundary.getCreatedBy().getUserId().getEmail().isBlank()
                || !boundary.getCreatedBy().getUserId().getEmail().matches("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
                || boundary.getCreatedBy().getUserId().getSuperapp() == null
                || boundary.getCreatedBy().getUserId().getSuperapp().isBlank())
            throw new SuperappObjectBadRequestException("Need to input the alias and type of object");

    }

    @Override
    @Transactional
    public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary update,
                                       String userSuperapp, String email) {
        UserRole userRole = this.userCrud.findById(objectSuperApp+"/"+email).orElseThrow(
                () -> new UserNotFoundException("could not find user to login by id: "
                        + userSuperapp+"/"+email)).getRole();
        if (userRole != UserRole.SUPERAPP_USER)
            throw new UserUnauthorizedException("User Role is not allowed");

        SuperappObjectsEntity existing = this.objectCrud.findById(objectSuperApp+"/"+internalObjectId).orElseThrow(
                () -> new SuperappObjectNotFoundException(
                        "could not find object for update by id: "+
                        objectSuperApp+"/"+ internalObjectId));
        if (update.getType() != null && !update.getType().isBlank()) {
            existing.setType(update.getType());
        }
        if (update.getAlias() != null && !update.getAlias().isBlank()) {
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
    public List<ObjectBoundary> getObjectsByType(String type, String superapp, String email, int size, int page) {
        UserRole userRole = this.userCrud.findById(superapp+"/"+email).orElseThrow(
                () -> new UserNotFoundException("could not find user to login by id: "
                        + superapp+"/"+email)).getRole();
        List<SuperappObjectsEntity> list;
        if (userRole == UserRole.SUPERAPP_USER)
            list = this.objectCrud.findAllByType(type).orElseThrow(() //TODO discard the Optional return and leave the list
                    -> new SuperappObjectNotFoundException("could not find any objects by this type:"+type));
        else if (userRole == UserRole.MINIAPP_USER)
            list = this.objectCrud.findAllByTypeAndActive(type, true).orElseThrow(()
                    -> new SuperappObjectNotFoundException("could not find any objects by this type:"+type));
        else
            throw new SuperappObjectUnauthorizedException("User role is forbidden");
        //TODO: pagination
        return list
                .stream()
                .map(this::toBoundary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> getObjectsByAlias(String alias, String superapp, String email, int size, int page) {
        UserRole userRole = this.userCrud.findById(superapp+"/"+email).orElseThrow(
                () -> new UserNotFoundException("could not find user to login by id: "
                        + superapp+"/"+email)).getRole();
        List<SuperappObjectsEntity> list;
        if (userRole == UserRole.SUPERAPP_USER)
            list = this.objectCrud.findAllByAlias(alias).orElseThrow(()
                    -> new SuperappObjectNotFoundException("could not find any objects by this alias:"+alias));
        else if (userRole == UserRole.MINIAPP_USER)
            list = this.objectCrud.findAllByAliasAndActive(alias,true).orElseThrow(()
                    -> new SuperappObjectNotFoundException("could not find any objects by this alias:"+alias));
        else
            throw new SuperappObjectUnauthorizedException("User role is forbidden");

        //TODO: pagination
        return list
                .stream()
                .map(this::toBoundary)
                .toList();
    }

//    @Override
//    @Transactional(readOnly = true)
//    public List<ObjectBoundary> getObjectsByLocation(double lat, double lng, double distance, String superapp,
//                                                     String email, String distanceUnits, int size, int page) {
//        UserRole userRole = this.userCrud.findById(superapp+"/"+email).orElseThrow(
//                () -> new UserNotFoundException("could not find user to login by id: "
//                        + superapp+"/"+email)).getRole();
//        List<SuperappObjectsEntity> list;
//        if (userRole == UserRole.SUPERAPP_USER)
//            list = this.objectCrud.findAllByLocation(lat,lng).orElseThrow(()
//                    -> new SuperappObjectNotFoundException("could not find any objects in the distance: "+distance+" from: "+lat+", "+lng));
//        else if (userRole == UserRole.MINIAPP_USER)
//            list = this.objectCrud.findAllByLocationAndActive(lat,lng,true).orElseThrow(()
//                    -> new SuperappObjectNotFoundException("could not find any objects in the distance: "+distance+" from: "+lat+", "+lng));
//        else
//            throw new SuperappObjectUnauthorizedException("User role is forbidden");
//        //TODO: pagination
//        return list
//                .stream()
//                .map(this::toBoundary)
//                .toList();
//    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> getAllObjects(String superapp, String email, int size, int page) {
        UserRole userRole = this.userCrud.findById(superapp+"/"+email).orElseThrow(
                () -> new UserNotFoundException("could not find user to login by id: "
                        + superapp+"/"+email)).getRole();
        List<SuperappObjectsEntity> list;
        if (userRole == UserRole.SUPERAPP_USER)
            list = this.objectCrud.findAll(); //todo: MAYBE update the return function
        else if(userRole == UserRole.MINIAPP_USER)
            list = this.objectCrud.findAllByActive(true).orElseThrow(()
                    -> new SuperappObjectNotFoundException("Couldn't find existing objects"));
        else
            throw new SuperappObjectUnauthorizedException("User role is forbidden");

        return list
                .stream()
                .map(this::toBoundary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId, String userSuperapp, String email) {
        UserRole userRole = this.userCrud.findById(userSuperapp + "/" + email).orElseThrow(
                () -> new UserNotFoundException("could not find user to login by id: "
                        + userSuperapp + "/" + email)).getRole();
        if (userRole == UserRole.SUPERAPP_USER) // TODO: update checking role
            return  this.objectCrud.findById(objectSuperApp + "/" + internalObjectId).map(this::toBoundary).orElseThrow(
                    () -> new SuperappObjectNotFoundException("Could not find object by id: " + objectSuperApp + "/" + internalObjectId));
        else if (userRole == UserRole.MINIAPP_USER)
            return this.objectCrud.findByObjectIdAndActive(objectSuperApp + "/" + internalObjectId,true).map(this::toBoundary).orElseThrow(
                    () -> new SuperappObjectNotFoundException("Could not find object by id: " + objectSuperApp + "/" + internalObjectId));
        else //ADMIN
            throw new SuperappObjectUnauthorizedException("User role is forbidden");
    }

    @Override
    @Transactional
    public void deleteAllObjects(String superapp, String email) {
        UserRole userRole = this.userCrud.findById(superapp+"/"+email).orElseThrow(
                () -> new UserNotFoundException("could not find user to login by id: "
                        + superapp +"/"+email)).getRole();
        if (userRole != UserRole.ADMIN)
            throw new SuperappObjectUnauthorizedException("User role is forbidden");
        this.objectCrud.deleteAll();
    }
}
