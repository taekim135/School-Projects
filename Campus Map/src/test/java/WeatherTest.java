import app.Weather;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing the basic functionalities of the Weather class
 * Ensuring the interactions with OpenWeatherAPI are occurring as expected
 * @author Oliver Clennan
 * @author Sung Hyun Kim
 */

class WeatherTest {

    Weather weatherAPI;

    @BeforeEach
    void setUp() throws IOException {
        weatherAPI = new Weather();
    }

    /**
     * Test getCurrCondition method of Weather class
     */
    @Test
    void testGetCurrCondition() throws IOException, ParseException {
        System.out.println("testing getCurrCondition");
        weatherAPI.fetchCurrentWeather();
        String result = weatherAPI.getCurrCondition();
        assertNotNull(result);
    }

    /**
     * Test getCurrTemp method of Weather class
     */
    @Test
    void getCurrTemp() throws IOException, ParseException {
        System.out.println("testing getCurrTemp");
        weatherAPI.fetchCurrentWeather();
        String result = weatherAPI.getCurrTemp();
        assertNotNull(result);
    }
}