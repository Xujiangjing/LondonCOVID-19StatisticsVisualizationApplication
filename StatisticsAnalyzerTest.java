import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * This class contains unit tests for the StatisticsAnalyzer class. It verifies
 * the correctness of the methods responsible for calculating various statistics
 * such as average mobility, total deaths, average total cases, and average new
 * cases based on a set of CovidData.
 * 
 * @author Jiangjing Xu & Hongyuan Zhao & Lei Ding
 * @version 24.03.2024
 */

class StatisticsAnalyzerTest {
    private ArrayList<CovidData> covidDataList;
    private StatisticsAnalyzer analyzer;

    /**
     * Sets up test data before each test. This method is called before the
     * execution of each test method.
     */
    @BeforeEach
    void setUp() {
        // Initialize the test data list
        covidDataList = new ArrayList<>();

        // Adding sample CovidData to the list for Barking And Dagenham borough
        covidDataList
                .add(new CovidData("2022-10-15", "Barking And Dagenham", -17, 2, 27, -6, -19, 0, 11, 72918, 0, 615));
        covidDataList
                .add(new CovidData("2022-10-14", "Barking And Dagenham", -15, 10, 29, -11, -29, 3, 12, 72907, 0, 615));
        covidDataList
                .add(new CovidData("2022-10-13", "Barking And Dagenham", -15, 6, 28, -8, -31, 3, 17, 72895, 0, 615));

        // Initialize the StatisticsAnalyzer with the test data
        analyzer = new StatisticsAnalyzer(covidDataList);
    }

    /**
     * Tests the calculateAverageMobility method with the 'retailRecreation'
     * mobility type.
     */
    @Test
    void calculateAverageMobility_returnsCorrectAverage() {
        double average = analyzer.calculateAverageMobility("retailRecreation");
        assertEquals(-47.0 / 3, average, "The average mobility calculation should be correct.");
    }

    /**
     * Tests the calculateTotalDeaths method to ensure it accurately calculates the
     * sum of new deaths.
     */
    @Test
    void calculateTotalDeaths_returnsSum() {
        int totalDeaths = analyzer.calculateTotalDeaths();
        assertEquals(0, totalDeaths, "The total deaths calculation should be correct.");
    }

    /**
     * Tests the calculateAverageTotalCases method to ensure it returns the correct
     * average of total cases.
     */
    @Test
    void calculateAverageTotalCases_returnsCorrectAverage() {
        double averageTotalCases = analyzer.calculateAverageTotalCases();
        assertEquals(218720.0 / 3, averageTotalCases, "The average total cases calculation should be correct.");
    }

    /**
     * Tests the calculateAverageNewCases method to ensure it accurately calculates
     * the average of new cases.
     */
    @Test
    void calculateAverageNewCases_returnsCorrectAverage() {
        double averageNewCases = analyzer.calculateAverageNewCases();
        assertEquals(40.0 / 3, averageNewCases, "The average new cases calculation should be correct.");
    }

    @Test
    public void test() {
    }
}
