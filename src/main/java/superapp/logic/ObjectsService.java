package superapp.logic;

import java.util.List;

import superapp.bounderies.ObjectBoundary;

public interface ObjectsService {


	public ObjectBoundary CreateObject(ObjectBoundary object);
	@Deprecated
	public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary update);
	@Deprecated
	public ObjectBoundary getSpecificObject(String objectSuperApp, String internalObjectId);
	@Deprecated
	public List<ObjectBoundary> getAllObjects();
	@Deprecated
	public void deleteAllObjects();

}
