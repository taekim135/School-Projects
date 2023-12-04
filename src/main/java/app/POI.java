package app;

import java.util.*;
import java.io.*;

/**
 * This class represents a POI
 * Each POI contains a name, location, room number, floor number etc.
 * Each POI will be a part of some floor in the building.
 */

public class POI {
    /**
     * unique id associated with this POI
     */
    private int id;
    /**
     * name of this POI
     */
    private String name;
    private int roomNumber;
    /**
     * brief description of this POI
     */
    private String description;
    private float[] position;
    /**
     * brief description of the location of this POI
     */
    private String locationDesc;
    private String category;
    private int capacity;
    private int floor;
    private boolean isSelected;
    private boolean isFavourite;  //added by Sung

    /**
     * Construct a POI object and initialises relevant variables
     * @param id id of the poi. int
     * @param name user defined name of the poi. String
     * @param roomNumber user defined room number of the poi. int
     * @param description user defined description of the poi. String
     * @param position position of the poi on map. float[]
     * @param locationDesc user defined location description of the poi. String
     * @param category category of the poi. String
     * @param capacity user defined capacity of the poi. int
     * @param floor floor of the poi. int
     */
    public POI(int id, String name, int roomNumber, String description, float[] position, String locationDesc, String category, int capacity, int floor){
        this.id = id;
        this.name = name;
        this.roomNumber = roomNumber;
        this.description = description;
        this.position = position;
        this.locationDesc = locationDesc;
        this.category = category;
        this.capacity = capacity;
        this.floor = floor;
        this.isSelected = false;
        this.isFavourite = false; //added by Sung
    }

    /**
     * empty constructor just for initialization
     */
    public POI(){
    }

    /**
     * retrieves id of the poi. int
     * @return id of the poi
     */
    public int getID(){
        return id;
    }

    /**
     * retrieves name of the poi. String
     * @return name of poi
     */
    public String getName(){
        return name;
    }

    /**
     * retrieves room number of the poi. int
     * @return room number of poi
     */
    public int getRoomNumber(){
        return roomNumber;
    }

    /**
     * retrieves description of poi. String
     * @return description of poi
     */
    public String getDescription(){
        return description;
    }

    /**
     * retrieves position of the poi. float[2]
     * @return position of poi
     */
    public float[] getPosition(){
        return position;
    }

    /**
     * retrieves location description of the poi. String
     * which is just a brief description in words
     * @return location description of poi
     */
    public String getLocationDesc(){
        return locationDesc;
    }

    /**
     * retrieves category of the poi. String
     * @return category of poi
     */
    public String getCategory(){
        return category;
    }

    /**
     * retrieves capacity of the poi. int
     * @return capacity of poi
     */
    public int getCapacity(){
        return capacity;
    }

    /**
     * retrieves floor of the poi. int
     * @return floor of poi
     */
    public int getFloor(){
        return floor;
    }

    //Function added by Sung -  check if POI is favourited
    public boolean checkFavourte() {
        return isFavourite;
    }

    /**
     * set the name of the poi. String
     * @return new name of poi
     */
    public void setName(String newName){
        this.name = newName;
    }
    /**
     * set the Room number of the poi. int
     * @return new Room number of poi
     */
    public void setRoomNumber(int newNumber){
        this.roomNumber = newNumber;
    }
    /**
     * set the description of the poi. String
     * @return new description of poi
     */
    public void setDescription(String newDesc){
        this.description = newDesc;
    }

    /**
     * set the position of the poi. float[2]
     * @return new position of poi
     */
    public void setPosition(float[] newPos){
        position = new float[2];
        position[0] = newPos[0];
        position[1] = newPos[1];
    }

    /**
     * set the location description of the poi. String
     * @return new location description of poi
     */
    public void setLocDesc(String newDesc){
        this.locationDesc = newDesc;
    }

    /**
     * set the category of the poi. String
     * @return new category of poi
     */
    public void setCategory(String newCategory){
        this.category = newCategory;
    }

    /**
     * set the capacity of the poi. int
     * @return new capacity of poi
     */
    public void setCapacity(int newCapacity){
        this.capacity = newCapacity;
    }

    /**
     * set the floor of the poi. int
     * @return new floor of poi
     */
    public void setFloor(int newFloor){
        this.floor = newFloor;
    }

    /**
     * method to set a poi as a favourite
      * @param favCheck integer type
     */
    public void setFavourite(int favCheck) {
        if (favCheck == 1) {
            isFavourite = true;
        }
    }

    /**
     * method to get the POI as a favourite
     * @return
     */
    public int getFavourite(){
        if (this.isFavourite){
            return 1;
        }else{
            return 0;
        }
    }

    /**
     * method to set the id of a poi
     * @param id integer type
     */
    public void setId(int id) {
        this.id = id;
    }
}
