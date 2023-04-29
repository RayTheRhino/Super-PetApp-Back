package superapp.bounderies;

public class UserBoundary {
    private UserIdBoundary userId;
    private String role;
    private String username;
    private String avatar;

    public UserBoundary(NewUserBoundary newUserBoundary, String superapp) {
        this.userId = new UserIdBoundary(newUserBoundary.getEmail() , superapp);
        role = newUserBoundary.getRole();
        username = newUserBoundary.getUsername();
        avatar = newUserBoundary.getAvatar();
    }
    public UserBoundary(UserIdBoundary userId, String role, String username, String avatar) {
        this.userId = userId;
        this.role = role;
        this.username = username;
        this.avatar = avatar;
    }

    public UserBoundary() {
    }

    public UserIdBoundary getUserId() {
        return userId;
    }

    public void setUserId(UserIdBoundary userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}