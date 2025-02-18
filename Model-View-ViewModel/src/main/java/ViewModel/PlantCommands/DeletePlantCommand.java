package ViewModel.PlantCommands;

import Model.Repository.PlantRepository;
import ViewModel.ICommand;
import ViewModel.PlantViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DeletePlantCommand implements ICommand {
    private final PlantViewModel plantViewModel;

    public DeletePlantCommand(PlantViewModel plantViewModel) {
        this.plantViewModel = plantViewModel;
    }

    @Override
    public void execute() {
        PlantRepository plantRepository = new PlantRepository();
        int selectedRowIndex = plantViewModel.getSelectedRowIndex();
        DefaultTableModel model = plantViewModel.getPlantsTable();

        if (selectedRowIndex != -1) {
            int plantID = (int) model.getValueAt(selectedRowIndex, 0);
            int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this plant?", "Delete Plant", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                if (plantRepository.deletePlant(plantID)) {
                    model.removeRow(selectedRowIndex);
                    JOptionPane.showMessageDialog(null, "Plant deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete plant from the database!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a plant to delete!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}