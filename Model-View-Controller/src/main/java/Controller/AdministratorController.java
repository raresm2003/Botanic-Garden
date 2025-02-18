package Controller;

import Model.Plant;
import Model.Repository.PlantRepository;
import Model.Repository.UserRepository;
import Model.User;
import Model.UserType;
import Model.Zone;
import View.AdministratorGUI;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class AdministratorController {
    private final AdministratorGUI administratorGUI;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;

    public AdministratorController(boolean userOn) {
        this.administratorGUI = new AdministratorGUI(userOn);
        this.plantRepository = new PlantRepository();
        this.userRepository = new UserRepository();
        initialize();
    }

    private void initialize() {

        administratorGUI.addBtnFilterListener(e -> {
            if(administratorGUI.getUserOn())
                applyFiltersUser();
            else
                applyFiltersPlant();
        });

        administratorGUI.addBtnAddListener(e -> {
            if(administratorGUI.getUserOn())
                addUser();
            else
                addPlant();
        });

        administratorGUI.addBtnUpdateListener(e -> {
            if(administratorGUI.getUserOn())
                updateUser();
            else
                updatePlant();
        });

        administratorGUI.addBtnDeleteListener(e -> {
            if(administratorGUI.getUserOn())
                deleteUser();
            else
                deletePlant();
        });

        administratorGUI.addBtnPlantsListener(e -> {
            administratorGUI.dispose();
            AdministratorController administratorController = new AdministratorController(false);
        });

        administratorGUI.addBtnUsersListener(e -> {
            administratorGUI.dispose();
            AdministratorController administratorController = new AdministratorController(true);
        });

        administratorGUI.addBtnSavePlantListListener(e -> savePlantList());

        administratorGUI.addBtnStatisticListener(e -> showStats());

        administratorGUI.addBtnImageListener(e -> showPlantImage());

        administratorGUI.addBtnLogoutListener(e -> {
            administratorGUI.dispose();
            MainController mainController = new MainController();
        });

        if(administratorGUI.getUserOn())
            populateUserList();
        else {
            populatePlantList();
        }
    }

    private void populatePlantList() {
        try {
            List<Plant> plantList = plantRepository.getPlantList();
            if (plantList != null) {
                administratorGUI.resetDgvList();
                for (Plant plant : plantList) {
                    administratorGUI.addRowDgvList(new Object[]{plant.getPlantID(), plant.getName(), plant.getSpecies(), plant.isCarnivore(), plant.getZone()});
                }
            } else {
                administratorGUI.showMessage("Empty", "The list of plants is empty!");
            }
        } catch (Exception exception) {
            administratorGUI.showMessage("Plants list - exception", exception.toString());
        }
    }

    private void addPlant(){
        JTextField nameField = new JTextField();
        JTextField speciesField = new JTextField();
        JCheckBox carnivoreCheckbox = new JCheckBox("Carnivore");
        JComboBox<Zone> zoneComboBox = new JComboBox<>(Zone.values());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Species:"));
        panel.add(speciesField);
        panel.add(carnivoreCheckbox);
        panel.add(new JLabel("Zone:"));
        panel.add(zoneComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Plant",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String species = speciesField.getText();
            boolean carnivore = carnivoreCheckbox.isSelected();
            Zone zone = (Zone) zoneComboBox.getSelectedItem();

            try {
                Plant newPlant = new Plant(name, species, carnivore, zone);
                boolean success = plantRepository.addPlant(newPlant);
                if (success) {
                    administratorGUI.showMessage("Success", "Plant added successfully.");
                    populatePlantList();
                } else {
                    administratorGUI.showMessage("Error", "Failed to add plant.");
                }
            } catch (IllegalArgumentException ex) {
                administratorGUI.showMessage("Error", "Invalid zone provided.");
            }
        }
    }

    private void updatePlant(){
        int selectedPlantID = administratorGUI.getSelectedID();
        if (selectedPlantID != -1) {
            Plant selectedPlant = plantRepository.searchPlantByID(selectedPlantID);

            JTextField nameField = new JTextField(selectedPlant.getName());
            JTextField speciesField = new JTextField(selectedPlant.getSpecies());
            JCheckBox carnivoreCheckbox = new JCheckBox("Carnivore", selectedPlant.isCarnivore());
            JComboBox<Zone> zoneComboBox = new JComboBox<>(Zone.values());
            zoneComboBox.setSelectedItem(selectedPlant.getZone());

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Species:"));
            panel.add(speciesField);
            panel.add(carnivoreCheckbox);
            panel.add(new JLabel("Zone:"));
            panel.add(zoneComboBox);

            int result = JOptionPane.showConfirmDialog(null, panel, "Update Plant",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String species = speciesField.getText();
                boolean carnivore = carnivoreCheckbox.isSelected();
                Zone zone = (Zone) zoneComboBox.getSelectedItem();

                try {
                    Plant updatedPlant = new Plant(selectedPlantID, name, species, carnivore, zone);
                    boolean success = plantRepository.updatePlant(selectedPlantID, updatedPlant);
                    if (success) {
                        administratorGUI.showMessage("Success", "Plant updated successfully.");
                        populatePlantList(); // Refresh plant list after updating
                    } else {
                        administratorGUI.showMessage("Error", "Failed to update plant.");
                    }
                } catch (IllegalArgumentException ex) {
                    administratorGUI.showMessage("Error", "Invalid zone provided.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(administratorGUI, "Please select a plant to update.", "Update Plant", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deletePlant(){
        int selectedPlantID = administratorGUI.getSelectedID();
        if (selectedPlantID != -1) {
            int option = JOptionPane.showConfirmDialog(administratorGUI, "Are you sure you want to delete this plant?", "Delete Plant", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    boolean success = plantRepository.deletePlant(selectedPlantID);
                    if (success) {
                        administratorGUI.showMessage("Success", "Plant deleted successfully.");
                        populatePlantList();
                    } else {
                        administratorGUI.showMessage("Error", "Failed to delete plant.");
                    }
                } catch (Exception exception) {
                    administratorGUI.showMessage("Error", "An error occurred while deleting plant.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(administratorGUI, "Please select a plant to delete.", "Delete Plant", JOptionPane.WARNING_MESSAGE);
        }}


    private void applyFiltersPlant() {
        String nameFilter = administratorGUI.getTxtNameFilter();
        String speciesFilter = administratorGUI.getTxtSpeciesFilter();
        boolean carnivoreFilter = administratorGUI.isChkCarnivoreFilterSelected();
        String selectedZone = administratorGUI.getSelectedZone();
        Zone zoneFilter = selectedZone.equals("All Zones") ? null : Zone.valueOf(selectedZone);

        try {
            List<Plant> filteredList = plantRepository.filterPlants(nameFilter, speciesFilter, carnivoreFilter, zoneFilter);
            if (filteredList != null) {
                administratorGUI.resetDgvList();
                for (Plant plant : filteredList) {
                    administratorGUI.addRowDgvList(new Object[]{plant.getPlantID(), plant.getName(), plant.getSpecies(), plant.isCarnivore(), plant.getZone()});
                }
            } else {
                administratorGUI.showMessage("Empty", "No plants match the filters!");
            }
        } catch (Exception exception) {
            administratorGUI.showMessage("Filtering plants - exception", exception.toString());
        }
    }

    private void savePlantList() {
        JComboBox<String> formatComboBox;
        String[] formats = {"CSV", "JSON", "XML", "DOC"};
        formatComboBox = new JComboBox<>(formats);

        Object[] message = {"File format:", formatComboBox};

        int option = JOptionPane.showConfirmDialog(administratorGUI, message, "Save Plant List", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            List<Plant> allPlants = plantRepository.getPlantList();
            String fileType = (String) formatComboBox.getSelectedItem();

            switch (Objects.requireNonNull(fileType)) {
                case "CSV":
                    String csvFileName = "plants.csv";
                    try (FileWriter writer = new FileWriter(csvFileName)) {
                        // Writing CSV header
                        writer.append("ID,Name,Species,Carnivore,Zone\n");
                        // Writing plant data
                        for (Plant plant : allPlants) {
                            writer.append(Integer.toString(plant.getPlantID()))
                                    .append(",")
                                    .append(plant.getName())
                                    .append(",")
                                    .append(plant.getSpecies())
                                    .append(",")
                                    .append(Boolean.toString(plant.isCarnivore()))
                                    .append(",")
                                    .append(plant.getZone().toString())
                                    .append("\n");
                        }
                        System.out.println("CSV file saved successfully.");
                    } catch (IOException e) {
                        System.out.println("An error occurred while saving CSV file.");
                        e.printStackTrace();
                    }
                    break;
                case "JSON":
                    String jsonFileName = "plants.json";
                    try (FileWriter writer = new FileWriter(jsonFileName)) {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.writeValue(writer, allPlants);
                        System.out.println("JSON file saved successfully.");
                    } catch (IOException e) {
                        System.out.println("An error occurred while saving JSON file.");
                        e.printStackTrace();
                    }
                    break;
                case "XML":
                    String xmlFileName = "plants.xml";
                    try {
                        JAXBContext jaxbContext = JAXBContext.newInstance(Plant.class);
                        Marshaller marshaller = jaxbContext.createMarshaller();
                        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                        marshaller.marshal(allPlants, new File(xmlFileName));
                        System.out.println("XML file saved successfully.");
                    } catch (JAXBException e) {
                        System.out.println("An error occurred while saving XML file.");
                        e.printStackTrace();
                    }
                    break;
                case "DOC":
                    String docFileName = "plants.doc";
                    try (FileOutputStream out = new FileOutputStream(docFileName);
                         XWPFDocument document = new XWPFDocument()) {
                        // Creating paragraph for each plant
                        for (Plant plant : allPlants) {
                            XWPFParagraph paragraph = document.createParagraph();
                            // Adding text to paragraph for each plant
                            paragraph.createRun().setText(plant.getPlantID() + ", " + plant.getName() + ", " + plant.getSpecies() + ", " + plant.isCarnivore() + ", " + plant.getZone());
                            // Add a newline (Enter) after each plant entry
                            paragraph.createRun().addBreak();
                        }
                        // Writing the document to file
                        document.write(out);
                        System.out.println("DOC file saved successfully.");
                    } catch (IOException e) {
                        System.out.println("An error occurred while creating or saving DOC document.");
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("Unsupported file type");
                    break;
            }
        }
    }

    public void populateUserList() {
        try {
            List<User> userList = userRepository.getUserList();
            if (userList != null) {
                administratorGUI.resetDgvList();
                for (User user : userList) {
                    administratorGUI.addRowDgvList(new Object[]{user.getUserID(), user.getName(), user.getType()});
                }
            } else {
                administratorGUI.showMessage("Empty", "The list of users is empty!");
            }
        } catch (Exception exception) {
            administratorGUI.showMessage("Users list - exception", exception.toString());
        }
    }

    private void addUser() {
        JTextField nameField = new JTextField();
        JTextField passwordField = new JTextField();
        JComboBox<UserType> typeComboBox = new JComboBox<>(UserType.values());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Type:"));
        panel.add(typeComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add User",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String password = passwordField.getText();
            UserType type = UserType.valueOf(typeComboBox.getSelectedItem().toString());

            try {
                User newUser = new User(name, password, type);
                boolean success = userRepository.addUser(newUser);
                if (success) {
                    administratorGUI.showMessage("Success", "User added successfully.");
                    populateUserList();
                } else {
                    administratorGUI.showMessage("Error", "Failed to add user.");
                }
            } catch (IllegalArgumentException ex) {
                administratorGUI.showMessage("Error", "Invalid user type provided.");
            }
        }
    }

    private void updateUser() {
        int selectedUserID = administratorGUI.getSelectedID();
        if (selectedUserID != -1) {
            User selectedUser = userRepository.searchUserByID(selectedUserID);

            JTextField nameField = new JTextField(selectedUser.getName());
            JTextField passwordField = new JTextField(selectedUser.getPassword());
            JComboBox<UserType> typeComboBox = new JComboBox<>(UserType.values());
            typeComboBox.setSelectedItem(selectedUser.getType().toString());

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Password:"));
            panel.add(passwordField);
            panel.add(new JLabel("Type:"));
            panel.add(typeComboBox);

            int result = JOptionPane.showConfirmDialog(null, panel, "Update User",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String password = passwordField.getText();
                UserType type = UserType.valueOf(typeComboBox.getSelectedItem().toString());

                try {
                    User updatedUser = new User(selectedUserID, name, password, type);
                    boolean success = userRepository.updateUser(selectedUserID, updatedUser);
                    if (success) {
                        administratorGUI.showMessage("Success", "User updated successfully.");
                        populateUserList(); // Refresh user list after updating
                    } else {
                        administratorGUI.showMessage("Error", "Failed to update user.");
                    }
                } catch (IllegalArgumentException ex) {
                    administratorGUI.showMessage("Error", "Invalid user type provided.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(administratorGUI, "Please select a user to update.", "Update User", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteUser() {
        int selectedUserID = administratorGUI.getSelectedID();
        if (selectedUserID != -1) {
            int option = JOptionPane.showConfirmDialog(administratorGUI, "Are you sure you want to delete this user?", "Delete User", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    boolean success = userRepository.deleteUser(selectedUserID);
                    if (success) {
                        administratorGUI.showMessage("Success", "User deleted successfully.");
                        populateUserList(); // Refresh user list after deletion
                    } else {
                        administratorGUI.showMessage("Error", "Failed to delete user.");
                    }
                } catch (Exception exception) {
                    administratorGUI.showMessage("Error", "An error occurred while deleting user.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(administratorGUI, "Please select a user to delete.", "Delete User", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void applyFiltersUser() {
        String nameFilter = administratorGUI.getTxtNameFilter();
        String selectedUserType = administratorGUI.getSelectedType();
        UserType typeFilter = selectedUserType.equals("All Types") ? null : UserType.valueOf(selectedUserType);

        try {
            List<User> filteredList = userRepository.filterUsers(nameFilter, typeFilter);
            if (filteredList != null && !filteredList.isEmpty()) {
                administratorGUI.resetDgvList();
                for (User user : filteredList) {
                    administratorGUI.addRowDgvList(new Object[]{user.getUserID(), user.getName(), user.getType()});
                }
            } else {
                administratorGUI.showMessage("Empty", "No users match the selected filters!");
            }
        } catch (Exception exception) {
            administratorGUI.showMessage("Filtering error", exception.toString());
        }
    }

    private void showStats(){
        if (plantRepository == null) {
            System.err.println("PlantRepository is not properly initialized.");
            return;
        }

        DefaultPieDataset pieDataset = new DefaultPieDataset();
        DefaultCategoryDataset barDataset = new DefaultCategoryDataset();

        List<Plant> plants = plantRepository.getPlantList();
        if (plants.isEmpty()) {
            System.out.println("No plant data available.");
            return;
        }
        int[] zoneCount = new int[Zone.values().length];
        for (Plant plant : plants) {
            zoneCount[plant.getZone().ordinal()]++;
        }
        for (int i = 0; i < zoneCount.length; i++) {
            pieDataset.setValue(Zone.values()[i].toString(), zoneCount[i]);
            barDataset.addValue(zoneCount[i], "Plant Count", Zone.values()[i].toString());
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Plant Distribution by Zone",
                pieDataset,
                true,
                true,
                false
        );

        // Create bar chart
        JFreeChart barChart = ChartFactory.createBarChart(
                "Plant Distribution by Zone",
                "Zone",
                "Plant Count",
                barDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartPanel pieChartPanel = new ChartPanel(pieChart);
        pieChartPanel.setPreferredSize(new Dimension(500, 300));

        ChartPanel barChartPanel = new ChartPanel(barChart);
        barChartPanel.setPreferredSize(new Dimension(500, 300));

        JFrame pieChartFrame = new JFrame("Plant Distribution Pie Chart");
        pieChartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pieChartFrame.getContentPane().add(pieChartPanel);
        pieChartFrame.pack();
        pieChartFrame.setLocationRelativeTo(administratorGUI);
        pieChartFrame.setVisible(true);

        JFrame barChartFrame = new JFrame("Plant Distribution Bar Chart");
        barChartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        barChartFrame.getContentPane().add(barChartPanel);
        barChartFrame.pack();
        barChartFrame.setLocationRelativeTo(administratorGUI);
        barChartFrame.setVisible(true);
    }

    private void showPlantImage() {
        int selectedPlantID = administratorGUI.getSelectedID();
        if (selectedPlantID != -1) {
            Plant selectedPlant = plantRepository.searchPlantByID(selectedPlantID);

            String imagePath = "images/" + selectedPlant.getName() + ".jpeg";
            ImageIcon imageIcon = new ImageIcon(imagePath);

            if (imageIcon.getIconWidth() == -1 || imageIcon.getIconHeight() == -1) {
                JOptionPane.showMessageDialog(administratorGUI, "Failed to load plant image.", "Image Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JLabel imageLabel = new JLabel(imageIcon);

            JFrame imageFrame = new JFrame("Plant Image: " + selectedPlant.getName());
            imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            imageFrame.getContentPane().add(imageLabel, BorderLayout.CENTER);
            imageFrame.pack();
            imageFrame.setLocationRelativeTo(administratorGUI);
            imageFrame.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(administratorGUI, "Please select a plant.", "No Plant Selected", JOptionPane.WARNING_MESSAGE);
        }
    }
}
