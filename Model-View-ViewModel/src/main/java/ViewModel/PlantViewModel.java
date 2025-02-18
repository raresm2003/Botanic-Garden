package ViewModel;

import Model.Zone;
import ViewModel.PlantCommands.*;

import javax.swing.table.DefaultTableModel;

public class PlantViewModel {
    public DefaultTableModel plantsTable;
    private String plantName;
    private String species;
    private boolean isCarnivore;
    private Zone zone;
    private int selectedRowIndex;

    // New fields for filters
    private String nameFilter;
    private String speciesFilter;
    private boolean carnivoreFilter;
    private Zone zoneFilter;

    private String fileType;

    public final ICommand addPlantCommand;
    public final ICommand deletePlantCommand;
    public final ICommand updatePlantCommand;
    public final ICommand populatePlantListCommand;
    public final ICommand filterPlantsCommand;
    public final ICommand savePlantListCommand;

    public PlantViewModel() {
        this.plantName = "";
        this.species = "";
        this.isCarnivore = false;
        this.zone = null;
        this.selectedRowIndex = -1;

        // Initialize filters
        this.nameFilter = "";
        this.speciesFilter = "";
        this.carnivoreFilter = false;
        this.zoneFilter = null;

        this.fileType = "";

        this.plantsTable = new DefaultTableModel();
        this.plantsTable.addColumn("Plant ID");
        this.plantsTable.addColumn("Plant Name");
        this.plantsTable.addColumn("Species");
        this.plantsTable.addColumn("Carnivore");
        this.plantsTable.addColumn("Zone");

        this.addPlantCommand = new AddPlantCommand(this);
        this.deletePlantCommand = new DeletePlantCommand(this);
        this.updatePlantCommand = new UpdatePlantCommand(this);
        this.filterPlantsCommand = new FilterPlantsCommand(this);
        this.populatePlantListCommand = new PopulatePlantListCommand(this);
        this.savePlantListCommand = new SavePlantListCommand(this);
    }

    public DefaultTableModel getPlantsTable() {
        return plantsTable;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public boolean isCarnivore() {
        return isCarnivore;
    }

    public void setCarnivore(boolean carnivore) {
        isCarnivore = carnivore;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    // Getters and setters for filters
    public String getNameFilter() {
        return nameFilter;
    }

    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }

    public String getSpeciesFilter() {
        return speciesFilter;
    }

    public void setSpeciesFilter(String speciesFilter) {
        this.speciesFilter = speciesFilter;
    }

    public boolean isCarnivoreFilter() {
        return carnivoreFilter;
    }

    public void setCarnivoreFilter(boolean carnivoreFilter) {
        this.carnivoreFilter = carnivoreFilter;
    }

    public Zone getZoneFilter() {
        return zoneFilter;
    }

    public void setZoneFilter(Zone zoneFilter) {
        this.zoneFilter = zoneFilter;
    }

    public int getSelectedRowIndex() {
        return selectedRowIndex;
    }

    public void setSelectedRowIndex(int selectedRowIndex) {
        this.selectedRowIndex = selectedRowIndex;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}