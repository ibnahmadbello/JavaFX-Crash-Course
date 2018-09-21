/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package addressview;

import addressmodel.Person;
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;



/**
 * FXML Controller class
 *
 * @author ibnahmad
 */
public class BirthdayStatisticsController {
    
    @FXML
    private BarChart<String, Integer> barChart;
    
    @FXML
    private CategoryAxis xAxis;
    
    private ObservableList<String> monthNames = FXCollections.observableArrayList();
    

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    public void initialize() {
        // Get an array with the English month names.
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        // Convert it to a list and ad it to our ObservableList of months.
        monthNames.addAll(Arrays.asList(months));
        
        // Assign the month names as categories for the horizontal axis
        xAxis.setCategories(monthNames);
    }
    
    /**
     * Sets the persons to show the statistics for.
     * 
     * @param persons
     */
    public void setPersonData(List<Person> persons){
        // Count the number of people having their birthday in a specific month.
        int[] monthCounter = new int[12];
        for (Person person : persons) {
            int month = person.getBirthday().getMonthValue() - 1;
            monthCounter[month]++;
        }
        
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        
        // Create a XYChart.Data object for each month. Add it to the series.
        for (int i = 0; i < monthCounter.length; i++) {
            series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));
        }
        
        barChart.getData().add(series);
    }
    
}
