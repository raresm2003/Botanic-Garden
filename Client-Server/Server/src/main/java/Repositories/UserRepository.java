package Repositories;

import Domain.User.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository {
    private final Repository repository;

    public UserRepository() {
        this.repository = new Repository();
    }

    @Override
    public boolean addUser(User user) {
        String commandSQL = "INSERT INTO User (Name, Password, Type, Email, Phone) VALUES ('" +
                user.getName() + "','" +
                user.getPassword() + "','" +
                user.getType() + "','" +
                user.getEmail() + "','" +
                user.getPhone() + "')";
        return this.repository.executeSQL(commandSQL);
    }

    @Override
    public boolean deleteUser(int userID) {
        String commandSQL = "DELETE FROM User WHERE UserID = '" + userID + "'";
        return this.repository.executeSQL(commandSQL);
    }

    @Override
    public boolean updateUser(int userID, User user) {
        String commandSQL = "UPDATE User SET Name = '" +
                user.getName() + "', Password = '" +
                user.getPassword() + "', Type = '" +
                user.getType() + "', Email = '" +
                user.getEmail() + "', Phone = '" +
                user.getPhone() + "' WHERE UserID = '" + userID + "'";
        return this.repository.executeSQL(commandSQL);
    }

    @Override
    public List<User> getUserList() {
        List<User> list = new ArrayList<>();
        ResultSet resultSet = this.repository.getResultSet("SELECT * FROM User");
        try {
            while (resultSet.next()) {
                list.add(convertToUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public User searchUserByID(int userID) {
        ResultSet resultSet = this.repository.getResultSet("SELECT * FROM User WHERE UserID = '" + userID + "'");
        try {
            if (resultSet.next()) {
                return convertToUser(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> filterUsers(String nameFilter, UserType typeFilter) {
        List<User> filteredList = new ArrayList<>();
        String commandSQL = "SELECT * FROM User WHERE 1 = 1";

        if (nameFilter != null && !nameFilter.isEmpty()) {
            commandSQL += " AND Name LIKE '%" + nameFilter + "%'";
        }
        if (typeFilter != null) {
            commandSQL += " AND Type = '" + typeFilter.toString() + "'";
        }

        ResultSet resultSet = this.repository.getResultSet(commandSQL);
        try {
            while (resultSet.next()) {
                filteredList.add(convertToUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return filteredList;
    }

    @Override
    public User getUserByName(String name) {
        String commandSQL = "SELECT * FROM User WHERE Name = '" + name + "'";
        ResultSet resultSet = this.repository.getResultSet(commandSQL);
        try {
            if (resultSet.next()) {
                return convertToUser(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User convertToUser(ResultSet resultSet) throws SQLException {
        int userID = resultSet.getInt("UserID");
        String name = resultSet.getString("Name");
        String password = resultSet.getString("Password");
        UserType type = UserType.valueOf(resultSet.getString("Type"));
        String email = resultSet.getString("Email");
        String phone = resultSet.getString("Phone");
        return new User.UserBuilder()
                .userID(userID)
                .name(name)
                .password(password)
                .type(type)
                .email(email)
                .phone(phone)
                .build();
    }
}
