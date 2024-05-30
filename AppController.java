import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Write a description of class AppController here.
 *
 * @author Jiangjing Xu & Hongyuan Zhao & Lei Ding
 * @version 24.03.2024
 */
public class AppController {
    // UI components linked with FXML
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Button backButton, forwardButton;
    @FXML
    private MenuItem closeItem, aboutItem;
    @FXML
    private Label paneTitle;
    @FXML
    private StackPane mainContainer;

    // Controllers for different panels within the application
    private WelcomePanelController welcomeController;
    private StatisticsController statisticsController;
    private MapPanelController mapController;
    private LineChartController lineChartController;

    // Data model and state
    private List<Pane> panels = new ArrayList<>();
    private int currentIndex = 0;
    private LocalDate validStartDate;
    private LocalDate validEndDate;
    private MapPanelAnalyzer mapPanelAnalyzer;

    /**
     * Initializes the controller class. This method is automatically called after
     * the FXML file has been loaded. It sets up the application's UI and loads the
     * necessary data.
     */
    public void initialize() {
        loadCovidData(); // Load COVID data and determine the date range
        configureDatePickers(); // Configure date pickers based on loaded data
        initializePanels(); // Setup panels and ensure welcomePanel is visible first
        setupButtonHandlers(); // Setup navigation and other button handlers
        setupMenuItemHandlers(); // Setup MenuItems handlers
    }

    /**
     * Loads and initializes UI panels from their respective FXML files, and
     * configures their controllers.
     */
    private void initializePanels() {
        panels.clear(); // Clear any existing panels from the list

        try {
            // Load Welcome Panel
            FXMLLoader welcomeLoader = new FXMLLoader(getClass().getResource("WelcomePanelGUI.fxml"));
            Pane welcomePane = welcomeLoader.load();
            this.welcomeController = welcomeLoader.getController();
            panels.add(welcomePane); // Add loaded welcome panel to the list
            // Load Map Panel
            FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("MapPanelGUI.fxml"));
            VBox mapPane = mapLoader.load();
            this.mapController = mapLoader.getController();
            this.mapController.setMapPanelAnalyzer(mapPanelAnalyzer);
            panels.add(mapPane); // Add loaded map panel to the list

            // Load Statistics Panel
            FXMLLoader statisticsLoader = new FXMLLoader(getClass().getResource("StatisticsPanelGUI.fxml"));
            Pane statisticsPane = statisticsLoader.load();
            this.statisticsController = statisticsLoader.getController();
            panels.add(statisticsPane); // Add loaded statistics panel to the list

            // Load Line Chart Panel
            FXMLLoader lineChartLoader = new FXMLLoader(getClass().getResource("LineChartGUI.fxml"));
            Pane lineChartPane = lineChartLoader.load();
            this.lineChartController = lineChartLoader.getController();
            panels.add(lineChartPane); // Add loaded line chart panel to the list
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: Initializing Panels was not successful");
        }

        displayCurrentPanel(); // Display the current panel based on currentIndex
    }

    /**
     * Loads COVID data from csv file, establishing the range of valid dates for
     * which data is available.
     */
    private void loadCovidData() {
        try {
            CovidDataLoader loader = new CovidDataLoader();
            loader.load();
            LocalDate[] dateRange = loader.getDateRange();
            validStartDate = dateRange[0];
            validEndDate = dateRange[1];

            // Instantiate MapPanelAnalyzer with the valid date range
            mapPanelAnalyzer = new MapPanelAnalyzer(validStartDate, validEndDate);
        } catch (Exception e) {
            showAlertDialog("Data Loading Error", "Failed to load COVID data.");
        }
    }

    /**
     * Configures the date pickers to limit selections to valid dates based on the
     * loaded COVID data.
     */
    private void configureDatePickers() {
        if (validStartDate != null && validEndDate != null) {
            configureDatePicker(startDatePicker, validStartDate, validEndDate);
            configureDatePicker(endDatePicker, validStartDate, validEndDate);
            startDatePicker.setValue(validStartDate);
            endDatePicker.setValue(validEndDate);
        }
    }

    /**
     * Sets up handlers for the UI buttons, enabling navigation and functionality
     * within the application.
     */
    private void setupButtonHandlers() {
        backButton.setOnAction(this::navigateBack);
        forwardButton.setOnAction(this::navigateForward);
        // Initially disabled until a valid date range is selected.
        backButton.setDisable(true);
        forwardButton.setDisable(true);
        // Listen for date changes
        startDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> validateDateRange());
        endDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> validateDateRange());
    }

    /**
     * Sets up handlers for menu items, allowing actions such as closing the
     * application or displaying information about the application.
     */
    private void setupMenuItemHandlers() {
        closeItem.setOnAction(this::handleCloseItem);
        aboutItem.setOnAction(this::handleAboutItem);
    }

    /**
     * Navigates to the previous panel in the application.
     * 
     * @param event The event that triggered this action.
     */
    @FXML
    private void navigateBack(ActionEvent event) {
        if (currentIndex == 0) {
            currentIndex = panels.size() - 1; // Loop to the last panel
        } else {
            currentIndex--; // Move to the previous panel
        }
        displayCurrentPanel();
    }

    /**
     * Navigates to the next panel in the application.
     * 
     * @param event The event that triggered this action.
     */
    @FXML
    private void navigateForward(ActionEvent event) {
        currentIndex = (currentIndex + 1) % panels.size(); // Loop back to the first panel after the last
        displayCurrentPanel();
    }

    /**
     * Handles the action of the "Close" menu item, exiting the application.
     * 
     * @param event The event that triggered this action.
     */
    @FXML
    private void handleCloseItem(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Handles the action of the "About" menu item, displaying information about the
     * currently visible panel or the application in general.
     * 
     * @param event The event that triggered this action.
     */
    @FXML
    private void handleAboutItem(ActionEvent event) {
        if (!panels.isEmpty()) {

            Pane currentPanel = panels.get(currentIndex);

            String title = "About ";
            String content = "";

            if (currentPanel.equals(welcomeController.getView())) {
                title += "Welcome Panel";
                content += "You can see basic information at the Welcome Panel.";
            } else if (currentPanel.equals(mapController.getView())) {
                title += "Map Panel";
                content += "Click on the Borough's name button to view the data details,\n"
                        + "and you can choose to sort the data in different ways.";
            } else if (currentPanel.equals(statisticsController.getView())) {
                title += "Statistics Panel";
                content += "You can use the <> button to view different data";
            } else if (currentPanel.equals(lineChartController.getView())) {
                title += "Line Chart Panel";
                content += "The line chart shows the total cases and  total deaths in the London area,\n"
                        + "please note that the line graph is in months, and if the time interval chosen is too small,\n"
                        + "the line chart will appear as a straight line.";
            } else {
                System.err.println("About Window is not initializd");
            }

            showInformationDialog(title, content);
        } else {
            System.err.println("Panel is not initializd");
        }
    }

    private void displayCurrentPanel() {
        if (!panels.isEmpty() && currentIndex >= 0 && currentIndex < panels.size()) {
            mainContainer.getChildren().setAll(panels.get(currentIndex));
        }
    }

    /**
     * Validates the selected date range and updates UI components accordingly.
     */
    private void validateDateRange() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        boolean isDateRangeValid = startDate != null && endDate != null && !startDate.isBefore(validStartDate)
                && !endDate.isAfter(validEndDate) && !endDate.isBefore(startDate);

        backButton.setDisable(!isDateRangeValid);
        forwardButton.setDisable(!isDateRangeValid);
        if (isDateRangeValid) {
            mapPanelAnalyzer.updateDataRange(startDate, endDate);
            if (mapController != null) {
                mapController.updateMapVisualization(startDate, endDate);
                if (statisticsController != null) {
                    statisticsController.updateStatistics(startDate, endDate);
                    if (lineChartController != null) {
                        lineChartController.updateLineChart(startDate, endDate);
                    } else {
                        System.err.println("Error: LineChartController is not initializd");
                    }
                } else {
                    System.err.println("Error: StatisticsController is not initializd");
                }
            } else {
                System.err.println("Error: MapController is not initialized.");
            }
        } else {
            showAlertDialog("Invalid Date Range", "Please select a valid date range(2020/2/3-2023/2/9).");
        }
    }

    /**
     * Configures a given DatePicker, restricting its selectable dates.
     * 
     * @param datePicker The DatePicker to configure.
     * @param start      The earliest selectable date.
     * @param end        The latest selectable date.
     */
    private void configureDatePicker(DatePicker datePicker, LocalDate start, LocalDate end) {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(start) < 0 || date.compareTo(end) > 0);
            }
        });
    }

    /**
     * Shows an alert dialog with a warning message.
     * 
     * @param title   The title of the alert dialog.
     * @param content The content message displayed in the dialog.
     */
    private void showAlertDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Shows an information dialog.
     * 
     * @param title   The title of the information dialog.
     * @param content The informative message displayed in the dialog.
     */
    private void showInformationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
