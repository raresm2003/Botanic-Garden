package Controller;

import Model.Plant;
import Model.Repository.PlantRepository;
import Model.Zone;
import View.EmployeeGUI;

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

public class EmployeeController {

    private final EmployeeGUI employeeGUI;
    private final PlantRepository plantRepository;

    public EmployeeController() {
        this.employeeGUI = new EmployeeGUI();
        this.plantRepository = new PlantRepository();
        initialize();
    }

    private void initialize() {

        employeeGUI.addBtnFilterListener(e -> applyFilters());

        employeeGUI.addBtnAddListener(e -> addPlant());

        employeeGUI.addBtnUpdateListener(e -> updatePlant());

        employeeGUI.addBtnDeleteListener(e -> deletePlant());

        employeeGUI.addBtnSavePlantListListener(e -> savePlantList());

        employeeGUI.addBtnStatisticListener(e -> showStats());

        employeeGUI.addBtnImageListener(e -> showPlantImage());

        employeeGUI.addBtnLogoutListener(e -> {
            employeeGUI.dispose();
            MainController mainController = new MainController();
        });

        populatePlantList();
    }

    private void populatePlantList() {
        try {
            List<Plant> plantList = plantRepository.getPlantList();
            if (plantList != null) {
                employeeGUI.resetDgvPlantsList();
                for (Plant plant : plantList) {
                    employeeGUI.addRowDgvPlantsList(new Object[]{plant.getPlantID(), plant.getName(), plant.getSpecies(), plant.isCarnivore(), plant.getZone()});
                }
            } else {
                employeeGUI.showMessage("Empty", "The list of plants is empty!");
            }
        } catch (Exception exception) {
            employeeGUI.showMessage("Plants list - exception", exception.toString());
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
                    employeeGUI.showMessage("Success", "Plant added successfully.");
                    populatePlantList();
                } else {
                    employeeGUI.showMessage("Error", "Failed to add plant.");
                }
            } catch (IllegalArgumentException ex) {
                employeeGUI.showMessage("Error", "Invalid zone provided.");
            }
        }
    }

    private void updatePlant(){
        int selectedPlantID = employeeGUI.getSelectedPlantID();
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
                        employeeGUI.showMessage("Success", "Plant updated successfully.");
                        populatePlantList(); // Refresh plant list after updating
                    } else {
                        employeeGUI.showMessage("Error", "Failed to update plant.");
                    }
                } catch (IllegalArgumentException ex) {
                    employeeGUI.showMessage("Error", "Invalid zone provided.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(employeeGUI, "Please select a plant to update.", "Update Plant", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deletePlant(){
        int selectedPlantID = employeeGUI.getSelectedPlantID();
        if (selectedPlantID != -1) {
            int option = JOptionPane.showConfirmDialog(employeeGUI, "Are you sure you want to delete this plant?", "Delete Plant", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    boolean success = plantRepository.deletePlant(selectedPlantID);
                    if (success) {
                        employeeGUI.showMessage("Success", "Plant deleted successfully.");
                        populatePlantList();
                    } else {
                        employeeGUI.showMessage("Error", "Failed to delete plant.");
                    }
                } catch (Exception exception) {
                    employeeGUI.showMessage("Error", "An error occurred while deleting plant.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(employeeGUI, "Please select a plant to delete.", "Delete Plant", JOptionPane.WARNING_MESSAGE);
        }}


    private void applyFilters() {
        String nameFilter = employeeGUI.getTxtNameFilter();
        String speciesFilter = employeeGUI.getTxtSpeciesFilter();
        boolean carnivoreFilter = employeeGUI.isChkCarnivoreFilterSelected();
        String selectedZone = employeeGUI.getSelectedZone();
        Zone zoneFilter = selectedZone.equals("All Zones") ? null : Zone.valueOf(selectedZone);

        try {
            List<Plant> filteredList = plantRepository.filterPlants(nameFilter, speciesFilter, carnivoreFilter, zoneFilter);
            if (filteredList != null) {
                employeeGUI.resetDgvPlantsList();
                for (Plant plant : filteredList) {
                    employeeGUI.addRowDgvPlantsList(new Object[]{plant.getPlantID(), plant.getName(), plant.getSpecies(), plant.isCarnivore(), plant.getZone()});
                }
            } else {
                employeeGUI.showMessage("Empty", "No plants match the filters!");
            }
        } catch (Exception exception) {
            employeeGUI.showMessage("Filtering plants - exception", exception.toString());
        }
    }

    private void savePlantList() {
        JComboBox<String> formatComboBox;
        String[] formats = {"CSV", "JSON", "XML", "DOC"};
        formatComboBox = new JComboBox<>(formats);

        Object[] message = {"File format:", formatComboBox};

        int option = JOptionPane.showConfirmDialog(employeeGUI, message, "Save Plant List", JOptionPane.OK_CANCEL_OPTION);
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
        pieChartFrame.setLocationRelativeTo(employeeGUI);
        pieChartFrame.setVisible(true);

        JFrame barChartFrame = new JFrame("Plant Distribution Bar Chart");
        barChartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        barChartFrame.getContentPane().add(barChartPanel);
        barChartFrame.pack();
        barChartFrame.setLocationRelativeTo(employeeGUI);
        barChartFrame.setVisible(true);
    }

    private void showPlantImage() {
        int selectedPlantID = employeeGUI.getSelectedPlantID();
        if (selectedPlantID != -1) {
            Plant selectedPlant = plantRepository.searchPlantByID(selectedPlantID);

            String imagePath = "images/" + selectedPlant.getName() + ".jpeg";
            ImageIcon imageIcon = new ImageIcon(imagePath);

            if (imageIcon.getIconWidth() == -1 || imageIcon.getIconHeight() == -1) {
                JOptionPane.showMessageDialog(employeeGUI, "Failed to load plant image.", "Image Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JLabel imageLabel = new JLabel(imageIcon);

            JFrame imageFrame = new JFrame("Plant Image: " + selectedPlant.getName());
            imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            imageFrame.getContentPane().add(imageLabel, BorderLayout.CENTER);
            imageFrame.pack();
            imageFrame.setLocationRelativeTo(employeeGUI);
            imageFrame.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(employeeGUI, "Please select a plant.", "No Plant Selected", JOptionPane.WARNING_MESSAGE);
        }
    }
}
