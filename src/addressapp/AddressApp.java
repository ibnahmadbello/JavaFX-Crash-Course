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
import addressview.PersonOverviewController;

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
        
        initRootLayout();
        
        showPersonOverview();
    }
    
    /**
     * Initializes the root layout
     */
    public void initRootLayout(){
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AddressApp.class.getResource("/addressview/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
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
