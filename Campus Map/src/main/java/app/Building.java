// library
package app;

import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class represents a building of UWO
 * Contains information related to a building such as floor details and building name
 * @author Taegyun Kim
 */
public class Building {

    /**
     * unique id for this building class. For identifying the type of building
     */
    private int id;
    /**
     * building name
     */
    private String name;
    /**
     * array of Floor objects. Represents each floor in this building
     */
    private Floor[] floors;
    /**
     * checks if a floor is currently displayed on the map or not
     */
    private int activeFloor;
    /**
     * number of floors in this building
     */
    private int numFloors;
    /**
     * code to represent this building
     */
    private String code;


    /**
     * Constructor for this class
     * The parameter sets the building type
     * @param buildingID id specific to a building. Integer type
     */
    public Building(int buildingID) throws IOException, ParseException {
        this.id = buildingID;

        switch (buildingID) {
            case 1:
                this.name = "Alumni Hall";
                this.numFloors = 3;
                this.code = "AH";
                break;
            case 2:
                this.name = "Middlesex College";
                this.numFloors = 5;
                this.code = "MC";
                break;
            case 3:
                this.name = "Natural Sciences Centre";
                this.numFloors = 3;
                this.code = "NSC";
                break;
        }
        
        this.floors = new Floor[this.numFloors];
        this.activeFloor = 1;
        
        for (int i = 0; i < this.numFloors; i++) {
            
            this.floors[i] = new Floor(i + 1, this.id);
            
        }

    }

    /**
     * public method to return the floors list associated with the building
     * Each floor is wrapped in Floor class
     * @return list of Floor objects
     */
    public Floor[] getFloors(){
        return this.floors;
    }

    /**
     * public method to return the id number associated with this class
     * @return integer type of the building id
     */
    public int getID(){
        return this.id;
    }

    /**
     * public method to return the building name
     * @return String type of the building name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Retrieves the code associated with this building (i.e. Alumni Hall -> 'AH')
     * @return The building code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Retrieves the number of floors in this building
     * @return The number of floors
     */
    public int getNumFloors() {
        return this.numFloors;
    }

    /**
     * method to set a desired floor as the current floor
     * @param floorID integer type
     */
    public void setActiveFloor(int floorID) {
        this.activeFloor = floorID;
    }

    /**
     * method to get the current active floor
     * @return Floor object of current floor
     */
    public Floor getActiveFloor() {
        return this.floors[this.activeFloor - 1];
    }

}
