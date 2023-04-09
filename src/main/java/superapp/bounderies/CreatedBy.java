package superapp.bounderies;

public class CreatedBy {
    private UserIdBoundary userId;

    public CreatedBy(UserIdBoundary userId) {
        this.userId = userId;
    }
    public CreatedBy() {

    }

    public UserIdBoundary getUserId() {
        return userId;
    }

    public void setUserId(UserIdBoundary userId) {
        this.userId = userId;
    }
}
