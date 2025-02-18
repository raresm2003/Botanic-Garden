package View;

import Model.Zone;
import ViewModel.PlantViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class EmployeeGUI extends JFrame {

    private final PlantViewModel plantViewModel;

    private JTable table;
    private JScrollPane scrollPane;

    private JTextField txtNameFilter;
    private JTextField txtSpeciesFilter;
    private JCheckBox chkCarnivoreFilter;
    private JComboBox<String> cmbZoneFilter;
    private JButton btnFilter;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnLogout;
    private JButton btnSavePlantList;

    public EmployeeGUI() {
        super("Botanic Garden");
        this.plantViewModel = new PlantViewModel();
        initComponents();
        bindViewModel();
    }

    private void initComponents() {
        DefaultTableModel model = new DefaultTableModel();
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
        btnSavePlantList = new JButton("Save Plant List");

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // EmployeeGUI window
            }
        });

        btnSavePlantList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePlantList();
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

    private void bindViewModel() {
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
            zoneComboBox.setSelectedItem((Zone) table.getValueAt(selectedRow, 4));

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