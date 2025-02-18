package ViewModel.UserCommands;

import Model.Repository.UserRepository;
import ViewModel.ICommand;
import ViewModel.UserViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DeleteUserCommand implements ICommand {
    private final UserViewModel userViewModel;

    public DeleteUserCommand(UserViewModel userViewModel) {
        this.userViewModel = userViewModel;
    }

    @Override
    public void execute() {
        UserRepository userRepository = new UserRepository();
        int selectedRowIndex = userViewModel.getSelectedRowIndex();
        DefaultTableModel model = userViewModel.getUsersTable();

        if (selectedRowIndex != -1) {
            int userID = (int) model.getValueAt(selectedRowIndex, 0);
            int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?", "Delete User", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                if (userRepository.deleteUser(userID)) {
                    model.removeRow(selectedRowIndex);
                    JOptionPane.showMessageDialog(null, "User deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to user plant from the database!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a user to delete!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}