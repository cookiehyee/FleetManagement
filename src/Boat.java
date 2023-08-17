import java.io.Serializable;

public class Boat implements Serializable { //CSC 120 PROJECT 2 - HYUN KIM
    //Implements serializable to allow boat object to be serialized in a database file

    private enum BoatType {POWER, SAILING}
    private BoatType type; //Boat's type
    private String name; //Boat's name
    private String model; //Boat's model
    private int year; //Boat's manufactured date
    private int length; //Boat's length
    private double price; //Boat's price
    private double expense; //Boat's expense

    //Constructor to instantiate boat object
    public Boat(String[] boatData) throws IllegalArgumentException, IndexOutOfBoundsException {
        //Could throw Index Out Of Bounds exception if the data is in wrong CSV format

        this.type = BoatType.valueOf(boatData[0]); //Could cause Illegal Argument exception
        this.name = boatData[1];
        this.year = Integer.parseInt(boatData[2]); //Could cause Number Format exception
        this.model = boatData[3];
        this.length = Integer.parseInt(boatData[4]); //Could cause Number Format exception
        this.price = Double.parseDouble(boatData[5]); //Could cause Number Format exception
        this.expense = 0;

        //Number Format exception is a sub category of Illegal Argument exception
    }

    public String getName() { //Accessor method that returns the boat's name
        return name; //Returns the boat's name
    }

    public double getPrice() { //Accessor method that returns the boat's price
        return price; //Returns the boat's price
    }

    public double getExpense() { //Accessor method that returns the boat's expense
        return expense; //Returns the boat's expense
    }

    public void setExpense(double expense) { //Mutator method that changes the boat's expense
        this.expense = expense; //Sets the boat's expense
    }

    @Override
    public String toString() { //Overridden toString() method to print the boat data

        String boatString = "";
        //String.format used to format string representation of a boat's data accordingly
        boatString += String.format("%-8s", type) + String.format("%-21s", name)
                + String.format("%-5s", year) + String.format("%-12s", model)
                + length + "' : Paid $" + String.format("%9.2f", price) +
                " : Spent $" + String.format("%9.2f", expense);

        return boatString; //Returns the string representation of the boat
    }

}
