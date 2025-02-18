package ViewModel.PlantCommands;

import Model.Plant;
import Model.Repository.PlantRepository;
import ViewModel.ICommand;
import ViewModel.PlantViewModel;

import javax.swing.*;
import java.util.List;

public class PopulatePlantListCommand implements ICommand {
    private final PlantViewModel plantViewModel;

    public PopulatePlantListCommand(PlantViewModel plantViewModel) {
        this.plantViewModel = plantViewModel;
    }

    @Override
    public void execute() {
        PlantRepository plantRepository = new PlantRepository();
        List<Plant> allPlants = plantRepository.getPlantList();
        if (!allPlants.isEmpty()) {
            plantViewModel.plantsTable.setRowCount(0);
            for (Plant plant : allPlants) {
                plantViewModel.plantsTable.addRow(new Object[]{plant.getPlantID(), plant.getName(), plant.getSpecies(), plant.isCarnivore(), plant.getZone()});
            }
        } else {
            JOptionPane.showMessageDialog(null, "No plants found in the database!", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}