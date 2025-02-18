package Model.Repository;

import Model.Plant;
import Model.Zone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlantRepository {
    private final Repository repository;

    public PlantRepository() {
        this.repository = new Repository();
    }

    public boolean addPlant(Plant plant) {
        String commandSQL = "INSERT INTO Plant (Name, Species, Carnivore, Zone) VALUES ('" +
                plant.getName() + "','" +
                plant.getSpecies() + "','" +
                (plant.isCarnivore() ? 1 : 0) + "','" +
                plant.getZone() + "')";
        return this.repository.executeSQL(commandSQL);
    }

    public boolean deletePlant(int plantID) {
        String commandSQL = "DELETE FROM Plant WHERE PlantID = '" + plantID + "'";
        return this.repository.executeSQL(commandSQL);
    }

    public boolean updatePlant(int plantID, Plant plant) {
        String commandSQL = "UPDATE Plant SET Name = '" +
                plant.getName() + "', Species = '" +
                plant.getSpecies() + "', Carnivore = '" +
                (plant.isCarnivore() ? 1 : 0) + "', Zone = '" +
                plant.getZone() + "' WHERE PlantID = '" + plantID + "'";
        return this.repository.executeSQL(commandSQL);
    }

    public List<Plant> getPlantList() {
        List<Plant> list = new ArrayList<>();
        ResultSet resultSet = this.repository.getResultSet("SELECT * FROM Plant");
        try {
            while (resultSet.next()) {
                list.add(convertToPlant(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Plant searchPlantByID(int plantID) {
        ResultSet resultSet = this.repository.getResultSet("SELECT * FROM Plant WHERE PlantID = '" + plantID + "'");
        try {
            if (resultSet.next()) {
                return convertToPlant(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Plant> filterPlants(String nameFilter, String speciesFilter, boolean carnivoreFilter, Zone zoneFilter) {
        List<Plant> filteredList = new ArrayList<>();
        String commandSQL = "SELECT * FROM Plant WHERE 1 = 1";

        if (nameFilter != null && !nameFilter.isEmpty()) {
            commandSQL += " AND Name LIKE '%" + nameFilter + "%'";
        }
        if (speciesFilter != null && !speciesFilter.isEmpty()) {
            commandSQL += " AND Species LIKE '%" + speciesFilter + "%'";
        }
        if (carnivoreFilter) {
            commandSQL += " AND Carnivore = 1";
        }
        if (zoneFilter != null) {
            commandSQL += " AND Zone = '" + zoneFilter.toString() + "'";
        }

        ResultSet resultSet = this.repository.getResultSet(commandSQL);
        try {
            while (resultSet.next()) {
                filteredList.add(convertToPlant(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return filteredList;
    }

    private Plant convertToPlant(ResultSet resultSet) throws SQLException {
        int plantID = resultSet.getInt("PlantID");
        String name = resultSet.getString("Name");
        String species = resultSet.getString("Species");
        boolean carnivore = resultSet.getInt("Carnivore") == 1;
        Zone zone = Zone.valueOf(resultSet.getString("Zone"));
        return new Plant(plantID, name, species, carnivore, zone);
    }
}

