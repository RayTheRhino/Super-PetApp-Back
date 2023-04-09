package superapp.bounderies;

public class NewUserBoundary{
    private String userId;
    private String superapp;
    private String email;
    private String role;
    private String username;
    private String avatar;

    public NewUserBoundary(String userId, String superapp, String email, String role, String username, String avatar) {
        this.userId = userId;
        this.superapp = superapp;
        this.email = email;
        this.role = role;
        this.username = username;
        this.avatar = avatar;
    }

    public NewUserBoundary() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSuperapp() {
        return superapp;
    }

    public void setSuperapp(String superapp) {
        this.superapp = superapp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}