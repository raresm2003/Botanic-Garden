package Services;

import Domain.User.*;
import Repositories.UserRepository;

import java.util.List;

public class UserService implements IUserService {
    private final IUserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    @Override
    public boolean addUser(String userInfo1, String userInfo2, String userInfo3, String userInfo4, String userInfo5) {
        return userRepository.addUser(new User.UserBuilder()
                .name(userInfo1)
                .password(userInfo2)
                .type(UserType.valueOf(userInfo3))
                .email(userInfo4)
                .phone(userInfo5)
                .build());
    }

    @Override
    public boolean deleteUser(String message) {
        return userRepository.deleteUser(Integer.parseInt(message));
    }

    @Override
    public boolean updateUser(String userInfo0, String userInfo1, String userInfo2, String userInfo3, String userInfo4, String userInfo5) {
        if(userInfo2.isEmpty()){
            userInfo2 = userRepository.searchUserByID(Integer.parseInt(userInfo0)).getPassword();
        }
        User user = new User.UserBuilder()
                .name(userInfo1)
                .password(userInfo2)
                .type(UserType.valueOf(userInfo3))
                .email(userInfo4)
                .phone(userInfo5)
                .build();
        return userRepository.updateUser(Integer.parseInt(userInfo0), user);
    }

    @Override
    public String getUserList() {
        List<User> userList = userRepository.getUserList();
        StringBuilder attributesString = new StringBuilder();
        String email, phone;

        for (User user : userList) {
            email = (user.getEmail() == null) ? "-" : user.getEmail();
            phone = (user.getPhone() == null) ? "-" : user.getPhone();

            attributesString.append(user.getUserID()).append("#")
                    .append(user.getName()).append("#")
                    .append(user.getType()).append("#")
                    .append(email).append("#")
                    .append(phone).append("#");
        }

        return attributesString.toString();
    }

    @Override
    public String searchUserByID(String message) {
        User selectedUser = userRepository.searchUserByID(Integer.parseInt(message));
        return selectedUser.getName() + "#" + selectedUser.getPassword() + "#" + selectedUser.getType().toString() + "#" + selectedUser.getEmail() + "#" + selectedUser.getPhone();
    }

    @Override
    public String searchUserByName(String message) {
        User selectedUser = userRepository.getUserByName(message);
        return selectedUser.getName() + "#" + selectedUser.getPassword() + "#" + selectedUser.getType().toString() + "#" + selectedUser.getEmail() + "#" + selectedUser.getPhone();
    }

    @Override
    public String filterUsers(String userInfo1, String userInfo2) {
        UserType typeFilter = userInfo2.equals("All Types") ? null : UserType.valueOf(userInfo2);
        List<User> filteredUserList = userRepository.filterUsers(userInfo1, typeFilter);
        StringBuilder attributesString = new StringBuilder();
        String email, phone;

        for (User user : filteredUserList) {
            email = (user.getEmail() == null) ? "-" : user.getEmail();
            phone = (user.getPhone() == null) ? "-" : user.getPhone();

            attributesString.append(user.getUserID()).append("#")
                    .append(user.getName()).append("#")
                    .append(user.getType()).append("#")
                    .append(user.getEmail()).append("#")
                    .append(user.getPhone()).append("#");
        }

        return attributesString.toString();
    }

    @Override
    public String getUserPassword(String message){
        return userRepository.getUserByName(message).getPassword();
    }
}
