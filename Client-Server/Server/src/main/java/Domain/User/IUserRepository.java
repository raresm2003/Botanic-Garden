package Domain.User;

import java.util.List;

public interface IUserRepository {

    boolean addUser(User user);

    boolean deleteUser(int userID);

    boolean updateUser(int userID, User user);

    List<User> getUserList();

    User searchUserByID(int userID);

    List<User> filterUsers(String nameFilter, UserType typeFilter);

    User getUserByName(String name);
}
