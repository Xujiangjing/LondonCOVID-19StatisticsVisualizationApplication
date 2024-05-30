import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;

import com.opencsv.CSVReader;

public class CovidDataLoader {
    private ArrayList<CovidData> records = new ArrayList<>();

    /**
     * Return an ArrayList containing the rows in the Covid London data set csv
     * file.
     */
    public ArrayList<CovidData> load() {
        records.clear();
        // System.out.println("Begin loading Covid London dataset...");
        try {
            URL url = getClass().getResource("covid_london.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String[] line;
            // skip the first row (column headers)
            reader.readNext();
            while ((line = reader.readNext()) != null) {

                String date = line[0];
                String borough = line[1];
                int retailRecreationGMR = convertInt(line[2]);
                int groceryPharmacyGMR = convertInt(line[3]);
                int parksGMR = convertInt(line[4]);
                int transitGMR = convertInt(line[5]);
                int workplacesGMR = convertInt(line[6]);
                int residentialGMR = convertInt(line[7]);
                int newCases = convertInt(line[8]);
                int totalCases = convertInt(line[9]);
                int newDeaths = convertInt(line[10]);
                int totalDeaths = convertInt(line[11]);

                CovidData record = new CovidData(date, borough, retailRecreationGMR, groceryPharmacyGMR, parksGMR,
                        transitGMR, workplacesGMR, residentialGMR, newCases, totalCases, newDeaths, totalDeaths);
                records.add(record);
            }
        } catch (IOException | URISyntaxException e) {
            System.out.println("Something Went Wrong?!");
            e.printStackTrace();
        }
        // System.out.println("Number of Loaded Records: " + records.size());
        return records;
    }

    /**
     *
     * @param doubleString the string to be converted to Double type
     * @return the Double value of the string, or -1.0 if the string is either empty
     *         or just whitespace
     */
    private Double convertDouble(String doubleString) {
        if (doubleString != null && !doubleString.trim().equals("")) {
            return Double.parseDouble(doubleString);
        }
        return (double) 0;
    }

    /**
     *
     * @param intString the string to be converted to Integer type
     * @return the Integer value of the string, or -1 if the string is either empty
     *         or just whitespace
     */
    private Integer convertInt(String intString) {
        if (intString != null && !intString.trim().equals("")) {
            return Integer.parseInt(intString);
        }
        return 0;
    }

    public LocalDate[] getDateRange() {
        LocalDate minDate = LocalDate.MAX;
        LocalDate maxDate = LocalDate.MIN;

        for (CovidData record : records) {
            LocalDate date = record.getDate();
            if (date.isBefore(minDate)) {
                minDate = date;
            }
            if (date.isAfter(maxDate)) {
                maxDate = date;
            }
        }
        return new LocalDate[] { minDate, maxDate };
    }

}
