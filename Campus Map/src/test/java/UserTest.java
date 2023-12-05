//importing relevant components
import app.User;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Testing the basic functionalities of the User class
 * Checks  user component setters/getters are functioning properly
 * @author Sung Hyun Kim
 */
public class UserTest {

    User user;
    User setTestUser;

    @BeforeEach
    void setUp() throws IOException {
        user = new User("testid", "testpw", true);
        setTestUser = new User("test", "test", true);
    }

    /**
     * Test getUsername method of User class
     */
    @Test
    void testGetUsername() {
        System.out.println("testing getUsername");
        String expResult = "testid";
        String result = user.getUsername();
        assertNotNull(result);
        assertEquals(expResult, result);
    }

    /**
     * Test getPassword method of User class
     */
    @Test
    void testGetPassword() {
        System.out.println("testing getPassword");
        String expResult = "testpw";
        String result = user.getPassword();
        assertNotNull(result);
        assertEquals(expResult, result);
    }

    /**
     * Test getDevStatus method of User class
     */
    @Test
    void testGetDevStatus() {
        System.out.println("testing getDevStatus");
        Boolean result = user.getDevStatus();
        assertNotNull(result);
        assertTrue(result);
    }

    /**
     * Test setUsername method of User class
     */
    @Test
    void testSetUsername() {
        System.out.println("testing setUsername");
        String newName = "Jack";
        setTestUser.setUsername(newName);
        String result = setTestUser.getUsername();
        assertEquals(newName, result);
    }

    /**
     * Test setPassword method of User class
     */
    @Test
    void testSetPassword() {
        System.out.println("testing setPassword");
        String newPW = "1234";
        setTestUser.setPassword(newPW);
        String result = setTestUser.getPassword();
        assertEquals(newPW, result);
    }

    /**
     * Test setDevStatus method of User class
     */
    @Test
    void testSetDeveloper()  {
        System.out.println("testing setDeveloper");
        int newDevStatus = 0;
        setTestUser.setDeveloper(newDevStatus);
        Boolean result = setTestUser.getDevStatus();
        assertFalse(result);
    }


}
