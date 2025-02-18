package Communication;

import Notification.Notification;
import Services.IPlantService;
import Services.IUserService;
import Services.PlantService;
import Services.UserService;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private IPlantService plantService;
    private IUserService userService;
    private Notification notification;

    public ClientHandler(Socket socket) {
        this.plantService = new PlantService();
        this.userService = new UserService();
        this.notification = new Notification();
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    private void brodcastMessage(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                String[] splited = messageFromClient.split("#");
                System.out.println(messageFromClient);

                if(splited[0].equals("startPlant")){
                    String plantData = plantService.getPlantList();
                    this.brodcastMessage(plantData);
                }

                if(splited[0].equals("addPlant")){
                    plantService.addPlant(splited[1], splited[2], splited[3], splited[4], splited[5]);
                    String plantData = plantService.getPlantList();
                    this.brodcastMessage(plantData);
                }

                if(splited[0].equals("deletePlant")){
                    plantService.deletePlant(splited[1]);
                    String plantData = plantService.getPlantList();
                    this.brodcastMessage(plantData);
                }

                if(splited[0].equals("updatePlant")){
                    plantService.updatePlant(splited[1], splited[2], splited[3], splited[4], splited[5], splited[6]);
                    String plantData = plantService.getPlantList();
                    this.brodcastMessage(plantData);
                }

                if(splited[0].equals("filterPlants")){
                    String plantData = plantService.filterPlants(splited[1], splited[2], splited[3], splited[4]);
                    this.brodcastMessage(plantData);
                }

                if(splited[0].equals("selectPlant")){
                    String plantData = plantService.searchPlantByID(splited[1]);
                    this.brodcastMessage(plantData);
                }

                if(splited[0].equals("showPlantImage")){
                    String plantData = plantService.getPlantImage(splited[1]);
                    this.brodcastMessage(plantData);
                }

                if (splited[0].equals("startUser")) {
                    String userData = userService.getUserList();
                    this.brodcastMessage(userData);
                }

                if (splited[0].equals("addUser")) {
                    userService.addUser(splited[1], splited[2], splited[3], splited[4], splited[5]);
                    String userData = userService.getUserList();
                    this.brodcastMessage(userData);
                }

                if (splited[0].equals("deleteUser")) {
                    userService.deleteUser(splited[1]);
                    String userData = userService.getUserList();
                    this.brodcastMessage(userData);
                }

                if (splited[0].equals("updateUser")) {
                    userService.updateUser(splited[1], splited[2], splited[3], splited[4], splited[5], splited[6]);
                    String userData = userService.getUserList();
                    this.brodcastMessage(userData);

                    String text = "" +
                            "Atentie!\n" +
                            "Credentialele tale de logare au fost schimbate!\n" +
                            "Pentru mai multe informatii contactati administratorul.\n"+
                            "Noile credentiale sunt: Username: "+ splited[2]+" Parola: "+ userService.getUserPassword(splited[2]) +".";
                    this.notification.send(text, splited[5], splited[6]);
                }

                if (splited[0].equals("filterUsers")) {
                    String userData = userService.filterUsers(splited[1], splited[2]);
                    this.brodcastMessage(userData);
                }

                if (splited[0].equals("selectUserByID")) {
                    String userData = userService.searchUserByID(splited[1]);
                    this.brodcastMessage(userData);
                }

                if (splited[0].equals("selectUserByName")) {
                    String userData = userService.searchUserByName(splited[1]);
                    this.brodcastMessage(userData);
                }

            } catch (IOException e) {
                closeAll(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

}