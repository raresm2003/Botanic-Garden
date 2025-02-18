package View;

import Model.Zone;
import ViewModel.PlantViewModel;
import ViewModel.UserViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MainGUI extends JFrame{

    private final PlantViewModel plantViewModel;
    private final UserViewModel userViewModel;

    private JTable table;
    private JScrollPane scrollPane;

    private JTextField txtNameFilter;
    private JTextField txtSpeciesFilter;
    private JCheckBox chkCarnivoreFilter;
    private JComboBox<String> cmbZoneFilter;
    private JButton btnFilter;
    private JButton btnLogin;

    public MainGUI() {
        super("Botanic Garden");
        this.plantViewModel = new PlantViewModel();
        this.userViewModel = new UserViewModel();
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

        txtNameFilter = new JTextField(14);
        txtSpeciesFilter = new JTextField(14);
        chkCarnivoreFilter = new JCheckBox("Carnivore");
        cmbZoneFilter = new JComboBox<>();
        cmbZoneFilter.addItem("All Zones");
        for (Zone zone : Zone.values()) {
            cmbZoneFilter.addItem(zone.toString());
        }
        cmbZoneFilter.setSelectedIndex(0);
        btnFilter = new JButton("Filter");
        btnLogin = new JButton("Login");

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginDialog();
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
        filterPanel.add(btnLogin);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

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

                plantViewModel.filterPlantsCommand.execute();
            }
        });
    }

    private void showLoginDialog() {
        JTextField txtUsername = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        Object[] message = {
                "Name:", txtUsername,
                "Password:", txtPassword
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            userViewModel.setUserName(txtUsername.getText());
            userViewModel.setPassword(new String(txtPassword.getPassword()));

            userViewModel.loginCommand.execute();
        }
    }
}