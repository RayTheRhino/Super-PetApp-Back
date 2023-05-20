package superapp.logic;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
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
public class ObjectsServiceDB implements ImprovedObjectService {
    private ObjectCrud objectCrud;
    private UserCrud userCrud;
    private String superapp;

    ////SETUP////
    @Value("${spring.application.name}")
    public void setSuperapp(String superapp){this.superapp = superapp;}
    @Autowired
    public void setObjectCrud(ObjectCrud objectCrud) {this.objectCrud = objectCrud;}
    @Autowired
    public void setUserCrud(UserCrud userCrud) {this.userCrud = userCrud;}
    ////=====////

    @Override
    @Transactional
    public ObjectBoundary CreateObject(ObjectBoundary object) {
        checkInputForNewObject(object);
        UserRole userRole = this.userCrud.findById(object.getCreatedBy().getUserId().getSuperapp()+"/"+
                object.getCreatedBy().getUserId().getEmail()).orElseThrow(() -> new UserNotFoundException("could not find user by id")).getRole();

        if (userRole != UserRole.SUPERAPP_USER)
            throw new SuperappObjectUnauthorizedException("User Role is not allowed");
        object.setObjectId(new ObjectId(this.superapp,UUID.randomUUID().toString()));
        object.setCreationTimestamp(new Date());
        SuperappObjectsEntity entity = this.toEntity(object);
        objectCrud.save(entity);
        object = toBoundary(entity);
        return object;
    }
    ////=====////
    ////OLD FUNCTIONS////
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
    public List<ObjectBoundary> getAllObjects() { throw new SuperappObjectGoneException("Unavailable method");  }

    @Override
    @Transactional()
    @Deprecated
    public void deleteAllObjects() { throw new SuperappObjectGoneException("Unavailable method");   }


    @Override
    @Deprecated
    public void ObjectBindingChild(ObjectId parentId, ObjectId childId) {
        throw new SuperappObjectGoneException("Unavailable method");
    }

    @Override
    @Deprecated
    public List<ObjectBoundary> getChildren(String superapp, String internalObjectId) {
        throw new SuperappObjectGoneException("Unavailable method");
    }

    @Override
    @Deprecated
    public List<ObjectBoundary> getParents(String superapp, String internalObjectId) {
        throw new SuperappObjectGoneException("Unavailable method");
    }
    ////=====////
    ////TO BOUNDARY, ENTITY AND UTILS////
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
        double lat = 0.0, lng = 0.0;
        if (boundary.getLocation() != null) {
            if (boundary.getLocation().getLat() != null)
                lat = boundary.getLocation().getLat();
            if (boundary.getLocation().getLng() != null)
                lng = boundary.getLocation().getLng();
        }
        entity.setLocation(lng,lat);
        entity.setByEmail(boundary.getCreatedBy().getUserId().getEmail());

        entity.setBySuperapp(boundary.getCreatedBy().getUserId().getSuperapp());

        if (boundary.getObjectDetails() != null)
            entity.setObjectDetails(boundary.getObjectDetails());
        // else, do nothing , the constructor already made a new TreeMap 
        return entity;

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
            throw new SuperappObjectBadRequestException("Need to input the user details correctly");

    }
    private Metrics toEnumFromString (String value) {
        if (value != null) {
            for (Metrics role : Metrics.values())
                if (value.equals(role.name()))
                    return Metrics.valueOf(value);
        }
        return null;
    }
    ////=====////
    //// CHILDREN AND PARENTS BINDING AND GETTERS ////
    @Override
    @Transactional
    public void ObjectBindingChild(ObjectId parentId, ObjectId childId, String userSuperapp, String email) {
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
        if (parentObject.getChildren().contains(childObject))
            throw new SuperappObjectBadRequestException("Object child already exists in children list");
        if (parentObject.getParents().contains(childObject))
            throw new SuperappObjectBadRequestException("New Child object is already parent object's parent");
        parentObject.addChild(childObject);
        childObject.addParent(parentObject);
        this.objectCrud.save(childObject);
        this.objectCrud.save(parentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> getChildren(String superapp, String internalObjectId, String userSuperapp, String email,
                                            int size, int page) {
        if (size<=0 || page <0)
            throw new SuperappObjectBadRequestException("Page and size are incorrect, size need to be more then 0 and page 0 or above");
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
            List<SuperappObjectsEntity> rv = this.objectCrud.findAllByParentsContains(parentObject, PageRequest.of(page, size, Direction.DESC, "creationTimestamp","objectId"));
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
            List<SuperappObjectsEntity> rv = this.objectCrud.findAllByParentsContainsAndActive(parentObject, PageRequest.of(page, size, Direction.DESC, "creationTimestamp","objectId"),true);
            return rv.stream()
                    .map(this::toBoundary)
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> getParents(String superapp, String internalObjectId, String userSuperapp, String email,
                                           int size, int page) {
        if (size<=0 || page <0)
            throw new SuperappObjectBadRequestException("Page and size are incorrect, size need to be more then 0 and page 0 or above");
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
            List<SuperappObjectsEntity> rv = this.objectCrud.findAllByChildrenContains(childObject, PageRequest.of(page, size, Direction.DESC, "creationTimestamp","objectId"));
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
            List<SuperappObjectsEntity> rv = this.objectCrud.findAllByChildrenContainsAndActive(childObject, PageRequest.of(page, size, Direction.DESC, "creationTimestamp","objectId"),true);

            return rv.stream()
                    .map(this::toBoundary)
                    .toList();
        }
    }


    @Override
    @Transactional
    public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary update,
                                       String userSuperapp, String email ) {
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
            existing.setLocation(update.getLocation().getLng(),update.getLocation().getLat());
        }
        if (!update.getObjectDetails().isEmpty())
            existing.setObjectDetails(update.getObjectDetails());

        existing = objectCrud.save(existing);
        return this.toBoundary(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> getObjectsByType(String type, String superapp, String email, int size, int page) {
        if (size<=0 || page <0)
            throw new SuperappObjectBadRequestException("Page and size are incorrect, size need to be more then 0 and page 0 or above");
        UserRole userRole = this.userCrud.findById(superapp+"/"+email).orElseThrow(
                () -> new UserNotFoundException("could not find user to login by id: "
                        + superapp+"/"+email)).getRole();
        List<SuperappObjectsEntity> list;
        if (userRole == UserRole.SUPERAPP_USER)
            list = this.objectCrud.findAllByType(type, PageRequest.of(page, size, Direction.DESC, "creationTimestamp","objectId"));
        else if (userRole == UserRole.MINIAPP_USER)
            list = this.objectCrud.findAllByTypeAndActive(type, PageRequest.of(page, size, Direction.DESC, "creationTimestamp","objectId"), true);
        else
            throw new SuperappObjectUnauthorizedException("User role is forbidden");
        return list
                .stream()
                .map(this::toBoundary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> getObjectsByAlias(String alias, String superapp, String email, int size, int page) {
        if (size<=0 || page <0)
            throw new SuperappObjectBadRequestException("Page and size are incorrect, size need to be more then 0 and page 0 or above");
        UserRole userRole = this.userCrud.findById(superapp+"/"+email).orElseThrow(
                () -> new UserNotFoundException("could not find user to login by id: "
                        + superapp+"/"+email)).getRole();
        List<SuperappObjectsEntity> list;
        if (userRole == UserRole.SUPERAPP_USER)
            list = this.objectCrud.findAllByAlias(alias, PageRequest.of(page, size, Direction.DESC, "creationTimestamp","objectId"));
        else if (userRole == UserRole.MINIAPP_USER)
            list = this.objectCrud.findAllByAliasAndActive(alias, PageRequest.of(page, size, Direction.DESC, "creationTimestamp","objectId"),true);
        else
            throw new SuperappObjectUnauthorizedException("User role is forbidden");

        return list
                .stream()
                .map(this::toBoundary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> getObjectsByLocation(double lat, double lng, double distance, String superapp,
                                                     String email, String distanceUnits, int size, int page) {
        if (size<=0 || page <0)
            throw new SuperappObjectBadRequestException("Page and size are incorrect, size need to be more then 0 and page 0 or above");
        UserRole userRole = this.userCrud.findById(superapp+"/"+email).orElseThrow(
                () -> new UserNotFoundException("could not find user to login by id: "
                        + superapp+"/"+email)).getRole();
        Metrics metricsType = this.toEnumFromString(distanceUnits);
        if (metricsType == null)
            throw new SuperappObjectBadRequestException("Wrong input for Metrics distance");
        Distance maxDistance = new Distance(distance,metricsType);
        List<SuperappObjectsEntity> list;
        if (userRole == UserRole.SUPERAPP_USER)
            list = this.objectCrud.findAllByLocationNear(new Point(lng,lat),maxDistance,
                    PageRequest.of(page, size, Direction.DESC, "creationTimestamp","objectId"));
        else if (userRole == UserRole.MINIAPP_USER)
            list = this.objectCrud.findAllByLocationNearAndActive(new Point(lng,lat),maxDistance,
                    PageRequest.of(page, size, Direction.DESC, "creationTimestamp","objectId"),true);
        else
            throw new SuperappObjectUnauthorizedException("User role is forbidden");
        return list
                .stream()
                .map(this::toBoundary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectBoundary> getAllObjects(String superapp, String email, int size, int page) {
        if (size<=0 || page <0)
            throw new SuperappObjectBadRequestException("Page and size are incorrect, size need to be more then 0 and page 0 or above");
        UserRole userRole = this.userCrud.findById(superapp+"/"+email).orElseThrow(
                () -> new UserNotFoundException("could not find user to login by id: "
                        + superapp+"/"+email)).getRole();
        List<SuperappObjectsEntity> list;
        if (userRole == UserRole.SUPERAPP_USER)
            list = this.objectCrud.findAll(PageRequest.of(page, size, Direction.DESC, "creationTimestamp","objectId")).toList();
        else if(userRole == UserRole.MINIAPP_USER)
            list = this.objectCrud.findAllByActive(true,
                    PageRequest.of(page, size, Direction.DESC, "creationTimestamp","objectId"));
        else
            throw new SuperappObjectUnauthorizedException("User role is forbidden");

        return list
                .stream()
                .map(this::toBoundary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId,
                                            String userSuperapp, String email) {
        UserRole userRole = this.userCrud.findById(userSuperapp + "/" + email).orElseThrow(
                () -> new UserNotFoundException("could not find user to login by id: "
                        + userSuperapp + "/" + email)).getRole();
        if (userRole == UserRole.SUPERAPP_USER)
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
