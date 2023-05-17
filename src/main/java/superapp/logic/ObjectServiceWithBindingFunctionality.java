package superapp.logic;

import superapp.bounderies.ObjectBoundary;
import superapp.bounderies.ObjectId;

import java.util.List;

public interface ObjectServiceWithBindingFunctionality extends ImprovedObjectService{

    public void ObjectBindingChild(ObjectId parentId, ObjectId childId, String userSuperapp, String email);

    public List<ObjectBoundary> getChildren(String superapp, String internalObjectId, String userSuperapp, String email,
                                            int size, int page);

    public List<ObjectBoundary> getParents(String superapp, String internalObjectId, String userSuperapp, String email,
                                           int size, int page);


}
