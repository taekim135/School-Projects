
import app.POI;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class POITest {
    POI poi;

    /**
     * Setting up and create example poi will be used for testing.
     * @author Jiho Choi
     */
    @BeforeEach
    void setUp() {
        float[] position = {251.3f,324.214f};
        poi = new POI(0, "TestingPOI",123,"This is Testing.", position,
                "Location Description:Testing","Classroom",313,2);
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Test getID function in POI.java
     */
    @Test
    void testGetID(){
        System.out.println("Testing getID \n");
        int expectation = 0;
        int getId = poi.getID();
        assertNotNull(getId);
        assertEquals(expectation,getId);
    }

    /**
     * Test getName function in POI.java
     */
    @Test
    void testGetName(){
        System.out.println("Testing getName \n");
        String expectation = "TestingPOI";
        String getName = poi.getName();
        assertNotNull(getName);
        assertEquals(expectation,getName);
    }

    /**
     * Test getRoomNumber function in POI.java
     */
    @Test
    void testGetRoomNumber(){
        System.out.println("Testing getRoomNumber \n");
        int expectation = 123;
        int getRoomNumber = poi.getRoomNumber();
        assertNotNull(getRoomNumber);
        assertEquals(expectation,getRoomNumber);
    }
    /**
     * Test getDescription function in POI.java
     */
    @Test
    void testGetDescription(){
        System.out.println("Testing getDescription \n");
        String expectation = "This is Testing.";
        String getDescription = poi.getDescription();
        assertNotNull(getDescription);
        assertEquals(expectation,getDescription);
    }

    /**
     * Test getPosition function in POI.java
     */
    @Test
    void testGetPosition(){
        System.out.println("Testing getPosition \n");
        float[] expectation = {251.3f,324.214f};
        float[] getPosition = poi.getPosition();
        assertNotNull(getPosition);
        assertEquals(expectation[0],getPosition[0]);
        assertEquals(expectation[1],getPosition[1]);
    }

    /**
     * Test getLocationDesc function in POI.java
     */
    @Test
    void testGetLocationDesc(){
        System.out.println("Testing getLocationDesc \n");
        String expectation = "Location Description:Testing";
        String getLocationDesc = poi.getLocationDesc();
        assertNotNull(getLocationDesc);
        assertEquals(expectation,getLocationDesc);
    }

    /**
     * Test getCategory function in POI.java
     */
    @Test
    void testGetCategory(){
        System.out.println("Testing getClassroom \n");
        String expectation = "Classroom";
        String getCategory = poi.getCategory();
        assertNotNull(getCategory);
        assertEquals(expectation,getCategory);
    }
    /**
     * Test getCapacity function in POI.java
     */
    @Test
    void testGetCapacity(){
        System.out.println("Testing getCapacity \n");
        int expectation = 313;
        int getCapacity = poi.getCapacity();
        assertNotNull(getCapacity);
        assertEquals(expectation,getCapacity);
    }

    /**
     * Test getFloor function in POI.java
     */
    @Test
    void testGetFloor(){
        System.out.println("Testing getFloor \n");
        int expectation = 2;
        int getFloor = poi.getFloor();
        assertNotNull(getFloor);
        assertEquals(expectation,getFloor);
    }

    /**
     * Test setName function in POI.java
     */
    @Test
    void testSetName(){
        System.out.println("Testing setName \n");
        String setNew = "TestingPOI";
        poi.setName(setNew);
        String setName = poi.getName();
        assertNotNull(setName);
        assertEquals(setNew,setName);
    }

    /**
     * Test setRoomNumber function in POI.java
     */
    @Test
    void testSetRoomNumber(){
        System.out.println("Testing setRoomNumber \n");
        int setNew = 504;
        poi.setRoomNumber(setNew);
        int setRoomNumber = poi.getRoomNumber();
        assertNotNull(setRoomNumber);
        assertEquals(setNew,setRoomNumber);
    }

    /**
     * Test setDescription function in POI.java
     */
    @Test
    void testSetDescription(){
        System.out.println("Testing setDescription \n");
        String setNew = "setDescription";
        poi.setDescription(setNew);
        String setDescription = poi.getDescription();
        assertNotNull(setDescription);
        assertEquals(setNew,setDescription);
    }

    /**
     * Test setPosition function in POI.java
     */
    @Test
    void testSetPosition(){
        System.out.println("Testing setPosition \n");
        float[] setNew = {152.3f,293.1f};
        poi.setPosition(setNew);
        float[] setPosition = poi.getPosition();
        assertNotNull(setPosition);
        assertEquals(setNew[0],setPosition[0]);
        assertEquals(setNew[1],setPosition[1]);
    }

    /**
     * Test setLocDesc function in POI.java
     */
    @Test
    void testSetLocDesc(){
        System.out.println("Testing setLocDesc \n");
        String setNew = "setLocDesc";
        poi.setLocDesc(setNew);
        String setLocDesc = poi.getLocationDesc();
        assertNotNull(setLocDesc);
        assertEquals(setNew,setLocDesc);
    }

    /**
     * Test setCategory function in POI.java
     */
    @Test
    void testSetCategory(){
        System.out.println("Testing setCategory \n");
        String setNew = "setCategory";
        poi.setCategory(setNew);
        String setCategory = poi.getCategory();
        assertNotNull(setCategory);
        assertEquals(setNew,setCategory);
    }

    /**
     * Test setCapacity function in POI.java
     */
    @Test
    void testSetCapacity(){
        System.out.println("Testing setCapacity \n");
        int setNew = 710;
        poi.setCapacity(setNew);
        int setCapacity = poi.getCapacity();
        assertNotNull(setCapacity);
        assertEquals(setNew,setCapacity);
    }

    /**
     * Test setFloor function in POI.java
     */
    @Test
    void testSetFloor(){
        System.out.println("Testing setFloor \n");
        int setNew = 5;
        poi.setFloor(setNew);
        int setFloor = poi.getFloor();
        assertNotNull(setFloor);
        assertEquals(setNew,setFloor);
    }


}
