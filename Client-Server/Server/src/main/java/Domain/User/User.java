package Domain.User;

import Domain.Subject;

public class User extends Subject {

    private int userID;
    private String name;
    private String password;
    private UserType type;
    private String email;
    private String phone;

    private User(UserBuilder builder) {
        this.userID = builder.userID;
        this.name = builder.name;
        this.password = builder.password;
        this.type = builder.type;
        this.email = builder.email;
        this.phone = builder.phone;
        notifyObservers(this);
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
        notifyObservers(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyObservers(this);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyObservers(this);
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
        notifyObservers(this);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyObservers(this);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyObservers(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", type=" + type +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public static class UserBuilder {
        private int userID;
        private String name;
        private String password;
        private UserType type;
        private String email;
        private String phone;

        public UserBuilder() {}

        public UserBuilder userID(int userID) {
            this.userID = userID;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder type(UserType type) {
            this.type = type;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
