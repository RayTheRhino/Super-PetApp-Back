package superapp.logic;

import java.util.List;
import java.util.Optional;

import superapp.bounderies.ObjectBoundary;

public interface ObjectsService {

	
	public ObjectBoundary CreateObject(ObjectBoundary object);

	public ObjectBoundary updateObject(String objectSuperApp, String internalObjectId, ObjectBoundary update);

	public Optional<ObjectBoundary> getSpecificObject(String objectSuperApp, String initernalObjectId);

	public List<ObjectBoundary> getAllObjects();

	public void deleteAllObjects();

}
