
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
import java.io.*;

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
    
    public int getRank(int year, String name, String gender) {
        //FileResource fr = new FileResource();
        FileResource fr = new FileResource("us_babynames/us_babynames_by_year/yob" + year + ".csv");
        int rank = 0;
        for (CSVRecord record : fr.getCSVParser(false)) {
            if (gender.equals(record.get(1)))
            {
                rank++;
                if (name.equals(record.get(0))) return rank;
            }
        }
        return -1;
    }
    
    public void testGetRank() {
        System.out.println(getRank(2012, "Mason", "M"));
    }
    
    public String getName(int year, int rank, String gender) {
        //FileResource fr = new FileResource();
        FileResource fr = new FileResource("us_babynames/us_babynames_by_year/yob" + year + ".csv");
        int currentRank = 0;
        for (CSVRecord record : fr.getCSVParser(false)) {
            if (gender.equals(record.get(1)))
            {
                currentRank++;
                if (currentRank == rank) return record.get(0);
            }
        }
        return "NO NAME";
    }
    
    public void testGetName() {
        System.out.println(getName(2012, 2, "M"));
    }
    
    public void whatIsNameInYear(String name, int year, int newYear, String gender) {
        int rank = getRank(year, name, gender);
        if (rank == -1) {
            System.out.println(name + " with the gender " + gender + " not found in " + year);
            return;
        }
        String newName = getName(newYear, rank, gender);
        System.out.println(name + " born in " + year + " would be " + newName + " if born in " + newYear + ".");
    }
    
    public void testWhatIsNameInYear() {
        whatIsNameInYear("Valerie", 2001, 1920, "F");
    }
    
    public int yearOfHighestRank(String name, String gender) {
        int highestYear = -1;
        int highestRank = Integer.MAX_VALUE;
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            int currentRank = 0;
            boolean found = false;
            for (CSVRecord record : fr.getCSVParser(false)) {
                if (gender.equals(record.get(1))) {
                    currentRank++;
                    if (name.equals(record.get(0))) {
                        found = true;
                        break;
                    }
                }
            }
            if (found && currentRank < highestRank) {
                highestRank = currentRank;
                highestYear = Integer.parseInt(f.getName().substring(3, 7));
            }
        }
        return highestYear;
    }
    
    public void testYearOfHighestRank() {
        System.out.println(yearOfHighestRank("Mason", "M"));
    }
    
    public double getAverageRank(String name, String gender) {
        double averageRank = -1.0;
        double totalRank = 0.0;
        int countFiles = 0;
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            countFiles++;
            int currentRank = 0;
            boolean found = false;
            for (CSVRecord record : fr.getCSVParser(false)) {
                if (gender.equals(record.get(1))) {
                    currentRank++;
                    if (name.equals(record.get(0))) {
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                totalRank += currentRank;
                averageRank = totalRank/countFiles;
            }
        }
        return averageRank;
    }
    
    public void testGetAverageRank() {
        System.out.println(getAverageRank("Jacob", "M"));
    }
    
    public int getTotalBirthsRankedHigher(int year, String name, String gender) {
        FileResource fr = new FileResource();
        //FileResource fr = new FileResource("us_babynames/us_babynames_by_year/yob" + year + ".csv");
        int currentRank = 0;
        int totalBirthsRankedHigher = 0;
        for (CSVRecord record : fr.getCSVParser(false)) {
            if (gender.equals(record.get(1))) {
                currentRank++;
                if (name.equals(record.get(0))) {
                    return totalBirthsRankedHigher;
                }
                totalBirthsRankedHigher += Integer.parseInt(record.get(2));
            }
        }
        return -1;
    }
    
    public void testGetTotalBirthsRankedHigher() {
        System.out.println(getTotalBirthsRankedHigher(2012, "William", "M"));
    }
    
    public static void main(String[] args) {
        BabyNames bn = new BabyNames();
        bn.testGetTotalBirthsRankedHigher();
    }
}
