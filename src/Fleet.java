import java.io.Serializable;
import java.util.ArrayList;

public class Fleet implements Serializable { //CSC 120 PROJECT 2 - HYUN KIM
    //Implements serializable to allow fleet object to be serialized in a database file

    private ArrayList<Boat> boatList; //ArrayList of boat objects
    private double fleetPrice; //Total price of all the boats in the fleet
    private double fleetExpense; //Total expense spent on all boats in the fleet

    public Fleet() { //Constructor to instantiate fleet object
        boatList = new ArrayList<Boat>();
        fleetPrice = 0; //Initialized to 0
        fleetExpense = 0; //Initialized to 0
    }

    public void addBoat(String[] boatData) { //Method to add a new boat

        try { //Boat constructor could throw Number Format, Index Out of Bounds and Illegal Argument exceptions

            //Illegal Argument exception thrown if boat type is invalid
            if (!boatData[0].equals("POWER") && !boatData[0].equals("SAILING")) {
                throw new IllegalArgumentException("ERROR: " + boatData[0] + " is an invalid boat type" +
                        ", failed to add boat.");
            }

            /* Illegal Argument exception thrown if boat year is invalid
                Boat year cannot be negative or greater than this year (2022) */
            if(Integer.parseInt(boatData[2]) < 0 || Integer.parseInt(boatData[2]) > 2022) {
                throw new IllegalArgumentException("ERROR: " + boatData[2] + " is an invalid year" +
                        ", failed to add boat.");
            }

            //Illegal Argument exception thrown if boat length is greater than 100
            if (Integer.parseInt(boatData[4]) > 100) {
                throw new IllegalArgumentException("ERROR: Boat length cannot exceed 100, failed to add boat.");
            }

            //Illegal Argument exception thrown if boat price is greater than a million
            if (Double.parseDouble(boatData[5]) > 1000000) {
                throw new IllegalArgumentException("ERROR: Boat price cannot exceed 1 million, failed to add boat.");
            }

            Boat newBoat = new Boat(boatData); //Create a new boat object based on boat data
            boatList.add(newBoat); //Adds the boat to the boat list in the fleet data
            fleetPrice += newBoat.getPrice(); //Updates the total price of the fleet
            fleetPrice += newBoat.getExpense(); //Updates the total expense of the fleet

        } catch (NumberFormatException exception) { //If Number Format exception is caught
            System.out.println("ERROR: Invalid boat data, failed to add boat."); //Print exception error message
        } catch (IndexOutOfBoundsException exception) { //If Index Out of Bounds exception is caught
            System.out.println("ERROR: Invalid CSV format, failed to add boat."); //Print exception error message
        } catch (IllegalArgumentException exception) { //If Illegal Argument exception is caught
            System.out.println(exception.getMessage()); //Print exception error message
        }

    }

    public void removeBoat(int location) { //Method to remove a boat
        Boat boatToRemove = boatList.get(location); //Gets the boat to remove
        fleetPrice -= boatToRemove.getPrice(); //Updates the total price of the fleet
        fleetExpense -= boatToRemove.getExpense(); //Updates the total expense of the fleet
        boatList.remove(location); //Removes the boat from the boat list in the fleet data
    }

    public int searchBoat(String boatToSearch) { //Returns the location of the boat in the fleet data
        int boatLocation; //Boat location in the ArrayList<Boat> of the Fleet
        int boatIndex;

        for(boatIndex = 0; boatIndex < boatList.size(); boatIndex++) { //For loop to search for boat
            if(boatList.get(boatIndex).getName().equalsIgnoreCase(boatToSearch)) { //If the boat exists in the fleet
                boatLocation = boatIndex;
                return boatLocation; //Returns the boat's location index in the fleet
            }
        }

        return -1; //-1 is returned if boat is not found
    }

    public void authorizeExpense(int location, double expense) { //Method to authorize boat expense
        Boat boatToSpend = boatList.get(location); //Gets the boat to request and spend the expense on
        double budget = boatToSpend.getPrice() - boatToSpend.getExpense(); //Calculates the budget left for the boat

        if (expense > budget) { //If the requested expense is greater than the budget left, expense is denied
            System.out.print("Expense not permitted, only $");
            System.out.printf("%.2f", budget);
            System.out.println(" left to spend.");
        }

        else { //If the budget left is greater than the requested expense, expense is approved
            fleetExpense += expense; //Updates the total expense of the fleet
            boatToSpend.setExpense(boatToSpend.getExpense() + expense); //Updates the expense spent for the boat
            System.out.print("Expense authorized, $");
            System.out.printf("%.2f", boatToSpend.getExpense()); //Gets and prints the total expense spent for the boat
            System.out.println(" spent.");
        }
    }

    @Override
    public String toString() { //Overridden toString() method to print the fleet data
        int boatIndex;
        String fleetString = "\nFleet Report:";

        //Loops to concatenate string representations of all boats in the boatList
        for(boatIndex = 0; boatIndex < boatList.size(); boatIndex++) {
            fleetString += "\n\t" + boatList.get(boatIndex).toString();
        }

        //String.format used to format string representation of the fleet's data accordingly
        fleetString += "\n\t" + String.format("%-50s", "Total")
                + ": Paid $" + String.format("%9.2f", fleetPrice)
                + " : Spent $" + String.format("%9.2f", fleetExpense);

        return fleetString; //Returns the string representation of the fleet
    }

}
