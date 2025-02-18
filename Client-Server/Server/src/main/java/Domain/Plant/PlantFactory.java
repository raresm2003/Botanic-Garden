package Domain.Plant;

public class PlantFactory {
    public static Plant createPlant(int plantID, String name, String species, boolean carnivore, Zone zone, String image) {
        return new Plant(plantID, name, species, carnivore, zone, image);
    }

    public static Plant createPlant(String name, String species, boolean carnivore, Zone zone, String image) {
        return new Plant(name, species, carnivore, zone, image);
    }
}
