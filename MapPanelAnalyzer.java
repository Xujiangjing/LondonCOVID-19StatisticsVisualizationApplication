import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * Handles the analysis of COVID-19 data for visualization purposes,
 * specifically focusing on mortality rates and detailed information for each
 * administrative district (borough) within a specified date range. It prepares
 * the data needed for visualizing COVID-19 mortality rates and detailed
 * borough-specific data.
 * 
 * @author Jiangjing Xu & Hongyuan Zhao & Lei Ding
 * @version 24.03.2024
 */
public class MapPanelAnalyzer {
    private LocalDate startDate;
    private LocalDate endDate;

    private ArrayList<CovidData> covidDataList = new ArrayList<CovidData>(); // List holding all loaded COVID data
                                                                             // entries.
    private ArrayList<CovidData> filteredCovidDataList = new ArrayList<>(); // Filtered list based on the selected date
                                                                            // range.
    private CovidDataLoader loader = new CovidDataLoader(); // Loader to fetch COVID data.

    /**
     * Constructs a MapPanelAnalyzer with specified start and end dates for data
     * analysis.
     * 
     * @param startDate The beginning of the date range for analysis.
     * @param endDate   The end of the date range for analysis.
     */
    public MapPanelAnalyzer(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        // Load initial COVID data and apply initial filtering.
        covidDataList = loader.load();
        filterDataByDateRange();
    }

    /**
     * Updates the date range for data analysis and re-filters the COVID data
     * accordingly.
     * 
     * @param newStartDate The new start date for the analysis.
     * @param newEndDate   The new end date for the analysis.
     */
    public void updateDataRange(LocalDate newStartDate, LocalDate newEndDate) {
        this.startDate = newStartDate;
        this.endDate = newEndDate;
        // Re-filter the data according to the new date range.
        filterDataByDateRange();
    }

    /**
     * Filters the loaded COVID data according to the current date range. The
     * filtering is based on whether the data's date falls within the start and end
     * dates, inclusive.
     */
    private void filterDataByDateRange() {
        filteredCovidDataList = covidDataList.stream()
                .filter(data -> !data.getDate().isBefore(startDate) && !data.getDate().isAfter(endDate))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Retrieves detailed COVID data for a specific borough within the currently
     * selected date range. This includes information such as dates, Google mobility
     * data, new cases, total cases, and new deaths.
     * 
     * @param boroughName The name of the borough for which data is requested.
     * @return A list of CovidData entries for the specified borough.
     */
    public List<CovidData> getCovidDataForBorough(String boroughName) {
        return filteredCovidDataList.stream().filter(data -> data.getBorough().equalsIgnoreCase(boroughName))
                .collect(Collectors.toList());
    }

    /**
     * Computes the total number of new deaths by borough within the selected date
     * range.
     * 
     * @return A map where each key is a borough name and each value is the total
     *         number of new deaths in that borough within the selected date range.
     */
    public Map<String, Integer> updateDeathCountsByBorough(LocalDate startDate, LocalDate endDate) {
        return filteredCovidDataList.stream()
                .collect(Collectors.groupingBy(CovidData::getBorough, Collectors.summingInt(CovidData::getNewDeaths)));
    }
}
