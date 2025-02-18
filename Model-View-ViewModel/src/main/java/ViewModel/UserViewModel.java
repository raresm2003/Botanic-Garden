package ViewModel;

import Model.UserType;
import ViewModel.UserCommands.*;

import javax.swing.table.DefaultTableModel;

public class UserViewModel {
    public DefaultTableModel usersTable;
    private String userName;
    private String password;
    private UserType type;
    private int selectedRowIndex;

    // New fields for filters
    private String nameFilter;
    private UserType typeFilter;

    public final ICommand addUserCommand;
    public final ICommand deleteUserCommand;
    public final ICommand updateUserCommand;
    public final ICommand populateUserListCommand;
    public final ICommand filterUsersCommand;
    public final ICommand loginCommand;

    public UserViewModel() {
        this.userName = "";
        this.password = "";
        this.type = null;
        this.selectedRowIndex = -1;

        // Initialize filters
        this.nameFilter = "";
        this.typeFilter = null;

        this.usersTable = new DefaultTableModel();
        this.usersTable.addColumn("User ID");
        this.usersTable.addColumn("Name");
        this.usersTable.addColumn("Type");

        this.addUserCommand = new AddUserCommand(this);
        this.deleteUserCommand = new DeleteUserCommand(this);
        this.updateUserCommand = new UpdateUserCommand(this);
        this.filterUsersCommand = new FilterUsersCommand(this);
        this.populateUserListCommand = new PopulateUserListCommand(this);
        this.loginCommand = new LoginCommand(this);
    }

    public DefaultTableModel getUsersTable() {
        return usersTable;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    // Getters and setters for filters
    public String getNameFilter() {
        return nameFilter;
    }

    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }

    public UserType getTypeFilter() {
        return typeFilter;
    }

    public void setTypeFilter(UserType typeFilter) {
        this.typeFilter = typeFilter;
    }

    public int getSelectedRowIndex() {
        return selectedRowIndex;
    }

    public void setSelectedRowIndex(int selectedRowIndex) {
        this.selectedRowIndex = selectedRowIndex;
    }
}