import java.util.ArrayList;

/**
 * Analyzes COVID-19 data to compute statistical measures such as average
 * mobility in various categories, total deaths, average total cases, and
 * average new cases. The analysis helps in understanding the impact of COVID-19
 * over time and across different regions or aspects of daily life.
 *
 * @author Jiangjing Xu & Hongyuan Zhao & Lei Ding
 * @version 24.03.2024
 */
public class StatisticsAnalyzer {
    private ArrayList<CovidData> covidDataList;

    /**
     * Constructs a StatisticsAnalyzer with a specified list of COVID-19 data.
     * 
     * @param covidDataList The list of COVID-19 data records to be analyzed.
     */
    public StatisticsAnalyzer(ArrayList<CovidData> covidDataList) {
        this.covidDataList = covidDataList;
    }

    /**
     * Calculates the average mobility change for a specified category.
     * 
     * @param mobilityType The type of mobility (e.g., "retailRecreation",
     *                     "groceryPharmacy").
     * @return The average mobility change for the specified type.
     */
    public double calculateAverageMobility(String mobilityType) {
        if (covidDataList.isEmpty()) {
            return 0;
        }

        int sum = 0;
        for (CovidData data : covidDataList) {
            switch (mobilityType) {
            case "retailRecreation":
                sum += data.getRetailRecreationGMR();
                break;
            case "groceryPharmacy":
                sum += data.getGroceryPharmacyGMR();
                break;
            case "parks":
                sum += data.getParksGMR();
                break;
            case "transit":
                sum += data.getTransitGMR();
                break;
            case "workplaces":
                sum += data.getWorkplacesGMR();
                break;
            case "residential":
                sum += data.getResidentialGMR();
                break;
            }
        }
        return (double) sum / covidDataList.size();
    }

    /**
     * Calculates the total number of deaths reported in the data set.
     * 
     * @return The total number of deaths.
     */
    public int calculateTotalDeaths() {
        if (covidDataList == null || covidDataList.isEmpty()) {
            return 0; // Return 0 if the list is null or empty to avoid IndexOutOfBoundsException
        }

        // Initialize min and max with the first item's total deaths to ensure they're
        // within the range of actual data
        int min = covidDataList.get(0).getTotalDeaths();
        int max = covidDataList.get(0).getTotalDeaths();

        // Loop through the covidDataList to find the minimum and maximum total deaths
        for (CovidData data : covidDataList) {
            int totalDeaths = data.getTotalDeaths();
            if (totalDeaths < min) {
                min = totalDeaths; // Update min if a smaller total deaths count is found
            }
            if (totalDeaths > max) {
                max = totalDeaths; // Update max if a larger total deaths count is found
            }
        }

        return max - min; // Return the difference between the maximum and minimum total deaths
    }

    /**
     * Calculates the average number of total cases reported in the data set.
     * 
     * @return The average number of total cases.
     */
    public double calculateAverageTotalCases() {
        if (covidDataList.isEmpty()) {
            return 0;
        }

        int sum = 0;
        for (CovidData data : covidDataList) {
            sum += data.getTotalCases();
        }

        return (double) sum / covidDataList.size();
    }

    public double calculateAverageNewCases() {
        if (covidDataList.isEmpty()) {
            return 0;
        }

        int sum = 0;
        for (CovidData data : covidDataList) {
            sum += data.getNewCases();
        }

        return (double) sum / covidDataList.size();
    }
}
