package View;

public interface IAdministratorGUI extends IGUI {
    void ResetDgvPlantsList();
    void AddRowDgvPlantsList(Object[] row);
    void ResetDgvUsersList();
    void AddRowDgvUsersList(Object[] row);
}