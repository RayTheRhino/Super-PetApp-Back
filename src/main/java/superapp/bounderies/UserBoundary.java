package superapp.bounderies;

public class UserBoundary {
    private UserIdBoundary userId;
    private String role;
    private String username;
    private String avatar;

    public UserBoundary(NewUserBoundary newUserBoundary) {
        this.userId = new UserIdBoundary(newUserBoundary.getEmail());
        this.role = newUserBoundary.getRole();
        this.username = newUserBoundary.getUsername();
        this.avatar = newUserBoundary.getAvatar();
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "UserBoundary{" +
                "userId= {"+(userId != null ? userId.toString() : "null" )+ "}'"+
                ", role='" + (role != null ? role : "null") + '\'' +
                ", userName='" + (username != null ? username : "null") + '\'' +
                ", avatar='" + (avatar != null ? avatar : "null") + '\'' +
                '}';
    }
}