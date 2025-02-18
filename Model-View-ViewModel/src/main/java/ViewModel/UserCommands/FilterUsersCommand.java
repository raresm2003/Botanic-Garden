package ViewModel.UserCommands;

import Model.Repository.UserRepository;
import Model.User;
import Model.UserType;
import ViewModel.ICommand;
import ViewModel.UserViewModel;

import javax.swing.*;
import java.util.List;

public class FilterUsersCommand implements ICommand {
    private final UserViewModel userViewModel;

    public FilterUsersCommand(UserViewModel userViewModel) {
        this.userViewModel = userViewModel;
    }

    @Override
    public void execute() {
        String nameFilter = userViewModel.getNameFilter().trim();
        UserType typeFilter = userViewModel.getTypeFilter();

        UserRepository userRepository = new UserRepository();
        List<User> filteredUsers = userRepository.filterUsers(nameFilter, typeFilter);
        if (!filteredUsers.isEmpty()) {
            userViewModel.usersTable.setRowCount(0);
            for (User user : filteredUsers) {
                userViewModel.usersTable.addRow(new Object[]{user.getUserID(), user.getName(), user.getType().toString()});
            }
        } else {
            JOptionPane.showMessageDialog(null, "No users found matching the criteria!", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}