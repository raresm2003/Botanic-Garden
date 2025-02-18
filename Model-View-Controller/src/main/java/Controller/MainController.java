package Controller;

import Model.Plant;
import Model.Repository.PlantRepository;
import Model.Repository.UserRepository;
import Model.User;
import Model.UserType;
import Model.Zone;
import View.MainGUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainController {
    private final MainGUI mainGUI;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;

    public MainController() {
        this.mainGUI = new MainGUI();
        this.plantRepository = new PlantRepository();
        this.userRepository =new UserRepository();
        initialize();
    }

    private void initialize() {
        mainGUI.addBtnFilterListener(e -> applyFilters());

        mainGUI.addBtnImageListener(e -> showPlantImage());

        mainGUI.addBtnLoginListener(e -> showLoginDialog());

        populatePlantList();
    }

    private void populatePlantList() {
        try {
            List<Plant> plantList = plantRepository.getPlantList();
            if (plantList != null) {
                mainGUI.resetDgvPlantsList();
                for (Plant plant : plantList) {
                    mainGUI.addRowDgvPlantsList(new Object[]{plant.getPlantID(), plant.getName(), plant.getSpecies(), plant.isCarnivore(), plant.getZone()});
                }
            } else {
                mainGUI.showMessage("Empty", "The list of plants is empty!");
            }
        } catch (Exception exception) {
            mainGUI.showMessage("Plants list - exception", exception.toString());
        }
    }

    private void applyFilters() {
        String nameFilter = mainGUI.getTxtNameFilter();
        String speciesFilter = mainGUI.getTxtSpeciesFilter();
        boolean carnivoreFilter = mainGUI.isChkCarnivoreFilterSelected();
        String selectedZone = mainGUI.getSelectedZone();
        Zone zoneFilter = selectedZone.equals("All Zones") ? null : Zone.valueOf(selectedZone);

        try {
            List<Plant> filteredList = plantRepository.filterPlants(nameFilter, speciesFilter, carnivoreFilter, zoneFilter);
            if (filteredList != null) {
                mainGUI.resetDgvPlantsList();
                for (Plant plant : filteredList) {
                    mainGUI.addRowDgvPlantsList(new Object[]{plant.getPlantID(), plant.getName(), plant.getSpecies(), plant.isCarnivore(), plant.getZone()});
                }
            } else {
                mainGUI.showMessage("Empty", "No plants match the filters!");
            }
        } catch (Exception exception) {
            mainGUI.showMessage("Filtering plants - exception", exception.toString());
        }
    }

    private void showLoginDialog() {
        JTextField txtUsername = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        Object[] message = {
                "Name:", txtUsername,
                "Password:", txtPassword
        };
        int option = JOptionPane.showConfirmDialog(mainGUI, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = txtUsername.getText();
            char[] password = txtPassword.getPassword();
            authenticateUser(username, new String(password));
        }
    }

    private void authenticateUser(String username, String password) {
        User user = userRepository.getUserByName(username);

        if (user != null && user.getPassword().equals(password)) {
            UserType userType = user.getType();

            if (userType == UserType.EMPLOYEE) {
                SwingUtilities.invokeLater(() -> {
                    mainGUI.dispose();
                    EmployeeController employeeController = new EmployeeController();
                });
            } else if (userType == UserType.ADMINISTRATOR) {
                SwingUtilities.invokeLater(() -> {
                    mainGUI.dispose();
                    AdministratorController administratorController = new AdministratorController(false);
                });
            }
        } else {
            mainGUI.showMessage("Login Error", "Invalid username or password.");
        }
    }

    private void showPlantImage() {
        int selectedPlantID = mainGUI.getSelectedPlantID();
        if (selectedPlantID != -1) {
            Plant selectedPlant = plantRepository.searchPlantByID(selectedPlantID);

            String imagePath = "images/" + selectedPlant.getName() + ".jpeg";
            ImageIcon imageIcon = new ImageIcon(imagePath);

            if (imageIcon.getIconWidth() == -1 || imageIcon.getIconHeight() == -1) {
                JOptionPane.showMessageDialog(mainGUI, "Failed to load plant image.", "Image Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JLabel imageLabel = new JLabel(imageIcon);

            JFrame imageFrame = new JFrame("Plant Image: " + selectedPlant.getName());
            imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            imageFrame.getContentPane().add(imageLabel, BorderLayout.CENTER);
            imageFrame.pack();
            imageFrame.setLocationRelativeTo(mainGUI);
            imageFrame.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(mainGUI, "Please select a plant.", "No Plant Selected", JOptionPane.WARNING_MESSAGE);
        }
    }
}