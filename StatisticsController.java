import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Controls the display and interaction of statistics related to COVID-19 data
 * within the application. It facilitates the visualization of various
 * statistics such as average mobility, total deaths, average total cases, and
 * average new cases over a specified date range.
 * 
 * @author Jiangjing Xu & Hongyuan Zhao & Lei Ding
 * @version 24.03.2024
 */
public class StatisticsController {
    private StatisticsAnalyzer analyzer;
    private CovidDataLoader loader = new CovidDataLoader();
    private static final String VERSION = "1.0";

    @FXML
    private BorderPane statisticsPanel;
    @FXML
    private StackPane container;
    @FXML
    private Label averageMobilityLabel, totalDeathsLabel, averageTotalCasesLabel, averageNewCasesLabel;

    private int currentIndex = 0;

    private List<Node> displayableItems = new ArrayList<>();

    public StatisticsController() {
        ArrayList<CovidData> covidDataList = loader.load();
        analyzer = new StatisticsAnalyzer(covidDataList); // Initialize analyzer with loaded data.
    }

    public void initialize() {
        // Populate the list of displayable items with statistic labels.
        displayableItems.add(averageMobilityLabel);
        displayableItems.add(totalDeathsLabel);
        displayableItems.add(averageTotalCasesLabel);
        displayableItems.add(averageNewCasesLabel);

        displayCurrentItem(); // Display the first statistic item.
    }

    private void displayCurrentItem() {
        if (!displayableItems.isEmpty() && currentIndex >= 0 && currentIndex < displayableItems.size()) {
            container.getChildren().setAll(displayableItems.get(currentIndex));
        }
    }

    @FXML
    public void aboutInformation(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About Statistics");
        alert.setHeaderText(null);
        alert.setContentText("Statistics\n" + VERSION);
        alert.showAndWait();
    }

    @FXML
    public void btnClose(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void btnPrevious(ActionEvent event) {
        if (currentIndex == 0) {
            currentIndex = displayableItems.size() - 1; // Loop to the last panel
        } else {
            currentIndex--; // Move to the previous panel
        }
        displayCurrentItem();
    }

    @FXML
    public void btnNext(ActionEvent event) {
        currentIndex = (currentIndex + 1) % displayableItems.size(); // Loop back to the first panel after the last
        displayCurrentItem();
    }

    public void updateStatistics(LocalDate startDate, LocalDate endDate) {
        ArrayList<CovidData> covidDataList = loader.load();
        ArrayList<CovidData> filteredDataList = filterDataByDateRange(covidDataList, startDate, endDate);
        analyzer = new StatisticsAnalyzer(filteredDataList);
        updateStatisticsLabels();
    }

    private ArrayList<CovidData> filterDataByDateRange(ArrayList<CovidData> dataList, LocalDate startDate,
            LocalDate endDate) {
        return new ArrayList<>(dataList.stream()
                .filter(data -> !data.getDate().isBefore(startDate) && !data.getDate().isAfter(endDate))
                .collect(Collectors.toList()));
    }

    private void updateStatisticsLabels() {
        double averageRetailRecreationMobility = analyzer.calculateAverageMobility("retailRecreation");
        double averageGroceryPharmacyMobility = analyzer.calculateAverageMobility("groceryPharmacy");
        int totalDeaths = analyzer.calculateTotalDeaths();
        double averageTotalCases = analyzer.calculateAverageTotalCases();
        double averageNewCases = analyzer.calculateAverageNewCases();

        averageMobilityLabel.setText(
                String.format("Average Retail & Recreation Mobility: %.2f\nAverage Grocery & Pharmacy Mobility: %.2f",
                        averageRetailRecreationMobility, averageGroceryPharmacyMobility));
        totalDeathsLabel.setText("Total Deaths: " + totalDeaths);
        averageTotalCasesLabel.setText("Average Total Cases: " + averageTotalCases);
        averageNewCasesLabel.setText("Average New Cases: " + averageNewCases);
    }

    public BorderPane getView() {
        return statisticsPanel;
    }
}
