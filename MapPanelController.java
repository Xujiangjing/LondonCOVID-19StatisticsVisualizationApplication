import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class is responsible for handling user interactions with the map panel.
 * It updates the UI based on user actions and triggers updates in the
 * MapPanelAnalyzer. The controller manages the visualization of London boroughs
 * on the map, including their labeling and coloring based on COVID-19 mortality
 * rates. Each borough is represented as a button on the UI, which can be
 * interacted with to display detailed COVID data.
 *
 * 
 * @author Jiangjing Xu & Hongyuan Zhao & Lei Ding
 * @version 24.03.2024
 */
public class MapPanelController {
    @FXML
    private VBox mapPanelMain; // Main container for the map panel.

    @FXML
    private Pane mapPanel; // Pane where the map and borough buttons are displayed.

    @FXML
    // Button references for each London borough, allowing for interactive
    // visualization.
    private Button EnfieldBtn, HaringeyBtn, WalthamForestBtn, BarnetBtn, HarrowBtn, BrentBtn, CamdenBtn, IslingtonBtn,
            HackneyBtn, RedbridgeBtn, HaveringBtn, HillingdonBtn, EalingBtn, KensingtonAndChelseaBtn, WestminsterBtn,
            TowerHamletsBtn, NewhamBtn, BarkingAndDagenhamBtn, HounslowBtn, HammersmithAndFulhamBtn, WandsworthBtn,
            CityOfLondonBtn, GreenwichBtn, BexleyBtn, RichmondUponThamesBtn, MertonBtn, LambethBtn, SouthwarkBtn,
            LewishamBtn, KingstonUponThamesBtn, SuttonBtn, CroydonBtn, BromleyBtn;

    private MapPanelAnalyzer mapPanelAnalyzer; // Instance of MapPanelAnalyzer for data analysis and visualization.

    /**
     * Sets the MapPanelAnalyzer instance for this controller. This method prepares
     * the visualization of London's map with labeled and colored boroughs based on
     * COVID-19 data.
     * 
     * @param mapPanelAnalyzer The MapPanelAnalyzer to set.
     */
    public void setMapPanelAnalyzer(MapPanelAnalyzer mapPanelAnalyzer) {
        this.mapPanelAnalyzer = mapPanelAnalyzer;
    }

    /**
     * Finds a button corresponding to a given borough name.
     * 
     * @param boroughName The name of the borough.
     * @return The button corresponding to the borough, or null if not found.
     */
    private Button findButtonForBorough(String boroughName) {
        String buttonId = boroughName.replaceAll("\\s+", "_") + "Btn";
        return (Button) mapPanel.lookup("#" + buttonId);
    }

    /**
     * Updates the map visualization based on the selected date range. This involves
     * coloring or marking boroughs based on new analysis.
     * 
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     */
    public void updateMapVisualization(LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> deathCountsByBorough = mapPanelAnalyzer.updateDeathCountsByBorough(startDate, endDate);
        updateBoroughButtonStyles(deathCountsByBorough);
    }

    /**
     * Updates the styles of borough buttons based on death counts.
     * 
     * @param deathCountsByBorough A map of borough names to their death counts.
     */
    private void updateBoroughButtonStyles(Map<String, Integer> deathCountsByBorough) {
        deathCountsByBorough.forEach((boroughName, deathCount) -> {
            Button boroughButton = findButtonForBorough(boroughName);
            if (boroughButton != null) {
                String color = getColorForDeathCount(deathCount);
                boroughButton.setStyle("-fx-background-color: " + color + ";");
            }
        });
    }

    /**
     * Determines the color for a button based on the death count.
     * 
     * @param deathCount The death count for the borough.
     * @return A string representing the color for the button.
     */
    private String getColorForDeathCount(int deathCount) {
        if (deathCount < 10) {
            return "lightgreen";
        } else if (deathCount < 20) {
            return "forestgreen";
        } else if (deathCount < 30) {
            return "green";
        } else if (deathCount < 40) {
            return "darkgreen";
        } else if (deathCount < 50) {
            return "darkolivegreen";
        } else {
            return "red";
        }
    }

    /**
     * Handles click events on borough buttons, displaying detailed COVID data for
     * the selected borough. This method is called when a user clicks on a borough
     * button. It retrieves the COVID data for the selected borough and displays it
     * in a new window.
     * 
     * @param event The ActionEvent triggered by clicking a borough button.
     */
    public void handleBoroughClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String boroughName = clickedButton.getId().replace("Btn", "").replace("_", " ");
        List<CovidData> boroughData = mapPanelAnalyzer.getCovidDataForBorough(boroughName);

        showBoroughDataWindow(boroughName, boroughData);
    }

    /**
     * Opens a new window displaying detailed COVID data for a specific borough.
     * This window provides insights into COVID-19 statistics within the borough,
     * such as case counts and death rates over the selected period.
     * 
     * @param boroughName The name of the borough for which data is being displayed.
     * @param boroughData A list of CovidData objects containing the data for the
     *                    borough.
     */
    private void showBoroughDataWindow(String boroughName, List<CovidData> boroughData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BoroughDataWindow.fxml"));
            Pane root = loader.load();

            BoroughDataController controller = loader.getController();

            controller.setCovidData(boroughData);

            Stage stage = new Stage();
            stage.setTitle("COVID Data - " + boroughName);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: Borough Data Window is not initialized.");
        }
    }

    /**
     * Returns the main VBox container of the map panel. This method is used to
     * retrieve the VBox container for operations such as updating or replacing its
     * content.
     * 
     * @return The VBox container of the map panel.
     */
    public VBox getView() {
        return mapPanelMain;
    }
}