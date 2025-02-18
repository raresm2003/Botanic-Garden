package ViewModel.PlantCommands;

import Model.Plant;
import Model.Repository.PlantRepository;
import Model.Zone;
import ViewModel.ICommand;
import ViewModel.PlantViewModel;

import javax.swing.*;
import java.util.List;

public class FilterPlantsCommand implements ICommand {
    private final PlantViewModel plantViewModel;

    public FilterPlantsCommand(PlantViewModel plantViewModel) {
        this.plantViewModel = plantViewModel;
    }

    @Override
    public void execute() {
        String nameFilter = plantViewModel.getNameFilter().trim();
        String speciesFilter = plantViewModel.getSpeciesFilter().trim();
        boolean carnivoreFilter = plantViewModel.isCarnivoreFilter();
        Zone zoneFilter = plantViewModel.getZoneFilter();

        PlantRepository plantRepository = new PlantRepository();
        List<Plant> filteredPlants = plantRepository.filterPlants(nameFilter, speciesFilter, carnivoreFilter, zoneFilter);
        if (!filteredPlants.isEmpty()) {
            plantViewModel.plantsTable.setRowCount(0);
            for (Plant plant : filteredPlants) {
                plantViewModel.plantsTable.addRow(new Object[]{plant.getPlantID(), plant.getName(), plant.getSpecies(), plant.isCarnivore(), plant.getZone()});
            }
        } else {
            JOptionPane.showMessageDialog(null, "No plants found matching the criteria!", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}