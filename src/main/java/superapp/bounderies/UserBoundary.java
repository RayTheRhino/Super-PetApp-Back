package superapp.bounderies;

public class UserBoundary {
    private UserIdBoundary userId;
    private String role;
    private String userName;
    private String avatar;

    public UserBoundary(NewUserBoundary newUserBoundary, String superapp) {
        this.userId = new UserIdBoundary(newUserBoundary.getEmail() , superapp);
        this.role = newUserBoundary.getRole();
        this.userName = newUserBoundary.getUsername();
        this.avatar = newUserBoundary.getAvatar();
    }
    public UserBoundary(UserIdBoundary userId, String role, String username, String avatar) {
        this.userId = userId;
        this.role = role;
        this.userName = username;
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
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}