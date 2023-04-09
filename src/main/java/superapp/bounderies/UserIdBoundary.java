package superapp.bounderies;

public class UserIdBoundary{
    private String email;
    private String superapp;

    public UserIdBoundary(String email, String superapp) {
        this.email = email;
        this.superapp = superapp;
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
}