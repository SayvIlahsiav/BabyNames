
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

    // Displays names with at least 100 births from the CSV file.
    public void printNames() {
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            if (numBorn >= 100) {
                System.out.println("Name " + rec.get(0) + " Gender " + rec.get(1) + " Num Born " + rec.get(2));
            }
        }
    }
    
    // Calculates and prints total births and name counts by gender from the given FileResource.
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
        printSummary("Total names", maleNameCount + femaleNameCount);
        printSummary("Total births", totalMaleBirths + totalFemaleBirths);
        printSummary("Total girls' names", femaleNameCount);
        printSummary("Total girls", totalFemaleBirths);
        printSummary("Total boys' names", maleNameCount);
        printSummary("Total boys", totalMaleBirths);
    }
    
    // Helper method to print formatted summary messages.
    private void printSummary(String message, int value) {
        System.out.println(message + " = " + value);
    }
    
    // Attempts to parse a string to an integer, returns 0 on failure.
    private int tryParse(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0; // or handle the exception as needed
        }
    }
    
    // Tests the totalBirths method using a FileResource.
    public void testTotalBirths() {
        FileResource fr = new FileResource();
        totalBirths(fr);
    }
    
    //  Returns the rank of a name for a given year and gender.
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
    
    // Tests the getRank method with user-defined values.
    public void testGetRank() {
        Scanner scn = new Scanner(System.in);
        System.out.print("Enter year: ");
        int year = Integer.parseInt(scn.nextLine());
        System.out.print("Enter name: ");
        String name = scn.nextLine();
        System.out.print("Enter gender: ");
        String gender = scn.nextLine();
        System.out.println("Rank of " + name + " with gender " + gender + " is: " + getRank(year, name, gender));
    }
    
    // Retrieves the name corresponding to the rank in a specific year and gender.
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
    
    // Tests the getName method with user-defined values.
    public void testGetName() {
        Scanner scn = new Scanner(System.in);
        System.out.print("Enter year: ");
        int year = Integer.parseInt(scn.nextLine());
        System.out.print("Enter rank: ");
        int rank = Integer.parseInt(scn.nextLine());
        System.out.print("Enter gender: ");
        String gender = scn.nextLine();
        System.out.println("Name at rank " + rank + " with gender " + gender + " is: " + getName(year, rank, gender));
    }
    
    // Determines what a name from one year would have been in another year.
    public void whatIsNameInYear(String name, int year, int newYear, String gender) {
        int rank = getRank(year, name, gender);
        if (rank == -1) {
            System.out.println(name + " with the gender " + gender + " not found in " + year);
            return;
        }
        String newName = getName(newYear, rank, gender);
        System.out.println(name + " born in " + year + " would be " + newName + " if born in " + newYear + ".");
    }
    
    // Tests whatIsNameInYear with user-defined values.
    public void testWhatIsNameInYear() {
        Scanner scn = new Scanner(System.in);
        System.out.print("Enter name: ");
        String name = scn.nextLine();
        System.out.print("Enter year: ");
        int year = Integer.parseInt(scn.nextLine());
        System.out.print("Enter new year: ");
        int newYear = Integer.parseInt(scn.nextLine());
        System.out.print("Enter gender: ");
        String gender = scn.nextLine();
        whatIsNameInYear(name, year, newYear, gender);
    }
    
    // Finds the year when a name had its highest rank.
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
    
    // Tests yearOfHighestRank with user-defined values.
    public void testYearOfHighestRank() {
        Scanner scn = new Scanner(System.in);
        System.out.print("Enter name: ");
        String name = scn.nextLine();
        System.out.print("Enter gender: ");
        String gender = scn.nextLine();
        System.out.println(name + " with the gender " + gender + " had highest rank in the year: " + yearOfHighestRank(name, gender));
    }
    
    // Calculates the average rank of a name over all files.
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
    
    // Tests getAverageRank with user-defined values.
    public void testGetAverageRank() {
        Scanner scn = new Scanner(System.in);
        System.out.print("Enter name: ");
        String name = scn.nextLine();
        System.out.print("Enter gender: ");
        String gender = scn.nextLine();
        System.out.println(name + " with the gender " + gender + " has an average rank of " + getAverageRank(name, gender));
    }
    
    // Calculates total births of names ranked higher than a given name in a year.
    public int getTotalBirthsRankedHigher(int year, String name, String gender) {
        //FileResource fr = new FileResource();
        FileResource fr = new FileResource("us_babynames/us_babynames_by_year/yob" + year + ".csv");
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
    
    // Tests getTotalBirthsRankedHigher with user-defined values.
    public void testGetTotalBirthsRankedHigher() {
        Scanner scn = new Scanner(System.in);
        System.out.print("Enter year: ");
        int year = Integer.parseInt(scn.nextLine());
        System.out.print("Enter name: ");
        String name = scn.nextLine();
        System.out.print("Enter gender: ");
        String gender = scn.nextLine();
        System.out.println(getTotalBirthsRankedHigher(year, name, gender) + " is the total no. of births with rank higher than " + name + " with the gender " + gender);
    }
    
    public static void main(String[] args) {
        BabyNames bn = new BabyNames();
        Scanner scn = new Scanner(System.in);
        while (true) {
        System.out.println();
        System.out.println("What do you want to do? Type 1, 2, 3, 4, 5, 6, 7, or 8:");
        System.out.println("1: Display Popular Names - Names with at least 100 births.");
        System.out.println("2: Calculate Total Births - Displays total births and name counts by gender.");
        System.out.println("3: Check Name Rank - Displays the rank of a name for a specified year and gender");
        System.out.println("4: Find Name by Rank - Retrieves the name corresponding to a given rank in a specific year and gender");
        System.out.println("5: Compare Name Across Years - Determines what a name from one year would have been in another year.");
        System.out.println("6: Year of Highest Rank - Finds the year when a name had its highest rank.");
        System.out.println("7: Calculate Average Rank - Calculates the average rank of a name over all files.");
        System.out.println("8: Births Before Rank - Calculates total births of names ranked higher than a given name in a year.");
        
        System.out.println();
        String choice = scn.nextLine();
        System.out.println();
        
        if (choice.equals("1")) bn.printNames();
        else if (choice.equals("2")) bn.testTotalBirths();
        else if (choice.equals("3")) bn.testGetRank();
        else if (choice.equals("4")) bn.testGetName();
        else if (choice.equals("5")) bn.testWhatIsNameInYear();
        else if (choice.equals("6")) bn.testYearOfHighestRank();
        else if (choice.equals("7")) bn.testGetAverageRank();
        else if (choice.equals("8")) bn.testGetTotalBirthsRankedHigher();
        else {
            System.out.println("Invalid choice. Please enter a number from 1 to 8.");
            continue;
        }
    
        System.out.println("\n\nDo you want to continue? (y or n)");
        String continueChoice = scn.nextLine();
    
        if (continueChoice.equalsIgnoreCase("n")) {
            break;
        }
    }

        scn.close();
    }
}
