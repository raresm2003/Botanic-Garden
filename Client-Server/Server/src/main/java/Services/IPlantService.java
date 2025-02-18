package Services;

public interface IPlantService {

    boolean addPlant(String plantInfo1, String plantInfo2, String plantInfo3, String plantInfo4, String plantInfo5);

    boolean deletePlant(String message);

    boolean updatePlant(String plantInfo0, String plantInfo1, String plantInfo2, String plantInfo3, String plantInfo4, String plantInfo5);

    String getPlantList();

    String searchPlantByID(String message);

    String filterPlants(String plantInfo1, String plantInfo2, String plantInfo3, String plantInfo4);

    String getPlantImage(String message);
}
