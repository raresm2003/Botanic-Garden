package View;

import Model.Zone;
import Presenter.MainPresenter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MainGUI extends JFrame implements IMainGUI {

    private JTable table;
    private JScrollPane scrollPane;
    private final MainPresenter mainPresenter;

    private JTextField txtNameFilter;
    private JTextField txtSpeciesFilter;
    private JCheckBox chkCarnivoreFilter;
    private JComboBox<String> cmbZoneFilter;
    private JButton btnFilter;

    private JButton btnLogin;

    public MainGUI() {
        super("Botanic Garden");
        this.mainPresenter = new MainPresenter(this);
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

        // Initialize filtering components
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

        btnFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilters();
            }
        });

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

        mainPresenter.bind(this);
        mainPresenter.populatePlantList();
    }

    private void applyFilters() {
        String nameFilter = txtNameFilter.getText().trim();
        String speciesFilter = txtSpeciesFilter.getText().trim();
        boolean carnivoreFilter = chkCarnivoreFilter.isSelected();
        String selectedZone = Objects.requireNonNull(cmbZoneFilter.getSelectedItem()).toString();
        Zone zoneFilter = selectedZone.equals("All Zones") ? null : Zone.valueOf(selectedZone);

        mainPresenter.applyFilters(nameFilter, speciesFilter, carnivoreFilter, zoneFilter);
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
            String username = txtUsername.getText();
            char[] password = txtPassword.getPassword();
            mainPresenter.authenticateUser(username, new String(password));
        }
    }

    @Override
    public void openEmployeeGUI() {
        SwingUtilities.invokeLater(() -> {
            EmployeeGUI employeeGUI = new EmployeeGUI();
            employeeGUI.setVisible(true);
            dispose();
        });
    }

    @Override
    public void openAdministratorGUI() {
        SwingUtilities.invokeLater(() -> {
            AdministratorGUI administratorGUI = new AdministratorGUI(false);
            administratorGUI.setVisible(true);
            dispose();
        });
    }

    @Override
    public void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
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