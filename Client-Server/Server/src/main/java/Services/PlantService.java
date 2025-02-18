package Services;

import Domain.Plant.*;
import Repositories.PlantRepository;

import java.util.List;

public class PlantService implements IPlantService {
    private final IPlantRepository plantRepository;

    public PlantService() {
        this.plantRepository = new PlantRepository();
    }

    @Override
    public boolean addPlant(String plantInfo1, String plantInfo2, String plantInfo3, String plantInfo4, String plantInfo5) {
        return plantRepository.addPlant(new Plant(plantInfo1, plantInfo2, Boolean.parseBoolean(plantInfo3), Zone.valueOf(plantInfo4), plantInfo5));
    }

    @Override
    public boolean deletePlant(String message) {
        return plantRepository.deletePlant(Integer.parseInt(message));
    }

    @Override
    public boolean updatePlant(String plantInfo0, String plantInfo1, String plantInfo2, String plantInfo3, String plantInfo4, String plantInfo5) {
        if(plantInfo5.equals("Unchanged")){
            plantInfo5 = plantRepository.searchPlantByID(Integer.parseInt(plantInfo0)).getImage();
        }
        Plant plant = new Plant(plantInfo1, plantInfo2, Boolean.parseBoolean(plantInfo3), Zone.valueOf(plantInfo4), plantInfo5);
        return plantRepository.updatePlant(Integer.parseInt(plantInfo0), plant);
    }

    @Override
    public String getPlantList() {
        List<Plant> plantList = plantRepository.getPlantList();
        StringBuilder attributesString = new StringBuilder();

        for (Plant plant : plantList) {
            attributesString.append(plant.getPlantID()).append("#")
                    .append(plant.getName()).append("#")
                    .append(plant.getSpecies()).append("#")
                    .append(plant.isCarnivore()).append("#")
                    .append(plant.getZone()).append("#");
        }

        return attributesString.toString();
    }

    @Override
    public String searchPlantByID(String message) {
        Plant selectedPlant = plantRepository.searchPlantByID(Integer.parseInt(message));
        return selectedPlant.getName() + "#" + selectedPlant.getSpecies() + "#" + selectedPlant.isCarnivore() + "#" + selectedPlant.getZone().toString();
    }

    @Override
    public String filterPlants(String plantInfo1, String plantInfo2, String plantInfo3, String plantInfo4) {
        Zone zoneFilter = plantInfo4.equals("All Zones") ? null : Zone.valueOf(plantInfo4);
        List<Plant> filteredPlantList = plantRepository.filterPlants(plantInfo1, plantInfo2, Boolean.parseBoolean(plantInfo3), zoneFilter);
        StringBuilder attributesString = new StringBuilder();

        for (Plant plant : filteredPlantList) {
            attributesString.append(plant.getPlantID()).append("#")
                    .append(plant.getName()).append("#")
                    .append(plant.getSpecies()).append("#")
                    .append(plant.isCarnivore()).append("#")
                    .append(plant.getZone()).append("#");
        }

        return attributesString.toString();
    }

    @Override
    public String getPlantImage(String message) {
        Plant selectedPlant = plantRepository.searchPlantByID(Integer.parseInt(message));
        if(selectedPlant.getImage() != null)
            return selectedPlant.getImage();
        else
            return "null";
    }
}