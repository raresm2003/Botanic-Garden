package View;

public interface IMainGUI extends IGUI {
    void ResetDgvPlantsList();
    void AddRowDgvPlantsList(Object[] row);
    void openEmployeeGUI();
    void openAdministratorGUI();
    void showMessage(String title, String message);
}
