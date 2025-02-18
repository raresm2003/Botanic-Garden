package View;

import Model.UserType;
import Model.Zone;
import ViewModel.PlantViewModel;
import ViewModel.UserViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class AdministratorGUI extends JFrame {

    private final PlantViewModel plantViewModel;
    private final UserViewModel userViewModel;

    private JTable table;
    private JScrollPane scrollPane;

    private JTextField txtNameFilter;
    private JTextField txtSpeciesFilter;
    private JCheckBox chkCarnivoreFilter;
    private JComboBox<String> cmbZoneFilter;
    private JComboBox<String> cmbTypeFilter;
    private JButton btnFilter;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnLogout;
    private JButton btnUsers;
    private JButton btnPlants;
    private JButton btnSavePlantList;

    private boolean userOn;

    public AdministratorGUI(boolean userOn) {
        super("Botanic Garden");
        this.userOn = userOn;
        this.plantViewModel = new PlantViewModel();
        this.userViewModel = new UserViewModel();
        initComponents();
        if(!userOn)
            bindViewModelPlants();
        else
            bindViewModelUsers();
    }

    private void initComponents() {
        DefaultTableModel model = new DefaultTableModel();
        if(!userOn) {
            model.setColumnIdentifiers(new String[]{"Plant ID", "Name", "Species", "Carnivore", "Zone"});

            table = new JTable(model) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            table.getColumnModel().getColumn(0).setPreferredWidth(80);
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
            table.getColumnModel().getColumn(2).setPreferredWidth(200);
            table.getColumnModel().getColumn(3).setPreferredWidth(80);
            table.getColumnModel().getColumn(4).setPreferredWidth(150);

            scrollPane = new JScrollPane(table);

            txtNameFilter = new JTextField(13);
            txtSpeciesFilter = new JTextField(13);
            chkCarnivoreFilter = new JCheckBox("Carnivore");
            cmbZoneFilter = new JComboBox<>();
            cmbZoneFilter.addItem("All Zones");
            for (Zone zone : Zone.values()) {
                cmbZoneFilter.addItem(zone.toString());
            }
            cmbZoneFilter.setSelectedIndex(0);
            btnFilter = new JButton("Filter");

            btnAdd = new JButton("Add");
            btnUpdate = new JButton("Update");
            btnDelete = new JButton("Delete");
            btnLogout = new JButton("Logout");
            btnUsers = new JButton("Users");
            btnSavePlantList = new JButton("Save Plant List");

            btnLogout.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            btnSavePlantList.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    savePlantList();
                }
            });

            btnUsers.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();

                    SwingUtilities.invokeLater(() -> {
                        try {
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        JFrame administratorGUI = new AdministratorGUI(true);
                        administratorGUI.setVisible(true);
                    });
                }
            });

            JPanel filterPanel = new JPanel();
            filterPanel.setLayout(new FlowLayout());
            filterPanel.add(new JLabel("Name:"));
            filterPanel.add(txtNameFilter);
            filterPanel.add(new JLabel("Species:"));
            filterPanel.add(txtSpeciesFilter);
            filterPanel.add(chkCarnivoreFilter);
            filterPanel.add(new JLabel("Zone:"));
            filterPanel.add(cmbZoneFilter);
            filterPanel.add(btnFilter);
            filterPanel.add(btnLogout);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            buttonPanel.add(btnAdd);
            buttonPanel.add(btnUpdate);
            buttonPanel.add(btnDelete);
            buttonPanel.add(btnSavePlantList);
            buttonPanel.add(btnUsers);

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(filterPanel, BorderLayout.NORTH);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            setLayout(new BorderLayout());
            add(mainPanel, BorderLayout.CENTER);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 400);
            setLocationRelativeTo(null);
            setVisible(true);
        } else {
            model.setColumnIdentifiers(new String[]{"User ID", "Name", "Type"});

            table = new JTable(model) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            table.getColumnModel().getColumn(0).setPreferredWidth(100);
            table.getColumnModel().getColumn(1).setPreferredWidth(300); // Adjusted width for name
            table.getColumnModel().getColumn(2).setPreferredWidth(230);

            scrollPane = new JScrollPane(table);

            txtNameFilter = new JTextField(13);
            cmbTypeFilter = new JComboBox<>();
            cmbTypeFilter.addItem("All Types");
            for (UserType type : UserType.values()) {
                cmbTypeFilter.addItem(type.toString());
            }
            cmbTypeFilter.setSelectedIndex(0);
            btnFilter = new JButton("Filter");

            btnAdd = new JButton("Add");
            btnUpdate = new JButton("Update");
            btnDelete = new JButton("Delete");
            btnLogout = new JButton("Logout");
            btnPlants = new JButton("Plants");

            btnLogout.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose(); // Close the AdministratorGUI window
                }
            });

            btnPlants.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();

                    SwingUtilities.invokeLater(() -> {
                        try {
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        JFrame administratorGUI = new AdministratorGUI(false);
                        administratorGUI.setVisible(true);
                    });
                }
            });

            JPanel filterPanel = new JPanel();
            filterPanel.setLayout(new FlowLayout());
            filterPanel.add(new JLabel("Name:"));
            filterPanel.add(txtNameFilter);
            filterPanel.add(new JLabel("Type:"));
            filterPanel.add(cmbTypeFilter);
            filterPanel.add(btnFilter);
            filterPanel.add(btnLogout);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            buttonPanel.add(btnAdd);
            buttonPanel.add(btnUpdate);
            buttonPanel.add(btnDelete);
            buttonPanel.add(btnPlants);

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(filterPanel, BorderLayout.NORTH);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            setLayout(new BorderLayout());
            add(mainPanel, BorderLayout.CENTER);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 400);
            setLocationRelativeTo(null);
            setVisible(true);
        }

    }

    private void bindViewModelPlants() {
        // Set the table model
        table.setModel(plantViewModel.plantsTable);
        plantViewModel.populatePlantListCommand.execute();

        // Action listener for the filter button
        btnFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update filter values when the filter button is pressed
                plantViewModel.setNameFilter(txtNameFilter.getText().trim());
                plantViewModel.setSpeciesFilter(txtSpeciesFilter.getText().trim());
                plantViewModel.setCarnivoreFilter(chkCarnivoreFilter.isSelected());
                String selectedZone = Objects.requireNonNull(cmbZoneFilter.getSelectedItem()).toString();
                Zone zoneFilter = selectedZone.equals("All Zones") ? null : Zone.valueOf(selectedZone);
                plantViewModel.setZoneFilter(zoneFilter);

                // Execute the filter command
                plantViewModel.filterPlantsCommand.execute();
            }
        });

        // Action listener for the add button
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPlant();
                plantViewModel.populatePlantListCommand.execute();
            }
        });

        // Action listener for the update button
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePlant();
                plantViewModel.populatePlantListCommand.execute();
            }
        });

        // Action listener for the delete button
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePlant();
                plantViewModel.populatePlantListCommand.execute();
            }
        });
    }

    private void bindViewModelUsers(){
        // Set the table model
        table.setModel(userViewModel.usersTable);
        userViewModel.populateUserListCommand.execute();

        // Action listener for the filter button
        btnFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update filter values when the filter button is pressed
                userViewModel.setNameFilter(txtNameFilter.getText().trim());
                String selectedType = Objects.requireNonNull(cmbTypeFilter.getSelectedItem()).toString();
                UserType typeFilter = selectedType.equals("All Zones") ? null : UserType.valueOf(selectedType);
                userViewModel.setTypeFilter(typeFilter);

                // Execute the filter command
                userViewModel.filterUsersCommand.execute();
            }
        });

        // Action listener for the add button
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
                userViewModel.populateUserListCommand.execute();
            }
        });

        // Action listener for the update button
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
                userViewModel.populateUserListCommand.execute();
            }
        });

        // Action listener for the delete button
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
                userViewModel.populateUserListCommand.execute();
            }
        });
    }

    private void addPlant() {
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
            plantViewModel.setPlantName(nameField.getText());
            plantViewModel.setSpecies(speciesField.getText());
            plantViewModel.setCarnivore(carnivoreCheckbox.isSelected());
            plantViewModel.setZone((Zone) zoneComboBox.getSelectedItem());

            plantViewModel.addPlantCommand.execute();
        }
    }

    private void updatePlant() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            plantViewModel.setSelectedRowIndex(selectedRow);

            JTextField nameField = new JTextField(table.getValueAt(selectedRow, 1).toString());
            JTextField speciesField = new JTextField(table.getValueAt(selectedRow, 2).toString());
            JCheckBox carnivoreCheckbox = new JCheckBox("Carnivore", (boolean) table.getValueAt(selectedRow, 3));
            JComboBox<Zone> zoneComboBox = new JComboBox<>(Zone.values());
            zoneComboBox.setSelectedItem(table.getValueAt(selectedRow, 4));

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
                plantViewModel.setPlantName(nameField.getText());
                plantViewModel.setSpecies(speciesField.getText());
                plantViewModel.setCarnivore(carnivoreCheckbox.isSelected());
                plantViewModel.setZone((Zone) zoneComboBox.getSelectedItem());

                plantViewModel.updatePlantCommand.execute();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a plant to update.", "Update Plant", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deletePlant() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            plantViewModel.setSelectedRowIndex(selectedRow);
            plantViewModel.deletePlantCommand.execute();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a plant to delete.", "Delete Plant", JOptionPane.WARNING_MESSAGE);
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
            userViewModel.setUserName(nameField.getText());
            userViewModel.setPassword(passwordField.getText());
            userViewModel.setType(UserType.valueOf(typeComboBox.getSelectedItem().toString()));

            userViewModel.addUserCommand.execute();
        }
    }

    private void updateUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            userViewModel.setSelectedRowIndex(selectedRow);

            JTextField nameField = new JTextField(table.getValueAt(selectedRow, 1).toString());
            JTextField passwordField = new JTextField();

            JComboBox<UserType> typeComboBox = new JComboBox<>(UserType.values());
            typeComboBox.setSelectedItem(table.getValueAt(selectedRow, 2));

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
                userViewModel.setUserName(nameField.getText());
                userViewModel.setPassword(passwordField.getText());
                userViewModel.setType((UserType) typeComboBox.getSelectedItem());

                userViewModel.updateUserCommand.execute();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to update.", "Update User", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            userViewModel.setSelectedRowIndex(selectedRow);
            userViewModel.deleteUserCommand.execute();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Delete User", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void savePlantList() {

        JComboBox<String> formatComboBox;
        String[] formats = {"CSV", "JSON", "XML", "DOC"};
        formatComboBox = new JComboBox<>(formats);

        Object[] message = {"File format:", formatComboBox};

        int option = JOptionPane.showConfirmDialog(this, message, "Save Plant List", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            plantViewModel.setFileType((String) formatComboBox.getSelectedItem());

            plantViewModel.savePlantListCommand.execute();
        }
    }

}