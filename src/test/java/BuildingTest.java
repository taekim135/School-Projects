import app.*;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is the test class for Building.java
 * All the major methods are tested
 * @author Taegyun Kim
 */
class BuildingTest {

    // class object to test as a sample
    Building sample;

    @BeforeEach
    void setUp() {
        System.out.println("setUpClass()");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearDown()");
    }

    /**
     * test for getFloors method
     * @throws FileNotFoundException
     */
    @Test
    void testGetFloors() throws IOException, ParseException {
        System.out.println("getFloors method");
        sample = new Building(1);
        int[] expected = {1, 2, 3};
        Floor[] floors = sample.getFloors();
        int[] result  = {floors[0].getFloorID(), floors[1].getFloorID(), floors[2].getFloorID()};
        assertNotNull(result);
        assertArrayEquals(expected, result);

    }

    /**
     * test for getID method. Checks the building ID
     */
    @Test
    void testGetID() throws IOException, ParseException {
        System.out.println("getID method");
        sample = new Building(2);
        int expected = 2;
        int result = sample.getID();
        assertNotNull(result);
        assertEquals(expected, result);
    }

    /**
     * test for getName method. Checks the name of the building
     */
    @Test
    void testGetName() throws IOException, ParseException {
        System.out.println("getname method");
        sample = new Building(2);
        String expected = "Middlesex College";
        String result = sample.getName();
        assertNotNull(result);
        assertEquals(expected, result);
    }

    /**
     * Testing the functionality of the getNumFloors() method
     * @throws FileNotFoundException If there is an error locating a floor's JSON file
     */
    @Test
    void testGetNumFloors() throws IOException, ParseException {
        System.out.println("getNumFloors() method");
        sample = new Building(1);
        int expected = 3;
        int result = sample.getNumFloors();
        assertNotNull(result);
        assertEquals(expected, result);
    }

    /**
     * Testing the functionality of the getActiveFloor() method with the default active floor (floor 1)
     * @throws FileNotFoundException If there is an error locating a floor's JSON file
     */
    @Test
    void testGetDefaultActiveFloor() throws IOException, ParseException {
        System.out.println("getActiveFloor() method");
        sample = new Building(1);
        int expected = 1;
        int result = sample.getActiveFloor().getFloorID();
        assertNotNull(result);
        assertEquals(expected, result);
    }

    /**
     * Testing the functionality of the getActiveFloor() method after the active floor has been manually changed
     * @throws FileNotFoundException If there is an error locating a floor's JSON file
     */
    @Test
    void testGetUpdatedActiveFloor() throws IOException, ParseException {
        System.out.println("getActiveFloor() method");
        sample = new Building(1);
        sample.setActiveFloor(2);
        int expected = 2;
        int result = sample.getActiveFloor().getFloorID();
        assertNotNull(result);
        assertEquals(expected, result);
    }

}