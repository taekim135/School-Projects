import app.Layers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LayersTest {

    Layers layer;
    @BeforeEach
    void setUp() {
        layer =new Layers("classroom", true, 2);
    }

    @Test
    void testGetCurrLayer() {

    }

    /**
     * Test getCategory method of Layers class
     */
    @Test
    void testGetCategory() {
        System.out.println("testing getCategory");
        String expResult = "classroom";
        String result = layer.getCategory();
        assertNotNull(result);
        assertEquals(expResult, result);
    }

    /**
     * Test getID method of Layers class
     */
    @Test
    void testGetID() {
        System.out.println("testing getID");
        int expResult = 2;
        int result = layer.getID();
        assertNotNull(result);
        assertEquals(expResult, result);
    }
}
