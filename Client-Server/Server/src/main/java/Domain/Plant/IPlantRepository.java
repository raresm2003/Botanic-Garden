package Domain.Plant;

import java.util.List;

public interface IPlantRepository {

    boolean addPlant(Plant plant);

    boolean deletePlant(int plantID);

    boolean updatePlant(int plantID, Plant plant);

    List<Plant> getPlantList();

    Plant searchPlantByID(int plantID);

    List<Plant> filterPlants(String nameFilter, String speciesFilter, boolean carnivoreFilter, Zone zoneFilter);
}