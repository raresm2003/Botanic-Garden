package Controller;

import Communication.Client;
import View.EmployeeGUI;

import org.apache.poi.xwpf.usermodel.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Objects;

public class EmployeeController {

    private final EmployeeGUI employeeGUI;
    private Client client;

    public EmployeeController(Client client) {
        this.employeeGUI = new EmployeeGUI();
        this.client = client;
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
            MainController mainController = new MainController(this.client);
        });

        populatePlantList();
    }

    private void populatePlantList() {
        client.sendMessage("startPlant");
        String message = client.listenForMessage();

        try {
            if (message != null) {
                employeeGUI.resetDgvPlantsList();
                String[] plants = message.split("#");
                for (int i = 0; i < plants.length; i += 5) {
                    employeeGUI.addRowDgvPlantsList(new Object[]{plants[i], plants[i+1], plants[i+2], plants[i+3], plants[i+4]});
                }
            } else {
                employeeGUI.showMessage("Empty", "The list of plants is empty!");
            }
        } catch (Exception exception) {
            employeeGUI.showMessage("Empty", "The list of plants is empty!");
        }
    }

    private void addPlant(){
        JTextField nameField = new JTextField();
        JTextField speciesField = new JTextField();
        JCheckBox carnivoreCheckbox = new JCheckBox("Carnivore");
        JComboBox<String> zoneComboBox = new JComboBox<>();
        zoneComboBox.addItem("FLOWER_GARDEN");
        zoneComboBox.addItem("HERBARIUM");
        zoneComboBox.addItem("ARBORETUM");
        zoneComboBox.addItem("WATER_FEATURES");
        zoneComboBox.addItem("NATIVE_PLANTS");
        //zoneComboBox.setSelectedIndex(0);
        JTextField imageField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Species:"));
        panel.add(speciesField);
        panel.add(carnivoreCheckbox);
        panel.add(new JLabel("Zone:"));
        panel.add(zoneComboBox);
        panel.add(new JLabel("Image:"));
        panel.add(imageField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Plant",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String species = speciesField.getText();
            String carnivore = String.valueOf(carnivoreCheckbox.isSelected());
            String zone = (String) zoneComboBox.getSelectedItem();
            String image = imageField.getText();

            client.sendMessage("addPlant#" + name + "#" + species + "#" + carnivore + "#" + zone + "#" + image);
            String message = client.listenForMessage();

            try {
                if (message != null) {
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
            JTextField nameField = new JTextField(employeeGUI.getTableValueAt(1));
            JTextField speciesField = new JTextField(employeeGUI.getTableValueAt(2));
            JCheckBox carnivoreCheckbox = new JCheckBox("Carnivore", Boolean.valueOf(employeeGUI.getTableValueAt(3)));
            JComboBox<String> zoneComboBox = new JComboBox<>();
            zoneComboBox.addItem("FLOWER_GARDEN");
            zoneComboBox.addItem("HERBARIUM");
            zoneComboBox.addItem("ARBORETUM");
            zoneComboBox.addItem("WATER_FEATURES");
            zoneComboBox.addItem("NATIVE_PLANTS");
            zoneComboBox.setSelectedItem(employeeGUI.getTableValueAt(4));
            JTextField imageField = new JTextField("Unchanged");

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Species:"));
            panel.add(speciesField);
            panel.add(carnivoreCheckbox);
            panel.add(new JLabel("Zone:"));
            panel.add(zoneComboBox);
            panel.add(new JLabel("Image:"));
            panel.add(imageField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Update Plant",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String species = speciesField.getText();
                String carnivore = String.valueOf(carnivoreCheckbox.isSelected());
                String zone = (String) zoneComboBox.getSelectedItem();
                String image = imageField.getText();

                client.sendMessage("updatePlant#" + selectedPlantID + "#" + name + "#" + species + "#" + carnivore + "#" + zone + "#" + image);
                String message = client.listenForMessage();

                try {
                    if (message != null) {
                        employeeGUI.showMessage("Success", "Plant updated successfully.");
                        populatePlantList();
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
                    client.sendMessage("deletePlant#" + selectedPlantID);
                    String message = client.listenForMessage();

                    if (message != null) {
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
        }
    }

    private void applyFilters() {
        String nameFilter = employeeGUI.getTxtNameFilter();
        String speciesFilter = employeeGUI.getTxtSpeciesFilter();
        String carnivoreFilter = String.valueOf(employeeGUI.isChkCarnivoreFilterSelected());
        String zoneFilter = employeeGUI.getSelectedZone();

        client.sendMessage("filterPlants#" + nameFilter + "#" + speciesFilter + "#" + carnivoreFilter + "#" + zoneFilter);
        String message = client.listenForMessage();

        try {
            if (message != null) {
                employeeGUI.resetDgvPlantsList();
                String[] plants = message.split("#");
                for (int i = 0; i < plants.length; i += 5) {
                    employeeGUI.addRowDgvPlantsList(new Object[]{plants[i], plants[i+1], plants[i+2], plants[i+3], plants[i+4]});
                }
            } else {
                employeeGUI.showMessage("Empty", "No plants match the filters!");
            }
        } catch (Exception exception) {
            employeeGUI.showMessage("Empty", "No plants match the filters!");
        }
    }

    private void savePlantList() {
        JComboBox<String> formatComboBox;
        String[] formats = {"CSV", "JSON", "XML", "DOC"};
        formatComboBox = new JComboBox<>(formats);

        Object[] message = {"File format:", formatComboBox};

        int option = JOptionPane.showConfirmDialog(employeeGUI, message, "Save Plant List", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            client.sendMessage("startPlant");
            String plantData = client.listenForMessage();
            String[] plants = plantData.split("#");

            if (plantData == null || plantData.isEmpty()) {
                System.out.println("No plant data available.");
                return;
            }

            String fileType = (String) formatComboBox.getSelectedItem();

            switch (Objects.requireNonNull(fileType)) {
                case "CSV":
                    String csvFileName = "plants.csv";
                    try (FileWriter writer = new FileWriter(csvFileName)) {
                        writer.append("ID,Name,Species,Carnivore,Zone\n");
                        for (int i = 0; i < plants.length; i += 5) {
                            writer.append(plants[i])
                                    .append(",")
                                    .append(plants[i + 1])
                                    .append(",")
                                    .append(plants[i + 2])
                                    .append(",")
                                    .append(plants[i + 3])
                                    .append(",")
                                    .append(plants[i + 4])
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
                        writer.write("{ \"plants\": [");
                        for (int i = 0; i < plants.length; i += 5) {
                            writer.write("{");
                            writer.write("\"ID\": \"" + plants[i] + "\",");
                            writer.write("\"Name\": \"" + plants[i + 1] + "\",");
                            writer.write("\"Species\": \"" + plants[i + 2] + "\",");
                            writer.write("\"Carnivore\": \"" + plants[i + 3] + "\",");
                            writer.write("\"Zone\": \"" + plants[i + 4] + "\"");
                            writer.write("}");
                            if (i + 5 < plants.length) writer.write(",");
                        }
                        writer.write("]}");
                        System.out.println("JSON file saved successfully.");
                    } catch (IOException e) {
                        System.out.println("An error occurred while saving JSON file.");
                        e.printStackTrace();
                    }
                    break;
                case "XML":
                    String xmlFileName = "plants.xml";
                    try (FileWriter writer = new FileWriter(xmlFileName)) {
                        writer.write("<plants>");
                        for (int i = 0; i < plants.length; i += 5) {
                            writer.write("<plant>");
                            writer.write("<ID>" + plants[i] + "</ID>");
                            writer.write("<Name>" + plants[i + 1] + "</Name>");
                            writer.write("<Species>" + plants[i + 2] + "</Species>");
                            writer.write("<Carnivore>" + plants[i + 3] + "</Carnivore>");
                            writer.write("<Zone>" + plants[i + 4] + "</Zone>");
                            writer.write("</plant>");
                        }
                        writer.write("</plants>");
                        System.out.println("XML file saved successfully.");
                    } catch (IOException e) {
                        System.out.println("An error occurred while saving XML file.");
                        e.printStackTrace();
                    }
                    break;
                case "DOC":
                    String docFileName = "plants.doc";
                    try (FileOutputStream out = new FileOutputStream(docFileName);
                         XWPFDocument document = new XWPFDocument()) {
                        for (int i = 0; i < plants.length; i += 5) {
                            XWPFParagraph paragraph = document.createParagraph();
                            paragraph.createRun().setText(
                                    "ID: " + plants[i] + ", Name: " + plants[i + 1] +
                                            ", Species: " + plants[i + 2] + ", Carnivore: " +
                                            plants[i + 3] + ", Zone: " + plants[i + 4]);
                            paragraph.createRun().addBreak();
                        }
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


    private void showStats() {
        client.sendMessage("startPlant");
        String plantData = client.listenForMessage();

        if (plantData == null || plantData.isEmpty()) {
            System.out.println("No plant data available.");
            return;
        }

        DefaultPieDataset pieDataset = new DefaultPieDataset();
        DefaultCategoryDataset barDataset = new DefaultCategoryDataset();

        String[] plantArray = plantData.split("#");
        String[] zones = {"FLOWER_GARDEN", "HERBARIUM", "ARBORETUM", "WATER_FEATURES", "NATIVE_PLANTS"};
        int[] zoneCount = new int[zones.length];

        for (int i = 0; i < plantArray.length; i += 5) {
            String zoneStr = plantArray[i + 4];
            for (int j = 0; j < zones.length; j++) {
                if (zones[j].equals(zoneStr)) {
                    zoneCount[j]++;
                    break;
                }
            }
        }

        for (int i = 0; i < zoneCount.length; i++) {
            String zoneName = zones[i];
            pieDataset.setValue(zoneName, zoneCount[i]);
            barDataset.addValue(zoneCount[i], "Plant Count", zoneName);
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Plant Distribution by Zone",
                pieDataset,
                true,
                true,
                false
        );

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
            client.sendMessage("showPlantImage#" + selectedPlantID);
            String message = client.listenForMessage();

            if (message.equals("null")) {
                employeeGUI.showMessage("Empty", "The selected plant has no image!");
            } else {
                try {
                    URL url = new URL(message);
                    ImageIcon imageIcon = new ImageIcon(url);

                    if (imageIcon.getIconWidth() == -1 || imageIcon.getIconHeight() == -1) {
                        JOptionPane.showMessageDialog(employeeGUI, "Failed to load plant image.", "Image Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int maxWidth = 400;
                    int maxHeight = 400;
                    Image image = imageIcon.getImage();
                    int width = imageIcon.getIconWidth();
                    int height = imageIcon.getIconHeight();

                    if (width > maxWidth || height > maxHeight) {
                        float widthRatio = (float) maxWidth / width;
                        float heightRatio = (float) maxHeight / height;
                        float scaleFactor = Math.min(widthRatio, heightRatio);

                        width = (int) (width * scaleFactor);
                        height = (int) (height * scaleFactor);
                        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        imageIcon = new ImageIcon(scaledImage);
                    }

                    JLabel imageLabel = new JLabel(imageIcon);

                    JFrame imageFrame = new JFrame("Plant Image: " + employeeGUI.getTableValueAt(1));
                    imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    imageFrame.getContentPane().add(imageLabel, BorderLayout.CENTER);
                    imageFrame.pack();
                    imageFrame.setLocationRelativeTo(employeeGUI);
                    imageFrame.setVisible(true);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(employeeGUI, "Failed to load plant image from the internet.", "Image Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(employeeGUI, "Please select a plant.", "No Plant Selected", JOptionPane.WARNING_MESSAGE);
        }
    }
}

