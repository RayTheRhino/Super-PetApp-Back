package superapp.bounderies;

public class UserIdBoundary{
    private String superapp;
    private String email;

    public UserIdBoundary(String email, String superapp) {
        this.superapp = superapp;
        this.email = email;
    }

    public UserIdBoundary(){
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

    @Override
    public String toString() {
        return "{superapp= '" + superapp + '\'' +
                ", email= '" + email + '\'' +
                '}';
    }
}