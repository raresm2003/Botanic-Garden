package Controller;

import View.MainGUI;
import Communication.Client;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MainController {
    private final MainGUI mainGUI;
    private Client client;

    public MainController(Client client) {
        this.mainGUI = new MainGUI();
        this.client = client;
        initialize();
    }

    private void initialize() {
        mainGUI.addBtnFilterListener(e -> applyFilters());

        mainGUI.addBtnImageListener(e -> showPlantImage());

        mainGUI.addBtnLoginListener(e -> showLoginDialog());

        populatePlantList();
    }

    private void populatePlantList() {
        client.sendMessage("startPlant");
        String message = client.listenForMessage();

        try {
            if (message != null) {
                mainGUI.resetDgvPlantsList();
                String[] plants = message.split("#");
                for (int i = 0; i < plants.length; i += 5) {
                    mainGUI.addRowDgvPlantsList(new Object[]{plants[i], plants[i+1], plants[i+2], plants[i+3], plants[i+4]});
                }
            } else {
                mainGUI.showMessage("Empty", "The list of plants is empty!");
            }
        } catch (Exception exception) {
            mainGUI.showMessage("Empty", "The list of plants is empty!");
        }
    }

    private void applyFilters() {
        String nameFilter = mainGUI.getTxtNameFilter();
        String speciesFilter = mainGUI.getTxtSpeciesFilter();
        String carnivoreFilter = String.valueOf(mainGUI.isChkCarnivoreFilterSelected());
        String zoneFilter = mainGUI.getSelectedZone();

        client.sendMessage("filterPlants#" + nameFilter + "#" + speciesFilter + "#" + carnivoreFilter + "#" + zoneFilter);
        String message = client.listenForMessage();

        try {
            if (message != null) {
                mainGUI.resetDgvPlantsList();
                String[] plants = message.split("#");
                for (int i = 0; i < plants.length; i += 5) {
                    mainGUI.addRowDgvPlantsList(new Object[]{plants[i], plants[i+1], plants[i+2], plants[i+3], plants[i+4]});
                }
            } else {
                mainGUI.showMessage("Empty", "No plants match the filters!");
            }
        } catch (Exception exception) {
            mainGUI.showMessage("Empty", "No plants match the filters!");
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
        client.sendMessage("selectUserByName#" + username);
        String userData = client.listenForMessage();
        String[] userDataArray = userData.split("#");

        if (userDataArray[1].equals(password)) {
            if (userDataArray[2].equals("EMPLOYEE")) {
                SwingUtilities.invokeLater(() -> {
                    mainGUI.dispose();
                    EmployeeController employeeController = new EmployeeController(this.client);
                });
            } else if (userDataArray[2].equals("ADMINISTRATOR")) {
                SwingUtilities.invokeLater(() -> {
                    mainGUI.dispose();
                    AdministratorController administratorController = new AdministratorController(false, this.client);
                });
            }
        } else {
            mainGUI.showMessage("Login Error", "Invalid username or password.");
        }
    }

    private void showPlantImage() {
        int selectedPlantID = mainGUI.getSelectedPlantID();
        if (selectedPlantID != -1) {
            client.sendMessage("showPlantImage#" + selectedPlantID);
            String message = client.listenForMessage();

            if (message.equals("null")) {
                mainGUI.showMessage("Empty", "The selected plant has no image!");
            } else {
                try {
                    URL url = new URL(message);
                    ImageIcon imageIcon = new ImageIcon(url);

                    if (imageIcon.getIconWidth() == -1 || imageIcon.getIconHeight() == -1) {
                        JOptionPane.showMessageDialog(mainGUI, "Failed to load plant image.", "Image Error", JOptionPane.ERROR_MESSAGE);
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

                    JFrame imageFrame = new JFrame("Plant Image: " + mainGUI.getTableValueAt(1));
                    imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    imageFrame.getContentPane().add(imageLabel, BorderLayout.CENTER);
                    imageFrame.pack();
                    imageFrame.setLocationRelativeTo(mainGUI);
                    imageFrame.setVisible(true);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(mainGUI, "Failed to load plant image from the internet.", "Image Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(mainGUI, "Please select a plant.", "No Plant Selected", JOptionPane.WARNING_MESSAGE);
        }
    }
}
