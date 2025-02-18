package ViewModel.PlantCommands;

import Model.Plant;
import Model.Repository.PlantRepository;
import Model.Zone;
import ViewModel.ICommand;
import ViewModel.PlantViewModel;

import javax.swing.*;

public class AddPlantCommand implements ICommand {
    private final PlantViewModel plantViewModel;

    public AddPlantCommand(PlantViewModel plantViewModel) {
        this.plantViewModel = plantViewModel;
    }

    @Override
    public void execute() {
        PlantRepository plantRepository = new PlantRepository();
        String plantName = plantViewModel.getPlantName();
        String species = plantViewModel.getSpecies();
        boolean isCarnivore = plantViewModel.isCarnivore();
        Zone zone = plantViewModel.getZone();

        if (!plantName.isEmpty() && !species.isEmpty() && zone != null) {
            try {
                Plant plant = new Plant(plantName, species, isCarnivore, zone);
                if (plantRepository.addPlant(plant)) {
                    JOptionPane.showMessageDialog(null, "Plant added successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add plant to the database!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input for plant data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please fill in all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}