package app;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents single user and related data
 * @author Sung Hyun Kim
 * @version 1.0
 */

public class User{
    public String username;
    public String password;
    public boolean isDeveloper; //boolean checking if user is dev
    ArrayList<POI> customPOIs;  //collection of user specific POI


    /**
     * Constructs a User object, and initializes a POI collection
     * @param username String type. username (id) of the user
     * @param password String type. password of the user
     * @param isDeveloper boolean type. Dev status of the user (0 for normal, 1 for develoepr)
     */
    public User(String username, String password, boolean isDeveloper){
        this.username = username;
        this.password = password;
        this.isDeveloper = isDeveloper;
        this.customPOIs = new ArrayList<>();
    }

    /**
     * Retrieves username
     * @return String username of this user
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Retrieves password
     * @return String password of this user
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Retrieves user specific POIs that is marked as favourite
     * @return array list containing only favourited POIs
     */
    public List<POI> getFavourites() {
        ArrayList<POI> favouritePOI = new ArrayList<>();
        int arraySize = customPOIs.size();

        for (int i = 0; i < arraySize; i++) {
            if (customPOIs.get(i).checkFavourte()) {
                favouritePOI.add(customPOIs.get(i));
            }
        }
        return favouritePOI;
    }

    /**
     * Retrieves array list containing all user specific POIs
     * @return custom POI array
     */
    public List<POI> getCustomPOIs() {
        return customPOIs;
    }

    /**
     * Retrieves developer status of the user
     * @return developer status of this user
     */
    //get method for developer status
    public boolean getDevStatus() {
        return this.isDeveloper;
    }

    /**
     * Set username
     * @param username username of the user. String
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Set password
     * @param password password of the user. String
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Set developer status
     * @param devCode developer code for the user. int type (1 -dev, 0- not dev).
     */
    //set method for developer status (2-error(?) 1-dev 0-notdev)
    public void setDeveloper(int devCode) {
        switch (devCode) {
            case 0:
                this.isDeveloper = false;
                break;
            case 1:
                this.isDeveloper = true;
                break;
        }
    }

    /**
     * Add custom POI to user database
     * creates new POI object with given data then adds to the existing array
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
    public void addCustomPOI(int id, String name,  int roomNumber, String description, float[] position, String locationDesc, String category, int capacity, int floor ) {
        POI newPOI = new POI(id, name, roomNumber, description, position, locationDesc, category, capacity, floor);
        customPOIs.add(newPOI);
    }

}