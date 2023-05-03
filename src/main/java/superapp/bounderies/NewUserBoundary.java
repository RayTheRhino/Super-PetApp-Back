package superapp.bounderies;

public class NewUserBoundary{

    private String email;
    private String role;
    private String userName;
    private String avatar;

    public NewUserBoundary( String email, String role, String userName, String avatar) {
        this.email = email;
        this.role = role;
        this.userName = userName;
        this.avatar = avatar;
    }

    public NewUserBoundary() {
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

    public String getUserName() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "NewUserBoundary{" +
                "email='" + email + '\'' +
                ",\nrole='" + role + '\'' +
                ",\nuserName='" + userName + '\'' +
                ",\navatar='" + avatar + '\'' +
                '}';
    }
}