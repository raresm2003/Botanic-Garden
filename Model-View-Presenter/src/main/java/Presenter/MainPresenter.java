package Presenter;

import Model.Plant;
import Model.Repository.PlantRepository;
import Model.Repository.UserRepository;
import Model.User;
import Model.UserType;
import Model.Zone;
import View.IMainGUI;

import java.util.List;

public class MainPresenter {
    private final IMainGUI iMainGUI;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;

    public MainPresenter(IMainGUI iMainGUI) {
        this.iMainGUI = iMainGUI;
        this.plantRepository = new PlantRepository();
        this.userRepository = new UserRepository();
    }

    public void bind(IMainGUI mainGUI) {}

    public void populatePlantList() {
        try {
            List<Plant> plantList = plantRepository.getPlantList();
            if (plantList != null) {
                iMainGUI.ResetDgvPlantsList();
                for (Plant plant : plantList) {
                    iMainGUI.AddRowDgvPlantsList(new Object[]{plant.getPlantID(), plant.getName(), plant.getSpecies(), plant.isCarnivore(), plant.getZone()});
                }
            } else {
                iMainGUI.SetMessage("Empty", "The list of plants is empty!");
            }
        } catch (Exception exception) {
            iMainGUI.SetMessage("Plants list - exception", exception.toString());
        }
    }

    public void applyFilters(String nameFilter, String speciesFilter, boolean carnivoreFilter, Zone zoneFilter) {
        try {
            List<Plant> filteredList = plantRepository.filterPlants(nameFilter, speciesFilter, carnivoreFilter, zoneFilter);
            if (filteredList != null && !filteredList.isEmpty()) {
                iMainGUI.ResetDgvPlantsList();
                for (Plant plant : filteredList) {
                    iMainGUI.AddRowDgvPlantsList(new Object[]{plant.getPlantID(), plant.getName(), plant.getSpecies(), plant.isCarnivore(), plant.getZone()});
                }
            } else {
                iMainGUI.SetMessage("Empty", "No plants match the selected filters!");
            }
        } catch (Exception exception) {
            iMainGUI.SetMessage("Filtering error", exception.toString());
        }
    }

    public void authenticateUser(String username, String password) {
        User user = userRepository.getUserByName(username);

        if (user != null && user.getPassword().equals(password)) {
            UserType userType = user.getType();

            if (userType == UserType.EMPLOYEE) {
                iMainGUI.openEmployeeGUI();
            } else if (userType == UserType.ADMINISTRATOR) {
                iMainGUI.openAdministratorGUI();
            }
        } else {
            iMainGUI.showMessage("Login Error", "Invalid username or password.");
        }
    }

}