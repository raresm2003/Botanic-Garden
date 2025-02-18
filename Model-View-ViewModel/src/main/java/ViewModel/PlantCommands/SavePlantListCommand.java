package ViewModel.PlantCommands;

import Model.Plant;
import Model.Repository.PlantRepository;
import ViewModel.ICommand;
import ViewModel.PlantViewModel;

import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xwpf.usermodel.*;
import java.io.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class SavePlantListCommand implements ICommand {
    private final PlantViewModel plantViewModel;

    public SavePlantListCommand(PlantViewModel plantViewModel) {
        this.plantViewModel = plantViewModel;
    }

    @Override
    public void execute() {
        PlantRepository plantRepository = new PlantRepository();
        List<Plant> allPlants = plantRepository.getPlantList();
        String fileType = plantViewModel.getFileType();

        switch (fileType) {
            case "CSV":
                String csvFileName = "plants.csv";
                try (FileWriter writer = new FileWriter(csvFileName)) {
                    // Writing CSV header
                    writer.append("ID,Name,Species,Carnivore,Zone\n");
                    // Writing plant data
                    for (Plant plant : allPlants) {
                        writer.append(Integer.toString(plant.getPlantID()))
                                .append(",")
                                .append(plant.getName())
                                .append(",")
                                .append(plant.getSpecies())
                                .append(",")
                                .append(Boolean.toString(plant.isCarnivore()))
                                .append(",")
                                .append(plant.getZone().toString())
                                .append("\n");
                    }
                    System.out.println("CSV file saved successfully.");
                } catch (IOException e) {
                    System.out.println("An error occurred while saving CSV file.");
                    e.printStackTrace();
                }
                break;
            case "JSON":
                String jsonFileName = "plants.json";
                try (FileWriter writer = new FileWriter(jsonFileName)) {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(writer, allPlants);
                    System.out.println("JSON file saved successfully.");
                } catch (IOException e) {
                    System.out.println("An error occurred while saving JSON file.");
                    e.printStackTrace();
                }
                break;
            case "XML":
                String xmlFileName = "plants.xml";
                try {
                    JAXBContext jaxbContext = JAXBContext.newInstance(Plant.class);
                    Marshaller marshaller = jaxbContext.createMarshaller();
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    marshaller.marshal(allPlants, new File(xmlFileName));
                    System.out.println("XML file saved successfully.");
                } catch (JAXBException e) {
                    System.out.println("An error occurred while saving XML file.");
                    e.printStackTrace();
                }
                break;
            case "DOC":
                String docFileName = "plants.doc";
                try (FileOutputStream out = new FileOutputStream(docFileName);
                     XWPFDocument document = new XWPFDocument()) {
                    // Creating paragraph for each plant
                    for (Plant plant : allPlants) {
                        XWPFParagraph paragraph = document.createParagraph();
                        // Adding text to paragraph for each plant
                        paragraph.createRun().setText(plant.getPlantID() + ", " + plant.getName() + ", " + plant.getSpecies() + ", " + plant.isCarnivore() + ", " + plant.getZone());
                        // Add a newline (Enter) after each plant entry
                        paragraph.createRun().addBreak();
                    }
                    // Writing the document to file
                    document.write(out);
                    System.out.println("DOC file saved successfully.");
                } catch (IOException e) {
                    System.out.println("An error occurred while creating or saving DOC document.");
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Unsupported file type");
                break;
        }
    }
}