import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Manages the UI for displaying COVID-19 data for a specific borough, allowing
 * users to view detailed statistics and sort them based on various criteria
 * such as date, new cases, total cases, and new deaths.
 *
 * @author Jiangjing Xu & Hongyuan Zhao & Lei Ding
 * @version 24.03.2024
 */

public class BoroughDataController {
    @FXML
    private TableView<CovidData> tableView;

    @FXML
    private TableColumn<CovidData, LocalDate> dateColumn;

    @FXML
    private TableColumn<CovidData, Integer> retailRecreationColumn;

    @FXML
    private TableColumn<CovidData, Integer> groceryPharmacyColumn;

    @FXML
    private TableColumn<CovidData, Integer> parksColumn;

    @FXML
    private TableColumn<CovidData, Integer> transitStationsColumn;

    @FXML
    private TableColumn<CovidData, Integer> workplacesColumn;

    @FXML
    private TableColumn<CovidData, Integer> residentialColumn;

    @FXML
    private TableColumn<CovidData, Integer> newCasesColumn;

    @FXML
    private TableColumn<CovidData, Integer> totalCasesColumn;

    @FXML
    private TableColumn<CovidData, Integer> newDeathsColumn;

    @FXML
    private ChoiceBox<String> sortChoiceBox;

    private List<CovidData> covidData;

    /**
     * Sets the COVID-19 data to be displayed in the table.
     * 
     * @param covidData The list of CovidData objects.
     */
    public void setCovidData(List<CovidData> covidData) {
        this.covidData = covidData;
        // Populate the table with the provided data.
        tableView.setItems(FXCollections.observableArrayList(covidData));
    }

    /**
     * Initializes the controller, setting up the table columns and the sorting
     * functionality.
     */
    @FXML
    private void initialize() {
        // Set up each column to display its corresponding data.
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        retailRecreationColumn.setCellValueFactory(new PropertyValueFactory<>("retailRecreationGMR"));
        groceryPharmacyColumn.setCellValueFactory(new PropertyValueFactory<>("groceryPharmacyGMR"));
        parksColumn.setCellValueFactory(new PropertyValueFactory<>("parksGMR"));
        transitStationsColumn.setCellValueFactory(new PropertyValueFactory<>("transitGMR"));
        workplacesColumn.setCellValueFactory(new PropertyValueFactory<>("workplacesGMR"));
        residentialColumn.setCellValueFactory(new PropertyValueFactory<>("residentialGMR"));
        newCasesColumn.setCellValueFactory(new PropertyValueFactory<>("newCases"));
        totalCasesColumn.setCellValueFactory(new PropertyValueFactory<>("totalCases"));
        newDeathsColumn.setCellValueFactory(new PropertyValueFactory<>("newDeaths"));

        // Populate the choice box and set the default sort option to "Date".
        sortChoiceBox.getItems().addAll("Date", "New Cases", "Total Cases", "New Deaths");
        sortChoiceBox.setValue("Date");

        // Add a listener to handle sorting based on the selected criteria from the
        // choice box.
        sortChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
            case "Date":
                tableView.getItems().sort(Comparator.comparing(CovidData::getDate));
                break;
            case "New Cases":
                tableView.getItems().sort(Comparator.comparingInt(CovidData::getNewCases).reversed());
                break;
            case "Total Cases":
                tableView.getItems().sort(Comparator.comparingInt(CovidData::getTotalCases).reversed());
                break;
            case "New Deaths":
                tableView.getItems().sort(Comparator.comparingInt(CovidData::getNewDeaths).reversed());
                break;
            }
        });
    }
}