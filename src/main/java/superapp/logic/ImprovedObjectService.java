package superapp.logic;

import superapp.bounderies.ObjectBoundary;

import java.util.List;

public interface ImprovedObjectService extends ObjectsService{

    public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary update,
                                   String userSuperapp, String email );

    public List<ObjectBoundary> getObjectsByType(String type, String superapp, String  email,
                                                 int size, int page);

    public List<ObjectBoundary> getObjectsByAlias(String alias, String superapp, String  email,
                                                  int size, int page);

//    public List<ObjectBoundary> getObjectsByLocation(double lat, double lng, double distance,
//                                                     String superapp, String email,String distanceUnits,
//                                                     int size, int page);
    public List<ObjectBoundary> getAllObjects(String superapp, String email, int size, int page);

    public ObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId,
                                            String userSuperapp, String email);

    public void deleteAllObjects(String superapp, String email);
}
