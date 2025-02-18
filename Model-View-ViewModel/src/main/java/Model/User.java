package Model;

public class User {

    private int userID;
    private String name;
    private String password;
    private UserType type;

    public User(int userID, String name, String password, UserType type) {
        this.userID = userID;
        this.name = name;
        this.password = password;
        this.type = type;
    }

    public User(String name, String password, UserType type) {
        this.name = name;
        this.password = password;
        this.type = type;
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public UserType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
