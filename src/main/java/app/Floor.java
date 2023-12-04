package app;

//core libraries
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;

/**
 * This class represents each floor in a building.
 * It contains a list of all default POIs and a floor number
 * This class is used by Building class and MapPage class
 * @author Taegyun Kim
 */
public class Floor {

    /**
     * list of POIs associated with this Floor
     */
    private POI[] POIs;
    /**
     * unique id associated with this Floor
     */
    private int id;
    /**
     * number of POIs for this Floor
     */
    private int numPOIs;
    
    private final Dictionary<Integer, String> buildingIndexToCode = new Hashtable<>() {{
        put(1, "AH");
        put(2, "MC");
        put(3, "NSC");
    }};

    /**
     * Constructor for this class
     * @param floorNumber integer type. the current floor numnber
     * @param buildingIndex integer type. The current building
     * @throws FileNotFoundException
     */
    public Floor(int floorNumber, int buildingIndex) throws IOException, ParseException {
        
        this.POIs = new POI[10];
        this.id = floorNumber;
        this.numPOIs = 0;
        String buildingCode = buildingIndexToCode.get(buildingIndex);
        
        JSONParser jsonParser = new JSONParser();
        int[] i = {0};
        
        try {
            
            JSONArray POILIST = (JSONArray) jsonParser.parse(new FileReader("src/main/resources/System Data/" + buildingCode + floorNumber + ".json"));
            
            POILIST.forEach(obj ->{

                JSONObject poi = (JSONObject) obj;
                this.POIs[i[0]] = new POI();
                this.POIs[i[0]].setRoomNumber((int)(long) poi.get("roomNumber"));
                this.POIs[i[0]].setName((String)poi.get("name"));
                this.POIs[i[0]].setDescription((String)poi.get("description"));
                this.POIs[i[0]].setCategory((String)poi.get("category"));
                this.POIs[i[0]].setLocDesc((String)poi.get("locationDesc"));

                JSONArray location = (JSONArray) poi.get("mapPosition");
                Float xPos = Float.parseFloat(location.get(0).toString());
                Float yPos = Float.parseFloat(location.get(1).toString());
                float[] resPos = {xPos, yPos};
                this.POIs[i[0]].setPosition(resPos);
                this.numPOIs++;
                i[0]++;

            });
            
        }
        catch (Exception e) {
            
        }
        
    }

    /**
     * Retrieves the number of POIs currently associated with this specific floor
     * @return The number of POIs
     */
    public int getNumPOIs() {
        return this.numPOIs;
    }

    /**
     * public method to retrieve the saved POIs on this object/floor
     * @return list of POI objects
     */
    public POI[] getPOIs() {
        return this.POIs;
    }

    public void setPOIs(POI[] item, int id){
        for (int i = 0; i < item.length; i++){
            if (this.POIs[i] == null){
                this.POIs[i] = new POI();
                this.POIs[i].setFloor(id);
                this.POIs[i].setFloor(item[i].getFloor());
                this.POIs[i].setPosition(item[i].getPosition());
                this.POIs[i].setCategory(item[i].getCategory());
                this.POIs[i].setName(item[i].getName());
                this.POIs[i].setRoomNumber(item[i].getRoomNumber());
                this.POIs[i].setDescription(item[i].getDescription());
                this.POIs[i].setLocDesc(item[i].getLocationDesc());
                this.POIs[i].setId(item[i].getID());
                this.POIs[i].setCapacity(item[i].getCapacity());
                this.POIs[i].setFavourite(item[i].getFavourite());
            }
        }
    }

    /**
     * public method to retrieve the floor number of this object
     * @return integer type. Current floor number of this object
     */
    public int getFloorID(){
        return this.id;
    }

    /**
     * public method to set the ID/floor number of the current floor
     * @param id integer type. The floor number to be set
     */
    public void setFloorID(int id){
        this.id = id;
    }

    /**
     * public method to find the target POI from the list of POIs on the current floor
     * @param poiID unique id associated with the target POI. Integer type
     * @return
     */
    public POI findPOI(int poiID){
        for (int i = 0; i < this.POIs.length; i++){
            if (POIs[i].getID() == poiID){
                return POIs[i];
            }
        }
        //return null if target POI does not exist
        return null;
    }


    /**
     * main method to test this class
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[]args) throws IOException, ParseException {
        Floor floorTest = new Floor(3, 1);

    }

}
