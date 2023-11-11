
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
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        int totalNames = 0;
        int totalBNames = 0;
        int totalGNames = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            if (rec.get(1).equals("M")) {
                totalBoys += numBorn;
                totalBNames++;
            } else {
                totalGirls += numBorn;
                totalGNames++;
            }
        }
        totalNames = totalBNames + totalGNames;
        System.out.println("total names = " + totalNames);
        System.out.println("total births = " + totalBirths);
        System.out.println("total girls names = " + totalGNames);
        System.out.println("total girls = " + totalGirls);
        System.out.println("total boys names = " + totalBNames);
        System.out.println("total boys = " + totalBoys);
    }
    
    public void testTotalBirths() {
        FileResource fr = new FileResource();
        totalBirths(fr);
    }

}
