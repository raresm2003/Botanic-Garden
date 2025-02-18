package ViewModel.PlantCommands;

import Model.Plant;
import Model.Repository.PlantRepository;
import ViewModel.ICommand;
import ViewModel.PlantViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UpdatePlantCommand implements ICommand {
    private final PlantViewModel plantViewModel;

    public UpdatePlantCommand(PlantViewModel plantViewModel) {
        this.plantViewModel = plantViewModel;
    }

    @Override
    public void execute() {
        PlantRepository plantRepository = new PlantRepository();
        DefaultTableModel model = plantViewModel.getPlantsTable();
        int selectedRowIndex = plantViewModel.getSelectedRowIndex();

        if (selectedRowIndex != -1) {
            int plantID = (int) model.getValueAt(selectedRowIndex, 0);

            Plant updatedPlant = new Plant(plantID, plantViewModel.getPlantName(), plantViewModel.getSpecies(), plantViewModel.isCarnivore(), plantViewModel.getZone());
            if (plantRepository.updatePlant(plantID, updatedPlant)) {
                JOptionPane.showMessageDialog(null, "Plant updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update plant in the database!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Please select a plant to update!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}