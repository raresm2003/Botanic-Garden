package View;

import Model.Zone;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;

public class EmployeeGUI extends JFrame {

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
    private JButton btnStatistics;
    private JButton btnImage;

    public EmployeeGUI() {
        super("Botanic Garden");
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

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnLogout = new JButton("Logout");
        btnSavePlantList = new JButton("Save Plant List");
        btnStatistics = new JButton("Statistics");
        btnImage = new JButton("Image");

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
        buttonPanel.add(btnStatistics);
        buttonPanel.add(btnImage);

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

    public String getTxtNameFilter() {
        return txtNameFilter.getText().trim();
    }

    public String getTxtSpeciesFilter() {
        return txtSpeciesFilter.getText().trim();
    }

    public boolean isChkCarnivoreFilterSelected() {
        return chkCarnivoreFilter.isSelected();
    }

    public String getSelectedZone() {
        return Objects.requireNonNull(cmbZoneFilter.getSelectedItem()).toString();
    }

    public int getSelectedPlantID(){
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int plantID = (int) table.getValueAt(selectedRow, 0);
            return plantID;
        }
        else
            return -1;
    }

    public void resetDgvPlantsList() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
    }

    public void addRowDgvPlantsList(Object[] row) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(row);
    }

    public void addBtnFilterListener(ActionListener listener) {
        this.btnFilter.addActionListener(listener);
    }

    public void addBtnAddListener(ActionListener listener) {
        this.btnAdd.addActionListener(listener);
    }

    public void addBtnUpdateListener(ActionListener listener) {
        this.btnUpdate.addActionListener(listener);
    }

    public void addBtnDeleteListener(ActionListener listener) {
        this.btnDelete.addActionListener(listener);
    }

    public void addBtnSavePlantListListener(ActionListener listener){
        this.btnSavePlantList.addActionListener(listener);
    }

    public void addBtnStatisticListener(ActionListener listener) {
        this.btnStatistics.addActionListener(listener);
    }

    public void addBtnImageListener(ActionListener listener) {
        this.btnImage.addActionListener(listener);
    }

    public void addBtnLogoutListener(ActionListener listener) {
        this.btnLogout.addActionListener(listener);
    }

    public void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}