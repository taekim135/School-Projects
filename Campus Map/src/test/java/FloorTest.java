import app.*;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This is the test file for Floor class
 * @author Taegyun Kim
 * @author Oliver Clennan
 */

public class FloorTest {

    // class object for testing
    Floor test;
    @BeforeEach
    void setUp() throws IOException, ParseException {
        System.out.println("setUpClass()");
        test = new Floor(1,1);
        POI samplePOI = new POI();
        POI[] samplePOIs = new POI[1];
        samplePOIs[0] = samplePOI;
        test.setPOIs(samplePOIs, 1);
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearDownClass()");
    }

    /**
     * test for getPOPIs method
     * @throws FileNotFoundException
     */
    @Test
    void testGetPOIs() throws FileNotFoundException {
        System.out.println("getPOIs()");
        POI[] expected = test.getPOIs();
        POI[] result = test.getPOIs();
        assertArrayEquals(expected,result);
        assertNotNull(result);
    }

    /**
     * test for getFloorID. test for building ID
     */
    @Test
    void testGetFloorID() {
        System.out.println("getFloorID()");
        int expected = 1;
        int result = test.getFloorID();
        assertEquals(expected,result);
        assertNotNull(result);
    }

    /**
     * test for setFoor ID. Sets the id of a building
     */
    @Test
    void testSetFloorID() {
        System.out.println("getFloorID()");
        int expected = 2;
        test.setFloorID(2);
        int result = test.getFloorID();
        assertEquals(expected,result);
        assertNotNull(result);

    }

    /**
     * Testing the functionality of getNumPOIs() method to ensure POIs are being properly read from JSON files.
     * @throws FileNotFoundException If there is an error located the corresponding floor's JSON file.
     */
    @Test
    void getNumPOIs() throws IOException, ParseException {
        System.out.println("getNumPOIs() method");
        Floor sample = new Floor(1, 3);
        int expected = 2;
        int result = sample.getNumPOIs();
        assertNotNull(result);
        assertEquals(expected, result);
    }
    
}
