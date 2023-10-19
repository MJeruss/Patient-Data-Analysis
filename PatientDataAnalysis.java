/*
 * Description: This program reads from files and calculates the average age of patients that survived 5 years or longer or died within 5 years. It makes additional calculations about survival rate and the average number of axillary nodes. 
 * Author:
 * 1. Max Jeruss
 */

import java.io.*;
import java.util.*;

public class PatientDataAnalysis {
    public static void main(String[] args) {
        // declare and initialize some variables
        boolean PatientDataAnalysisRun = true;
        boolean userInputFilePrompt;
        boolean userOutputFilePrompt;
        boolean userRunAgainPrompt;
        String userRunAgain;
        Scanner inputStream = null, keyboard = new Scanner(System.in);
        PrintWriter outputStream = null;
        String line;

        while (PatientDataAnalysisRun) {
            userInputFilePrompt = true;
            while (userInputFilePrompt) {
                // get user data, file names and handle exceptions
                System.out.println("\nPlease enter the name of a dataset in CSV format: ");
                String userInputFile = keyboard.nextLine();
                try {
                    inputStream = new Scanner(new File(userInputFile));
                    userInputFilePrompt = false;
                } catch (FileNotFoundException e) {
                    System.out.println("\nError opening the file " + userInputFile);
                }
            }

            // parse data
            int survivedAgeSum = 0;
            int survivedPatientsCount = 0;
            int survivedAxillaryCount = 0;
            int diedAgeSum = 0;
            int diedPatientsCount = 0;
            int diedAxillaryCount = 0;
            HashMap<Integer, Integer> survivedMap = new HashMap<>();
            HashMap<Integer, Integer> diedMap = new HashMap<>();
            while (inputStream.hasNext()) {
                line = inputStream.nextLine();
                String[] dataStrings = line.split(",");
                int[] dataInts = new int[dataStrings.length];
                for (int i = 0; i < dataInts.length; i++) {
                    try {
                        dataInts[i] = Integer.parseInt(dataStrings[i]);
                    } catch (NumberFormatException e) {
                        System.out.println("Error with the file format. Exiting Program.");
                        System.exit(0);
                    }
                }
                // if survived
                if (dataInts[3] == 1) {
                    survivedAgeSum += dataInts[0];
                    survivedPatientsCount++;
                    survivedAxillaryCount += dataInts[2];
                    if (survivedMap.containsKey(dataInts[1])) {
                        survivedMap.put(dataInts[1], survivedMap.get(dataInts[1]) + 1);
                    } else
                        survivedMap.put(dataInts[1], 1);
                }
                // if died
                if (dataInts[3] == 2) {
                    diedAgeSum += dataInts[0];
                    diedPatientsCount++;
                    diedAxillaryCount += dataInts[2];
                    if (diedMap.containsKey(dataInts[1])) {
                        diedMap.put(dataInts[1], diedMap.get(dataInts[1]) + 1);
                    } else
                        diedMap.put(dataInts[1], 1);
                }
                if (dataInts[3] != 1 && dataInts[3] != 2) {
                    System.out.println("Error with file format: Survival Column Error. Exiting Program.");
                    System.exit(0);
                }
            }

            // perform calculations
            double survivedAgeAverage = (double) survivedAgeSum / survivedPatientsCount,
                    survivedAxillaryAverage = (double) survivedAxillaryCount / survivedPatientsCount;
            double diedAgeAverage = (double) diedAgeSum / diedPatientsCount,
                    diedAxillaryAverage = (double) diedAxillaryCount / diedPatientsCount;
            double maxSurvivalRate = 0;
            int maxSurvivalYear = 0;
            double maxDiedRate = Double.MAX_VALUE;
            int maxDiedYear = 0;
            // if any people survived at all
            if (survivedMap.keySet().size() > 0) {
                for (Integer year : survivedMap.keySet()) {
                    double survived = survivedMap.get(year);
                    double died;
                    double survivalRate;
                    // if everyone survived in a given year(no intersection of keys from the two
                    // HashTables), this will throw NullPointerException
                    try {
                        died = diedMap.get(year);
                        survivalRate = survived / died;
                    } catch (NullPointerException e) {
                        // if anyone survived in a given year given no one died in that year(try block
                        // above), survival rate is 100%.
                        survivalRate = Double.MAX_VALUE;
                        maxSurvivalYear = year;
                        // break from loop bc 100% survival rate was found. could add an if statement
                        // above and incorporate an array to store multiple years with 100% survival
                        // rate.
                        break;
                    }
                    if (survivalRate >= maxSurvivalRate) {
                        maxSurvivalRate = survivalRate;
                        maxSurvivalYear = year;
                    }
                    if (survivalRate <= maxDiedRate) {
                        maxDiedRate = survivalRate;
                        maxDiedYear = year;
                    }
                }
            }

            // if any people died at all
            if (diedMap.keySet().size() > 0) {
                for (Integer year : diedMap.keySet()) {
                    double died = diedMap.get(year);
                    double survived;
                    double diedRate;
                    // if everyone died in a given year(no intersection of keys from the two
                    // HashTables), this will throw NullPointerException
                    try {
                        survived = survivedMap.get(year);
                        diedRate = died / survived;
                    } catch (NullPointerException e) {
                        // if all keys in survivedMap are 1 and all keys in diedMap are 2, or other way
                        // around, handle.
                        diedRate = Double.MAX_VALUE;
                        maxDiedYear = year;
                        break;
                    }
                    if (diedRate <= maxSurvivalRate) {
                        maxSurvivalRate = diedRate;
                        maxSurvivalYear = year;
                    }
                    if (diedRate >= maxDiedRate) {
                        maxDiedRate = diedRate;
                        maxDiedYear = year;
                    }
                }
            }

            // new loop to get valid output filename
            userOutputFilePrompt = true;
            while (userOutputFilePrompt) {
                System.out.println(
                        "Please enter the name of the output file that saves the result from the program: ");
                String userOutputFile = keyboard.nextLine();
                try {
                    outputStream = new PrintWriter(new File(userOutputFile));
                    userOutputFilePrompt = false;
                } catch (IOException e) {
                    System.out.println("Error. Please enter a valid file name: ");
                }
            }

            // write to file
            if (survivedMap.keySet().size() > 0) {
                outputStream.println("The year with the highest patient surival rate is: 19" + maxSurvivalYear);
                outputStream.printf("The average age of patients who survived 5 years or longer is: %.2f",
                        survivedAgeAverage);
                outputStream.printf(
                        "\nThe average number of positive axillary nodes detected for patients survived 5 years of longer: %.2f",
                        survivedAxillaryAverage);
            } else {
                outputStream.print("Unfortunately, no one survived in this sample.");

            }
            if (diedMap.keySet().size() > 0) {
                outputStream.print("\nThe year with the lowest patient surival rate is: 19" + maxDiedYear);
                outputStream.printf("\nThe average age of patients who died within 5 years is: %.2f",
                        diedAgeAverage);
                outputStream.printf(
                        "\nThe average number of positive axillary nodes detected for patients died within 5 years: %.2f",
                        diedAxillaryAverage);
            } else
                outputStream.print("\nFortunately, no one died in this sample.");
            inputStream.close();
            outputStream.close();

            // prompt user to run again
            userRunAgainPrompt = true;
            while (userRunAgainPrompt) {
                System.out.println("\nDo you want to run the program again (y for yes and n for no)?: ");
                userRunAgain = keyboard.nextLine();

                if (userRunAgain.equalsIgnoreCase("n")) {
                    userRunAgainPrompt = false;
                    PatientDataAnalysisRun = false;
                } else if (userRunAgain.equalsIgnoreCase("y"))
                    userRunAgainPrompt = false;
                else {
                    System.out.println("Invalid Response \n");
                    userRunAgainPrompt = true;
                }
            }

        }
        keyboard.close();
    }
}