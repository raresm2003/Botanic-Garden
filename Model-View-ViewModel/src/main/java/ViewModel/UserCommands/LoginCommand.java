package ViewModel.UserCommands;

import Model.Repository.UserRepository;
import Model.User;
import Model.UserType;
import View.AdministratorGUI;
import View.EmployeeGUI;
import ViewModel.ICommand;
import ViewModel.UserViewModel;

import javax.swing.*;

public class LoginCommand implements ICommand {
    private final UserViewModel userViewModel;

    public LoginCommand(UserViewModel userViewModel) {
        this.userViewModel = userViewModel;
    }

    @Override
    public void execute() {
        UserRepository userRepository = new UserRepository();
        String userName = userViewModel.getUserName();
        String password = userViewModel.getPassword();
        User user = userRepository.getUserByName(userName);

        if (user != null && user.getPassword().equals(password)) {
            UserType userType = user.getType();

            if (userType == UserType.EMPLOYEE) {
                SwingUtilities.invokeLater(() -> {
                    EmployeeGUI employeeGUI = new EmployeeGUI();
                    employeeGUI.setVisible(true);
                });
            } else if (userType == UserType.ADMINISTRATOR) {
                SwingUtilities.invokeLater(() -> {
                    AdministratorGUI administratorGUI = new AdministratorGUI(false);
                    administratorGUI.setVisible(true);
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
