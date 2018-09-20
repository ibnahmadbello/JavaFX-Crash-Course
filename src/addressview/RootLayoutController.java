/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package addressview;

import addressapp.AddressApp;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;



/**
 * FXML Controller class
 *
 * @author ibnahmad
 */
public class RootLayoutController {

    // Reference to the main application
    private AddressApp mainApp;
    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(AddressApp addressApp){
        this.mainApp = addressApp;
    }
    
    /**
     * Creates an empty address book.
     */
    @FXML
    private void handleNew(){
        mainApp.getPersonData().clear();
        mainApp.setPersonFilePath(null);
    }
    
    /**
     * Opens a FileChooser to let the user select an address book to load.
     */
    @FXML
    private void handleOpen(){
        FileChooser fileChooser = new FileChooser();
        
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        
        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        
        if (file != null) {
            mainApp.loadPersonDataFromFile(file);
        }
    }
    
    /**
     * Saves the file to the person file that is currently open. If there is no
     * ope file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave(){
        File personFile = mainApp.getPersonFilePath();
        if (personFile != null) {
            mainApp.savePersonDataToFile(personFile);
        } else {
            handleSaveAs();
        }
    }
    
    /**
     * Opens a FileCHooser to let the user select a file to save to.
     */   
    @FXML
    private void handleSaveAs(){
        FileChooser fileChooser = new FileChooser();
        
        // Set extension filter
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extensionFilter);
        
        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        
        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.savePersonDataToFile(file);
        }
    }
    
    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("AddressApp");
        alert.setHeaderText("About");
        alert.setContentText("Author: Ibn Ahmad\nWebsite: http://code.makery.ch");
        
        alert.showAndWait();
    }
    
    /**
     * Closes the application
     */
    @FXML
    private void handleExit(){
        System.exit(0);
    }
    
}
