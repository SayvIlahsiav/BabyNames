
/**
 * Analyze U.S. baby name trends from 1880 to 2014 with this Java project. 
 * Features methods for ranking, name equivalency across years, and birth statistics. 
 * Includes both full dataset analysis and simplified testing with provided short datasets.
 * 
 * @author - Vaishali Vyas
 * @version - 2023, November 6
 */

import edu.duke.*;
import java.util.*;
import org.apache.commons.csv.*;

public class BabyNames {
    public void printNames() {
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            if (numBorn >= 100) {
                System.out.println("Name " + rec.get(0) + " Gender " + rec.get(1) + " Num Born " + rec.get(2));
            }
        }
    }

    public void totalBirths(FileResource fr) {
        int totalMaleBirths = 0;
        int totalFemaleBirths = 0;
        int maleNameCount = 0;
        int femaleNameCount = 0;
    
        for (CSVRecord record : fr.getCSVParser(false)) {
            int numBorn = tryParse(record.get(2));
            if ("M".equals(record.get(1))) {
                totalMaleBirths += numBorn;
                maleNameCount++;
            } else {
                totalFemaleBirths += numBorn;
                femaleNameCount++;
            }
        }
    
        printSummary("Total names = ", maleNameCount + femaleNameCount);
        printSummary("Total births = ", totalMaleBirths + totalFemaleBirths);
        printSummary("Total girls' names = ", femaleNameCount);
        printSummary("Total girls = ", totalFemaleBirths);
        printSummary("Total boys' names = ", maleNameCount);
        printSummary("Total boys = ", totalMaleBirths);
    }
    
    private void printSummary(String message, int value) {
        System.out.println(message + " = " + value);
    }
    
    private int tryParse(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0; // or handle the exception as needed
        }
    }
    
    public void testTotalBirths() {
        FileResource fr = new FileResource();
        totalBirths(fr);
    }

}
