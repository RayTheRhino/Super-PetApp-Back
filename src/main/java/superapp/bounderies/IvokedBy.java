package superapp.bounderies;

public class IvokedBy {
    private ObjectId objectId;

    public IvokedBy(ObjectId objectId) {
        this.objectId = objectId;
    }

    public IvokedBy() {
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }
}
