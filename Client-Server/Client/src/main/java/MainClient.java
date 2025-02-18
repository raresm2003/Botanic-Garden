import Communication.Client;
import Controller.MainController;

public class MainClient {
    public static void main(String[] args) {
        Client client = new Client();
        client.connectClient();
        MainController mainController = new MainController(client);
    }
}