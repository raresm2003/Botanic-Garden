package ViewModel.UserCommands;

import Model.Plant;
import Model.Repository.UserRepository;
import Model.User;
import ViewModel.ICommand;
import ViewModel.UserViewModel;

import javax.swing.*;
import java.util.List;

public class PopulateUserListCommand implements ICommand {
    private final UserViewModel userViewModel;

    public PopulateUserListCommand(UserViewModel userViewModel) {
        this.userViewModel = userViewModel;
    }

    @Override
    public void execute() {
        UserRepository userRepository = new UserRepository();
        List<User> allUsers = userRepository.getUserList();

        if (!allUsers.isEmpty()) {
            userViewModel.usersTable.setRowCount(0);
            for (User user : allUsers) {
                userViewModel.usersTable.addRow(new Object[]{user.getUserID(), user.getName(), user.getType().toString()});
            }
        } else {
            JOptionPane.showMessageDialog(null, "No users found in the database!", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}