package Presenter;

import Model.Plant;
import Model.Repository.PlantRepository;
import Model.Zone;
import View.IEmployeeGUI;

import java.util.List;

public class EmployeePresenter {
    private final IEmployeeGUI iEmployeeGUI;
    private final PlantRepository plantRepository;

    public EmployeePresenter(IEmployeeGUI iEmployeeGUI) {
        this.iEmployeeGUI = iEmployeeGUI;
        this.plantRepository = new PlantRepository();
    }

    public void bind(IEmployeeGUI employeeGUI) {}

    public void populatePlantList() {
        try {
            List<Plant> plantList = plantRepository.getPlantList();
            if (plantList != null) {
                iEmployeeGUI.ResetDgvPlantsList();
                for (Plant plant : plantList) {
                    iEmployeeGUI.AddRowDgvPlantsList(new Object[]{plant.getPlantID(), plant.getName(), plant.getSpecies(), plant.isCarnivore(), plant.getZone()});
                }
            } else {
                iEmployeeGUI.SetMessage("Empty", "The list of plants is empty!");
            }
        } catch (Exception exception) {
            iEmployeeGUI.SetMessage("Plants list - exception", exception.toString());
        }
    }

    public void addPlant(String name, String species, boolean carnivore, String zone) {
        try {
            Plant newPlant = new Plant(name, species, carnivore, Zone.valueOf(zone));
            boolean success = plantRepository.addPlant(newPlant);
            if (success) {
                iEmployeeGUI.SetMessage("Success", "Plant added successfully.");
                populatePlantList(); // Refresh plant list after adding
            } else {
                iEmployeeGUI.SetMessage("Error", "Failed to add plant.");
            }
        } catch (IllegalArgumentException ex) {
            iEmployeeGUI.SetMessage("Error", "Invalid zone provided.");
        }
    }

    public void updatePlant(int plantID, String name, String species, boolean carnivore, String zone) {
        try {
            Plant updatedPlant = new Plant(plantID, name, species, carnivore, Zone.valueOf(zone));
            boolean success = plantRepository.updatePlant(plantID, updatedPlant);
            if (success) {
                iEmployeeGUI.SetMessage("Success", "Plant updated successfully.");
                populatePlantList(); // Refresh plant list after updating
            } else {
                iEmployeeGUI.SetMessage("Error", "Failed to update plant.");
            }
        } catch (IllegalArgumentException ex) {
            iEmployeeGUI.SetMessage("Error", "Invalid zone provided.");
        }
    }

    public void deletePlant(int plantID) {
        try {
            boolean success = plantRepository.deletePlant(plantID);
            if (success) {
                iEmployeeGUI.SetMessage("Success", "Plant deleted successfully.");
                populatePlantList(); // Refresh plant list after deletion
            } else {
                iEmployeeGUI.SetMessage("Error", "Failed to delete plant.");
            }
        } catch (Exception exception) {
            iEmployeeGUI.SetMessage("Error", "An error occurred while deleting plant.");
        }
    }

    public void applyFilters(String nameFilter, String speciesFilter, boolean carnivoreFilter, Zone zoneFilter) {
        try {
            List<Plant> filteredList = plantRepository.filterPlants(nameFilter, speciesFilter, carnivoreFilter, zoneFilter);
            if (filteredList != null && !filteredList.isEmpty()) {
                iEmployeeGUI.ResetDgvPlantsList();
                for (Plant plant : filteredList) {
                    iEmployeeGUI.AddRowDgvPlantsList(new Object[]{plant.getPlantID(), plant.getName(), plant.getSpecies(), plant.isCarnivore(), plant.getZone()});
                }
            } else {
                iEmployeeGUI.SetMessage("Empty", "No plants match the selected filters!");
            }
        } catch (Exception exception) {
            iEmployeeGUI.SetMessage("Filtering error", exception.toString());
        }
    }

    public Plant getSelectedPlant(int plantID) {
        return plantRepository.searchPlantByID(plantID);
    }
}