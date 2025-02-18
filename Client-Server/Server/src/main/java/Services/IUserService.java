package Services;

public interface IUserService {

    boolean addUser(String userInfo1, String userInfo2, String userInfo3, String userInfo4, String userInfo5);

    boolean deleteUser(String message);

    boolean updateUser(String userInfo0, String userInfo1, String userInfo2, String userInfo3, String userInfo4, String userInfo5);

    String getUserList();

    String searchUserByID(String message);

    String searchUserByName(String message);

    String filterUsers(String userInfo1, String userInfo2);

    String getUserPassword(String message);
}
