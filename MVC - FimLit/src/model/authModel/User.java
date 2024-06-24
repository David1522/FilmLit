package model.authModel;

public class User {
    private String username;
    private String email;
    private String password;
    private String type;

    public User(String username, String email, String password, String type) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return  "Username: " + username +
                ", Email: " + email +
                ", Password: " + password + ".\n";
    }
}
