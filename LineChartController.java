import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;

/**
 * Manages the presentation and updating of a line chart that displays COVID-19
 * case and death statistics over time. The controller filters data within a
 * specified date range and updates the chart to reflect trends in total cases
 * and total deaths.
 *
 * @author Jiangjing Xu & Hongyuan Zhao & Lei Ding
 * @version 24.03.2024
 */

public class LineChartController {

    private CovidDataLoader loader = new CovidDataLoader();
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private BorderPane LineChartPanel;

    /**
     * Initializes the line chart with proper labels and settings.
     */
    @FXML
    private void initialize() {
        xAxis.setLabel("Date");
        yAxis.setLabel("Cases");
        lineChart.setTitle("COVID-19 Cases");
        lineChart.setLegendVisible(true);
    }

    /**
     * Updates the line chart to display data within the specified date range.
     * 
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     */
    public void updateLineChart(LocalDate startDate, LocalDate endDate) {
        ArrayList<CovidData> covidDataList = loader.load();
        ArrayList<CovidData> filteredDataList = filterDataByDateRange(covidDataList, startDate, endDate);

        // Maps to store total cases and deaths by date.
        Map<LocalDate, Integer> totalCasesMap = new TreeMap<>();
        Map<LocalDate, Integer> totalDeathsMap = new TreeMap<>();

        // Populate the maps with aggregated data.
        for (CovidData data : filteredDataList) {
            LocalDate date = data.getDate();
            totalCasesMap.merge(date, data.getTotalCases(), Integer::sum);
            totalDeathsMap.merge(date, data.getTotalDeaths(), Integer::sum);
        }

        // Series for total cases and total deaths.
        XYChart.Series<String, Number> totalCasesSeries = new XYChart.Series<>();
        totalCasesSeries.setName("Total Cases");
        XYChart.Series<String, Number> totalDeathsSeries = new XYChart.Series<>();
        totalDeathsSeries.setName("Total Deaths");

        // Formatter for the date label.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");

        // Populate series with formatted data.
        for (Map.Entry<LocalDate, Integer> entry : totalCasesMap.entrySet()) {
            String formattedDate = entry.getKey().format(formatter);
            int totalCases = entry.getValue();
            totalCasesSeries.getData().add(new XYChart.Data<>(formattedDate, totalCases));
        }

        for (Map.Entry<LocalDate, Integer> entry : totalDeathsMap.entrySet()) {
            String formattedDate = entry.getKey().format(formatter);
            int totalDeaths = entry.getValue();
            totalDeathsSeries.getData().add(new XYChart.Data<>(formattedDate, totalDeaths));
        }

        // Clear previous data and add new series to the chart.
        lineChart.getData().clear();
        lineChart.getData().addAll(totalCasesSeries, totalDeathsSeries);
    }

    /**
     * Filters the list of COVID data to only include records within the specified
     * date range.
     * 
     * @param dataList  The full list of COVID data.
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @return A filtered list of COVID data.
     */
    private ArrayList<CovidData> filterDataByDateRange(ArrayList<CovidData> dataList, LocalDate startDate,
            LocalDate endDate) {
        return new ArrayList<>(dataList.stream()
                .filter(data -> !data.getDate().isBefore(startDate) && !data.getDate().isAfter(endDate))
                .collect(Collectors.toList()));
    }

    /**
     * Returns the BorderPane containing the line chart.
     * 
     * @return The BorderPane of the line chart.
     */
    public BorderPane getView() {
        return LineChartPanel;
    }
}
