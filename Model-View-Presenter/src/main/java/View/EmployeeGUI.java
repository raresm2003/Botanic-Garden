package View;

import Model.Plant;
import Model.Zone;
import Presenter.EmployeePresenter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeGUI extends JFrame implements IEmployeeGUI {

    private JTable table;
    private JScrollPane scrollPane;
    private final EmployeePresenter employeePresenter;

    private JTextField txtNameFilter;
    private JTextField txtSpeciesFilter;
    private JCheckBox chkCarnivoreFilter;
    private JComboBox<String> cmbZoneFilter;
    private JButton btnFilter;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnLogout;

    public EmployeeGUI() {
        super("Botanic Garden");
        this.employeePresenter = new EmployeePresenter(this);
        initComponents();
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

        btnFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilters();
            }
        });

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnLogout = new JButton("Logout");

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPlant();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePlant();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePlant();
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                SwingUtilities.invokeLater(() -> {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    JFrame frame = new MainGUI();
                    frame.setVisible(true);
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

        employeePresenter.bind(this);
        employeePresenter.populatePlantList();
    }

    private void applyFilters() {
        String nameFilter = txtNameFilter.getText().trim();
        String speciesFilter = txtSpeciesFilter.getText().trim();
        boolean carnivoreFilter = chkCarnivoreFilter.isSelected();
        String selectedZone = cmbZoneFilter.getSelectedItem().toString();
        Zone zoneFilter = selectedZone.equals("All Zones") ? null : Zone.valueOf(selectedZone);

        employeePresenter.applyFilters(nameFilter, speciesFilter, carnivoreFilter, zoneFilter);
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
            String name = nameField.getText();
            String species = speciesField.getText();
            boolean carnivore = carnivoreCheckbox.isSelected();
            Zone zone = (Zone) zoneComboBox.getSelectedItem();
            employeePresenter.addPlant(name, species, carnivore, zone.toString());
        }
    }

    private void updatePlant() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int plantID = (int) table.getValueAt(selectedRow, 0);
            Plant selectedPlant = employeePresenter.getSelectedPlant(plantID);

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
                employeePresenter.updatePlant(plantID, name, species, carnivore, zone.toString());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a plant to update.", "Update Plant", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deletePlant() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int plantID = (int) table.getValueAt(selectedRow, 0);
            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this plant?", "Delete Plant", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                employeePresenter.deletePlant(plantID);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a plant to delete.", "Delete Plant", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void SetMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void ResetDgvPlantsList() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
    }

    @Override
    public void AddRowDgvPlantsList(Object[] row) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(row);
    }
}