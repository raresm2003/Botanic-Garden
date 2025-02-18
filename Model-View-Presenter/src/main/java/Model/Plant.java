package Model;

public class Plant {

    private int plantID;
    private String name;
    private String species;
    private boolean carnivore;
    private Zone zone;

    public Plant(int plantID, String name, String species, boolean carnivore, Zone zone) {
        this.plantID = plantID;
        this.name = name;
        this.species = species;
        this.carnivore = carnivore;
        this.zone = zone;
    }

    public Plant(String name, String species, boolean carnivore, Zone zone) {
        this.name = name;
        this.species = species;
        this.carnivore = carnivore;
        this.zone = zone;
    }

    public int getPlantID() {
        return plantID;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public boolean isCarnivore() {
        return carnivore;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "plantID=" + plantID +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", carnivore=" + carnivore +
                ", zone='" + zone + '\'' +
                '}';
    }
}
