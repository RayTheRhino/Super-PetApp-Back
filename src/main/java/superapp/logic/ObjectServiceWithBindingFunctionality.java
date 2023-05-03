package superapp.logic;

import superapp.bounderies.ObjectBoundary;
import superapp.bounderies.ObjectId;

import java.util.List;

public interface ObjectServiceWithBindingFunctionality extends ObjectsService{

    public void ObjectBindingChild(ObjectId parentId, ObjectId childId);

    public List<ObjectBoundary> getChildren(String superapp, String internalObjectId);

    public List<ObjectBoundary> getParents(String superapp, String internalObjectId);


}
