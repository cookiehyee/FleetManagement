import java.io.*;
import java.util.Scanner;

public class FleetManagement { //CSC 120 PROJECT 2 - HYUN KIM

    public static void main(String[] args)  {

        Fleet fleetData = new Fleet(); //Instantiates the fleet object to load data into
        String databaseFile = "FleetData.db"; //Name of the fleet database
        boolean commandLineExists = args.length > 0; //Checks if command line exists

        try {
            if (commandLineExists) { //If command line exists
                loadCSVData(args[0], fleetData); //Loads CSV data into fleetData, throws IO exception
                //args[0] is the name of the CSV file containing data about the boats
            } else { //If there is no command line 
                fleetData = deserializedFleetDB(databaseFile); //Deserializes and loads fleet database to fleetData
                //Throws IO and Class Not Found exceptions
            }
        } catch (IOException exception) {
            //Prints error message if IO exception is thrown
            System.out.println("ERROR: Failed to load fleet database.");
        } catch (ClassNotFoundException exception) {
            //Prints error message if Class Not Found exception is thrown
            System.out.println("ERROR: Failed to deserialize database.");
        }

        displayWelcomeMessage(); //Displays welcome message
        displayMenu(fleetData); //Displays menu to be chosen by the user
        saveFleetDB(fleetData); //Serializes and saves the fleet data into a database file

    }

    //Method to load CSV data into the fleet
    public static void loadCSVData (String file, Fleet fleet) throws IOException {

        FileInputStream boatInputStream = new FileInputStream(file);
        Scanner boatScanner = new Scanner(boatInputStream);
        String[] boatData;

        while (boatScanner.hasNextLine()) { //Loop to add every line of boat data
            boatData = boatScanner.nextLine().split(",");
            fleet.addBoat(boatData);  //Add boat to the fleet data
        }

        boatInputStream.close(); //Close input file

    }

    //Method to deserialize fleet database and load data into the fleet
    public static Fleet deserializedFleetDB(String dbFile) throws IOException, ClassNotFoundException {

        FileInputStream fleetInputStream = new FileInputStream(dbFile);
        ObjectInputStream loadFleet = new ObjectInputStream(fleetInputStream);
        Fleet fleetDatabase = (Fleet)loadFleet.readObject(); //Deserialize fleet database

        loadFleet.close();
        fleetInputStream.close();

        return fleetDatabase; //Returns the deserialized fleet database

    }

    public static void displayWelcomeMessage() {

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");

    }

    public static void displayMenu(Fleet fleetData) { //Method to display menu

        char userOption;

        do {

            userOption = getUserOption(); //Receives user option for menu

            if (userOption == 'P') {
                System.out.println(fleetData); //Prints entire fleet data
            }

            else if (userOption == 'A') {
                addBoatToFleet(fleetData); //Adds a new boat to the fleet
            }

            else if (userOption == 'R') {
                removeBoatFromFleet(fleetData);//Removes a boat from the fleet
            }

            else if (userOption == 'E') {
                requestBoatExpense(fleetData); //Requests to spend expense on a boat
            }

        } while (userOption != 'X'); //Terminates if user option is X

        //Displays exit message
        System.out.println("\nExiting the Fleet Management System");

    }

    public static char getUserOption() { //Method to return user option

        Scanner keyboard = new Scanner(System.in);
        char option; //User option

        System.out.print("\n(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
        option = Character.toUpperCase(keyboard.next().charAt(0));

        //Prompts user to input menu option if an invalid option was input
        while(option != 'P' && option != 'A' && option != 'R' && option != 'E' &&option != 'X') {
            System.out.println("Invalid menu option, try again");
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            option = Character.toUpperCase(keyboard.next().charAt(0));
        }

        return option; //Returns user option

    }

    public static void addBoatToFleet(Fleet fleetData) { //Method to add a boat to the fleet

        Scanner keyboard = new Scanner(System.in);
        String[] boatData; //String containing data of a boat

        System.out.print("Please enter the new boat CSV data\t\t\t: ");
        boatData = keyboard.nextLine().split(","); //Splits the CSV data based on comma into a string array
        fleetData.addBoat(boatData); //Adds a boat by its CSV data

    }

    public static void removeBoatFromFleet(Fleet fleetData) { //Method to remove a boat from the fleet

        Scanner keyboard = new Scanner(System.in);
        String boatName; //Name of the boat to remove
        int boatLocation; //Index of the boat in the ArrayList<Boat> of the Fleet

        System.out.print("Which boat do you want to remove?\t\t\t: ");
        boatName = keyboard.nextLine();
        boatLocation = fleetData.searchBoat(boatName); //Searches for the boat's location in the fleet
        //If the boat does not exist, then -1 is returned

        if(boatLocation!= -1) { //If the boat is found in the fleet
            fleetData.removeBoat(boatLocation);
        }

        else { //If boat does not exist in the fleet
            System.out.println("Cannot find boat " + boatName);
        }

    }

    //Method to request to spend expense on a boat in the fleet
    public static void requestBoatExpense(Fleet fleetData) {

        Scanner keyboard = new Scanner(System.in);
        String boatName; //Name of the boat to request to spend
        int boatLocation;
        double expense;

        System.out.print("Which boat do you want to spend on?\t\t\t: ");
        boatName = keyboard.nextLine();
        boatLocation = fleetData.searchBoat(boatName); //Searches for the boat's location in the fleet
        //If the boat does not exist, then -1 is returned

        if(boatLocation != -1) { //If the boat is found in the fleet
            System.out.print("How much do you want to spend?\t\t\t\t: ");
            expense = keyboard.nextDouble();
            fleetData.authorizeExpense(boatLocation, expense); //Request to authorize expense on the boat
        }

        else { //If boat does not exist in the fleet
            System.out.println("Cannot find boat " + boatName);
        }

    }

    public static void saveFleetDB(Fleet fleetData)  { //Method to save fleet data in database file

        try {

            FileOutputStream fleetDataBase = new FileOutputStream("FleetData.db");
            ObjectOutputStream recordFleet = new ObjectOutputStream(fleetDataBase);

            recordFleet.writeObject(fleetData); //Serialize fleet data into the database file
            recordFleet.close(); //Close object writer
            fleetDataBase.close(); //Close output file

        } catch(IOException exception) { //If IOException is caught
            System.out.println("ERROR: Failed to save database file."); //Print error message
        }

    }

}
