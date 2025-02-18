package ViewModel.UserCommands;

import Model.Repository.UserRepository;
import Model.User;
import ViewModel.ICommand;
import ViewModel.UserViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UpdateUserCommand implements ICommand {
    private final UserViewModel userViewModel;

    public UpdateUserCommand(UserViewModel userViewModel) {
        this.userViewModel = userViewModel;
    }

    @Override
    public void execute() {
        UserRepository userRepository = new UserRepository();
        int selectedRowIndex = userViewModel.getSelectedRowIndex();
        DefaultTableModel model = userViewModel.getUsersTable();

        if (selectedRowIndex != -1) {
            int userID = (int) model.getValueAt(selectedRowIndex, 0);

            User updatedUser = new User(userID, userViewModel.getUserName(), userViewModel.getPassword(), userViewModel.getType());
            if (userRepository.updateUser(userID, updatedUser)) {
                JOptionPane.showMessageDialog(null, "User updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update user in the database!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a user to update!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}