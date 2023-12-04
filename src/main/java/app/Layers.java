package app;


/**
 * This class represents the layer on the map
 * It contains all the POIs associated and allow the user to enable or disable the layers
 */
public class Layers {

    /**
     * list of POIs associated with this layer
     */
    public POI[] POIs;
    /**
     * represents the type of layer
     */
    public String category;
    /**
     * checks if the layer is visible or not on the map
     */
    public boolean isVisible;
    /**
     * unique id associated with this layer
     */
    public int id;

    /**
     * Constructor for this class
     *
     * @param category String type. sets the type of layer to display
     * @param isvisible boolean type. Checks whether the layer is visible or not
     * @param id integer type. Sets an unique id number to this layer
     */
    public Layers(String category, boolean isvisible, int id) {
        this.category =category;
        this.isVisible = isvisible;
        this.id = id;
    }

    /**
     * public method to retrieve all the POIs associated with this layer
     * @return array of POI. Contains all the POIs in this layer
     */
    public POI[] getCurrLayer(){
        return this.POIs;
        

    }

    /**
     * public method to return this object's category
     * @return String type of this category
     */
    public String getCategory(){
        return this.category;
    }

    /**
     * public method to return the unique id associated with this object
     * @return integer
     */
    public int getID(){
        return this.id;
    }


}
