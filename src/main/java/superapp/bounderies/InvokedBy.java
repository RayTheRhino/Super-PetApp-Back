package superapp.bounderies;

public class InvokedBy {
    private UserIdBoundary userId;

    public InvokedBy(UserIdBoundary objectId) {
        this.userId = objectId;
    }

    public InvokedBy() {
    }

    public UserIdBoundary getUserId() {
        return userId;
    }

    public void setUserId(UserIdBoundary userId) {
        this.userId = userId;
    }
}
