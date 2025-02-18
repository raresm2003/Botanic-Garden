package Presenter;

import Model.Plant;
import Model.Repository.PlantRepository;
import Model.Repository.UserRepository;
import Model.User;
import Model.UserType;
import Model.Zone;
import View.IAdministratorGUI;

import java.util.List;

public class AdministratorPresenter {
    private final IAdministratorGUI iAdministratorGUI;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;

    public AdministratorPresenter(IAdministratorGUI iAdministratorGUI) {
        this.iAdministratorGUI = iAdministratorGUI;
        this.plantRepository = new PlantRepository();
        this.userRepository = new UserRepository();
    }

    public void bind(IAdministratorGUI administratorGUI) {}

    public void populatePlantList() {
        try {
            List<Plant> plantList = plantRepository.getPlantList();
            if (plantList != null) {
                iAdministratorGUI.ResetDgvPlantsList();
                for (Plant plant : plantList) {
                    iAdministratorGUI.AddRowDgvPlantsList(new Object[]{plant.getPlantID(), plant.getName(), plant.getSpecies(), plant.isCarnivore(), plant.getZone()});
                }
            } else {
                iAdministratorGUI.SetMessage("Empty", "The list of plants is empty!");
            }
        } catch (Exception exception) {
            iAdministratorGUI.SetMessage("Plants list - exception", exception.toString());
        }
    }

    public void addPlant(String name, String species, boolean carnivore, String zone) {
        try {
            Plant newPlant = new Plant(name, species, carnivore, Zone.valueOf(zone));
            boolean success = plantRepository.addPlant(newPlant);
            if (success) {
                iAdministratorGUI.SetMessage("Success", "Plant added successfully.");
                populatePlantList(); // Refresh plant list after adding
            } else {
                iAdministratorGUI.SetMessage("Error", "Failed to add plant.");
            }
        } catch (IllegalArgumentException ex) {
            iAdministratorGUI.SetMessage("Error", "Invalid zone provided.");
        }
    }

    public void updatePlant(int plantID, String name, String species, boolean carnivore, String zone) {
        try {
            Plant updatedPlant = new Plant(plantID, name, species, carnivore, Zone.valueOf(zone));
            boolean success = plantRepository.updatePlant(plantID, updatedPlant);
            if (success) {
                iAdministratorGUI.SetMessage("Success", "Plant updated successfully.");
                populatePlantList(); // Refresh plant list after updating
            } else {
                iAdministratorGUI.SetMessage("Error", "Failed to update plant.");
            }
        } catch (IllegalArgumentException ex) {
            iAdministratorGUI.SetMessage("Error", "Invalid zone provided.");
        }
    }

    public void deletePlant(int plantID) {
        try {
            boolean success = plantRepository.deletePlant(plantID);
            if (success) {
                iAdministratorGUI.SetMessage("Success", "Plant deleted successfully.");
                populatePlantList(); // Refresh plant list after deletion
            } else {
                iAdministratorGUI.SetMessage("Error", "Failed to delete plant.");
            }
        } catch (Exception exception) {
            iAdministratorGUI.SetMessage("Error", "An error occurred while deleting plant.");
        }
    }

    public void applyFiltersPlant(String nameFilter, String speciesFilter, boolean carnivoreFilter, Zone zoneFilter) {
        try {
            List<Plant> filteredList = plantRepository.filterPlants(nameFilter, speciesFilter, carnivoreFilter, zoneFilter);
            if (filteredList != null && !filteredList.isEmpty()) {
                iAdministratorGUI.ResetDgvPlantsList();
                for (Plant plant : filteredList) {
                    iAdministratorGUI.AddRowDgvPlantsList(new Object[]{plant.getPlantID(), plant.getName(), plant.getSpecies(), plant.isCarnivore(), plant.getZone()});
                }
            } else {
                iAdministratorGUI.SetMessage("Empty", "No plants match the selected filters!");
            }
        } catch (Exception exception) {
            iAdministratorGUI.SetMessage("Filtering error", exception.toString());
        }
    }

    public Plant getSelectedPlant(int plantID) {
        return plantRepository.searchPlantByID(plantID);
    }

    public void populateUserList() {
        try {
            List<User> userList = userRepository.getUserList();
            if (userList != null) {
                iAdministratorGUI.ResetDgvUsersList();
                for (User user : userList) {
                    iAdministratorGUI.AddRowDgvUsersList(new Object[]{user.getUserID(), user.getName(), user.getType()});
                }
            } else {
                iAdministratorGUI.SetMessage("Empty", "The list of users is empty!");
            }
        } catch (Exception exception) {
            iAdministratorGUI.SetMessage("Users list - exception", exception.toString());
        }
    }

    public void addUser(String name, String password, UserType type) {
        try {
            User newUser = new User(name, password, type);
            boolean success = userRepository.addUser(newUser);
            if (success) {
                iAdministratorGUI.SetMessage("Success", "User added successfully.");
                populateUserList(); // Refresh user list after adding
            } else {
                iAdministratorGUI.SetMessage("Error", "Failed to add user.");
            }
        } catch (IllegalArgumentException ex) {
            iAdministratorGUI.SetMessage("Error", "Invalid user type provided.");
        }
    }

    public void updateUser(int userID, String name, String password, UserType type) {
        try {
            User updatedUser = new User(userID, name, password, type);
            boolean success = userRepository.updateUser(userID, updatedUser);
            if (success) {
                iAdministratorGUI.SetMessage("Success", "User updated successfully.");
                populateUserList(); // Refresh user list after updating
            } else {
                iAdministratorGUI.SetMessage("Error", "Failed to update user.");
            }
        } catch (IllegalArgumentException ex) {
            iAdministratorGUI.SetMessage("Error", "Invalid user type provided.");
        }
    }

    public void deleteUser(int userID) {
        try {
            boolean success = userRepository.deleteUser(userID);
            if (success) {
                iAdministratorGUI.SetMessage("Success", "User deleted successfully.");
                populateUserList(); // Refresh user list after deletion
            } else {
                iAdministratorGUI.SetMessage("Error", "Failed to delete user.");
            }
        } catch (Exception exception) {
            iAdministratorGUI.SetMessage("Error", "An error occurred while deleting user.");
        }
    }

    public void applyFiltersUser(String nameFilter, UserType typeFilter) {
        try {
            List<User> filteredList = userRepository.filterUsers(nameFilter, typeFilter);
            if (filteredList != null && !filteredList.isEmpty()) {
                iAdministratorGUI.ResetDgvUsersList();
                for (User user : filteredList) {
                    iAdministratorGUI.AddRowDgvUsersList(new Object[]{user.getUserID(), user.getName(), user.getType()});
                }
            } else {
                iAdministratorGUI.SetMessage("Empty", "No users match the selected filters!");
            }
        } catch (Exception exception) {
            iAdministratorGUI.SetMessage("Filtering error", exception.toString());
        }
    }

    public User getSelectedUser(int userID) {
        return userRepository.searchUserByID(userID);
    }
}