package Bounderies;

import java.util.Date;
import java.util.Map;

public class ObjectBoundary {
    private ObjectId objectId;
    private String type;
    private String alias;
    private boolean active;
    private Date creationTimestamp;
    private Location location;
    private CreatedBy createdBy;
    private Map<String, Object> objectDetails;


    public ObjectBoundary(ObjectId objectId, String type, String alias, boolean active, Date creationTimestamp, Location location, CreatedBy createdBy, Map<String, Object> objectDetails) {
        this.objectId = objectId;
        this.type = type;
        this.alias = alias;
        this.active = active;
        this.creationTimestamp = creationTimestamp;
        this.location = location;
        this.createdBy = createdBy;
        this.objectDetails = objectDetails;
    }

    public ObjectBoundary() {

    }


    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public Map<String, Object> getObjectDetails() {
        return objectDetails;
    }

    public void setObjectDetails(Map<String, Object> objectDetails) {
        this.objectDetails = objectDetails;
    }
}
