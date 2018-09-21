/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package addressapp;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.collections.*;
import addressmodel.Person;
import addressmodel.PersonListWrapper;
import addressview.BirthdayStatisticsController;
import addressview.PersonEditDialogController;
import addressview.PersonOverviewController;
import addressview.RootLayoutController;
import java.io.File;
import java.util.prefs.Preferences;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author ibnahmad
 */
public class AddressApp extends Application {
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    
    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<Person> personData = FXCollections.observableArrayList();
    
    /**
     * Constructor     
     */
    public AddressApp(){
        // Add some sample data
        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));
    }
    
    /**
     * Returns the data as an observable list of Persons.
     * @return 
     */
    public ObservableList<Person> getPersonData(){
        return personData;
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");
        
        // Set the application icon.
        this.primaryStage.getIcons().add(new Image("/resources/image/address_book.png"));
        
        initRootLayout();
        
        showPersonOverview();
    }
    
    /**
     * Initializes the root layout and tries to load the last opened
     * person file
     */
    public void initRootLayout(){
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AddressApp.class.getResource("/addressview/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            
            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Try to load last opened person file
        File file = getPersonFilePath();
        if (file != null) {
            loadPersonDataFromFile(file);
        }
    }
    
    /**
     * Shows the person overview inside the root layout.     *
     */
    public void showPersonOverview(){
        try {
            // load person overview
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AddressApp.class.getResource("/addressview/PersonOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            
            // Set person overview into the center of root layout
            rootLayout.setCenter(personOverview);
            
            // Give the controller access to the Main app
            PersonOverviewController controller = loader.getController();
            controller.setAddressApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Loads person data from the specified file. The current person data will
     * be replaced.
     * 
     * @param file
     */
    public void loadPersonDataFromFile(File file){
        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            
            // Reading XML from the file and unmarshalling.
            PersonListWrapper wrapper = (PersonListWrapper) unmarshaller.unmarshal(file);
            
            personData.clear();
            personData.addAll(wrapper.getPersons());
            
            // Save the file path to the registry.
            setPersonFilePath(file);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());
            
            alert.showAndWait();
        }
    }
    
    /**
     * Saves the current person data to the specified file
     * 
     * @param file
     */
    public void savePersonDataToFile(File file){
        try {
            JAXBContext jAXBContext = JAXBContext.newInstance(PersonListWrapper.class);
            Marshaller marshaller = jAXBContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            // Wrapping our person data
            PersonListWrapper personListWrapper = new PersonListWrapper();
            personListWrapper.setPersons(personData);
            
            // Marshalling and saving XML to the file.
            marshaller.marshal(personListWrapper, file);
            
            // Save the file path to the registry.
            setPersonFilePath(file);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());
            
            alert.showAndWait();
        }
    }
    
    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     * 
     * @param person the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showPersonEditDialog(Person person){
        try {
            // Load the fxml file and create a new stage for the popup dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AddressApp.class.getResource("/addressview/PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            // Create the dialog stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            // Set the person into the controller.
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);
            
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            
            return controller.isOkClicked();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Returns the person file preference, i.e the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     * 
     * @return
     */
    public File getPersonFilePath(){
        Preferences prefs = Preferences.userNodeForPackage(AddressApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }
    
    /**
     * Sets the file path of the currently loaded file. THe path is persisted in
     * the OS specific registry.
     * 
     * @param file the file or null to remove the path
     */
    public void setPersonFilePath(File file){
        Preferences preferences = Preferences.userNodeForPackage(AddressApp.class);
        if (file != null) {
            preferences.put("filePath", file.getPath());
            
            // Update the stage title.
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            preferences.remove("filePath");
            
            // Update the stage title.
            primaryStage.setTitle("AddressApp");
        }
    }
    
    /**
     * Opens a dialog to show birthday statistics
     */
    public void showBirthdayStatistics(){
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AddressApp.class.getResource("/addressview/BirthdayStatistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            // Set the persons into the controller.
            BirthdayStatisticsController controller = loader.getController();
            controller.setPersonData(personData);
            
            dialogStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage(){
        return primaryStage;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
