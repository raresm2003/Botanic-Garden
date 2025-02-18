package Domain.Plant;

import Domain.Subject;

public class Plant extends Subject {

    private int plantID;
    private String name;
    private String species;
    private boolean carnivore;
    private Zone zone;
    private String image;

    public Plant(int plantID, String name, String species, boolean carnivore, Zone zone, String image) {
        this.plantID = plantID;
        this.name = name;
        this.species = species;
        this.carnivore = carnivore;
        this.zone = zone;
        this.image = image;
        notifyObservers(this);
    }

    public Plant(String name, String species, boolean carnivore, Zone zone, String image) {
        this.name = name;
        this.species = species;
        this.carnivore = carnivore;
        this.zone = zone;
        this.image = image;
        notifyObservers(this);
    }

    public int getPlantID() {
        return plantID;
    }

    public void setPlantID(int plantID) {
        this.plantID = plantID;
        notifyObservers(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyObservers(this);
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
        notifyObservers(this);
    }

    public boolean isCarnivore() {
        return carnivore;
    }

    public void setCarnivore(boolean carnivore) {
        this.carnivore = carnivore;
        notifyObservers(this);
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
        notifyObservers(this);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        notifyObservers(this);
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
