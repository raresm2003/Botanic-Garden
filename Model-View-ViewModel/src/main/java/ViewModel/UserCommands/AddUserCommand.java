package ViewModel.UserCommands;

import Model.Repository.UserRepository;
import Model.User;
import Model.UserType;
import ViewModel.ICommand;
import ViewModel.UserViewModel;

import javax.swing.*;

public class AddUserCommand implements ICommand {
    private final UserViewModel userViewModel;

    public AddUserCommand(UserViewModel userViewModel) {
        this.userViewModel = userViewModel;
    }

    @Override
    public void execute() {
        UserRepository userRepository = new UserRepository();
        String userName = userViewModel.getUserName();
        String password = userViewModel.getPassword();
        UserType type = userViewModel.getType();

        if (!userName.isEmpty() && !password.isEmpty() && type != null) {
            try {
                User user = new User(userName, password, type);
                if (userRepository.addUser(user)) {
                    JOptionPane.showMessageDialog(null, "User added successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add user to the database!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input for user data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please fill in all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}