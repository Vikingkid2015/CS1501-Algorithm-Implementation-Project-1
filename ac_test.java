// import everything necessary to run dlb
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// test class
public class ac_test
{
    // main, throws file not found exception, for user_history.txt
    public static void main(String[] args) throws FileNotFoundException
    {
        // create dlb for dictionary words to be stored in
        DLB dictionary = new DLB();
        // create dlb for user history to be stored in
        DLB userHistory = new DLB();

        // create scanner for dictionary file
        Scanner scanner = new Scanner(new File("dictionary.txt"));
        // create scanner for user input
        Scanner console = new Scanner(System.in);

        // open the user_history.txt file
        File userHistoryFile = new File("user_history.txt");

        // if the file exists
        if(userHistoryFile.exists())
        {
            // creat a new scanner to read the user_history file
            Scanner userHistoryScan = new Scanner(userHistoryFile);

            // while user history file has a next line
            while(userHistoryScan.hasNextLine())
            {
                // user history scan has next line
                String line = userHistoryScan.nextLine();
                // add the next line to the user history dlb
                userHistory.insert(line);
            }
        }

        // printwriter that will write the user's history to the file
        // the file output stream allows us to append to the file, instead of overwriting the lines
        PrintWriter historyWriter = new PrintWriter(new FileOutputStream(userHistoryFile, true));

        // create an arraylist to hold the predictions
        ArrayList<String> currentPredictions = new ArrayList<String>();
        // create a string to hold the current prefix
        String currentPrefix = "";

        // will hold the System.nanoTime() execution time, when searching for the predictions
        double totalTime = 0;
        // will hold the number of times the programs searches for predictions
        int count = 0;
        
        // while dictionary has a next line
        while(scanner.hasNextLine())
        {
            // scan in the next line
            String line = scanner.nextLine();
            // add the next line to the dictionary dlb
            dictionary.insert(line);
        }

        // while loop that will let us use the program until the "!" is inputted by the user
        while(true)
        {
            // new way to print conditional statements
            System.out.print("\nEnter " + (currentPrefix.length() == 0 ? "your first " : "the next ") + "character: ");
            // prefix will hold the next string input from the user
            String input = console.nextLine();

            // if the user entered more than 1 character, then ask them to enter a new single character
            while(input.length() > 1)
            {
                // tell the user that they can only write a single letter at a time
                System.out.println("Please only enter a single character.");
                // new way to write conditional prints, if it is the first character added, then say "your first ",
                // else it will print "the next "
                System.out.print("Enter " + (currentPrefix.length() == 0 ? "your first " : "the next ") + "character: ");
                input = console.nextLine();
            }

            // new way to do a switch case
            Pattern numbers = Pattern.compile("[12345]");
            Matcher numbersMatcher = numbers.matcher(input);
            // program ends if the user enters "!"
            if(input.equals("!"))
            {
                break;
            }
            // if the user enters a termination character, then
            else if(input.equals("$"))
            {
                // check if the word is already in the dlb and the file
                if(!userHistory.contains(currentPrefix))
                {
                    // if it isnt, then add the word to the user history dlb and file
                    userHistory.insert(currentPrefix);
                    historyWriter.println(currentPrefix);
                }
                // print that the word has been completed
                System.out.println("\nWORD COMPLETED: " + currentPrefix);
                // reset current prefix to be the empty string
                currentPrefix = "";
                // continue the loop
                continue;
            }
            // if the new type of switch case works, if the user inputs 1-5
            else if(numbersMatcher.matches())
            {
                // set select to be the integer that the user inputted, +1
                int select = Integer.parseInt(input) - 1;
                // if select is within the number of predictions that were found
                if(select >= 0 && select <= currentPredictions.size() )
                {
                    // and if the word isnt already contained in the user history dlb
                    if(!userHistory.contains(currentPredictions.get(select)))
                    {
                        // then add the word to the user history dlb, and file
                        userHistory.insert(currentPredictions.get(select));
                        historyWriter.println(currentPredictions.get(select));
                    }
                    // print word completed
                    System.out.println("\nWORD COMPLETED: " + currentPredictions.get(select));
                    // reset the current prefix to be the empty string
                    currentPrefix = "";
                }
                // if the inputted number is not one of the possible numbers that can be chosen
                else
                {
                    // tell the user that they inputted an invalid integer
                    System.out.println("You selected an invalid integer.");
                }
                // continue the loop
                continue;
            }

            // add the user input to the current prefix
            currentPrefix += input;
            // start is the when the time when prediction starts running
            long start = System.nanoTime();

            // check predictions from user dlb before checking the dictionary predictions
            ArrayList<String> userPredictions = userHistory.predict(currentPrefix);
            ArrayList<String> dictionaryPredictions = dictionary.predict(currentPrefix);

            // time is the current time, minus when the predictions happened, divided by 1 billion (to convert from nanoseconds to seconds)
            double time = (System.nanoTime()-start)/1000000000.0;
            // add the new time to the total time the program has used to do predictions
            totalTime += time;
            // incriment count
            count++;

            // print a blank line, then a formatted version of the prediction time in seconds
            System.out.println();
            System.out.println("(" + String.format("%.6f", time) + " s)");
            System.out.println("Predictions:");
            // set the total amount of predictions printed to 0
            int totalPrint = 0;

            // clear the predictions
            currentPredictions.clear();
            // from 0 to the size of the user's predictions
            for(int i = 0; i < userPredictions.size(); i++)
            {
                // incriment the total predictios printed
                totalPrint++;
                // print the prediction, and what number it is
                System.out.print("(" + totalPrint + ") " + userPredictions.get(i) + "\t");
                // add the user's predictions to the current predictions
                currentPredictions.add(userPredictions.get(i));
            }

            // same as the previous section
            for(int i = 0; i < dictionaryPredictions.size() && totalPrint < 5; i++)
            {
                totalPrint++;
                System.out.print("(" + totalPrint + ") " + dictionaryPredictions.get(i) + "\t");
                currentPredictions.add(dictionaryPredictions.get(i));
            }
            // io=f no predictions were found, then print that no predictions were found
            if(totalPrint == 0)
            {
                System.out.println("No predictions found.");
            }
            // print an empty line
            System.out.println("");
        }
        // calculate the average time by dividing the total time run for the predictions by the number of predictions done
        double averageTime = totalTime/count;
        // print a well formatted version of the average time used per prediction made
        System.out.print("\nAverage time: ");
        System.out.println("(" + String.format("%.6f", averageTime) + " s)");
        System.out.println("Bye!");
        // close the printwriter at the end of program
        historyWriter.close();
    }
}