package app;// Standard libraries
import java.io.*;
import static java.lang.System.exit;

// Libraries required to conduct HTTP requests
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// Libraries required to process JSON objects
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Facilitates interactions with the OpenWeatherAPI service as needed.
 * Attains information required to support the current weather feature/functionality
 *
 * @author Oliver Clennan
 * @version 1.0
 */
public class Weather {

    // Defining API-related constants, and declaring relevant instance variables
    private final double[] CAMPUS_COORDS = {43.0096, -81.2737};
    private final String UNIT_TYPE = "metric";
    private final String API_KEY = "28027663d6d744372098017ce98d3cd9";
    private final String API_ENDPOINT = "https://api.openweathermap.org/data/2.5/weather";
    private final String GET_PARAMS = String.format("?lat=%s&lon=%s&appid=%s&units=%s", CAMPUS_COORDS[0], CAMPUS_COORDS[1], API_KEY, UNIT_TYPE);
    private final String ICON_URL = "https://openweathermap.org/img/wn/";
    private String currCondition;
    private String currTemp;
    private String currIcon;
    private HttpURLConnection connection;

    /**
     * Constructs a Weather object, and initializes a connection with the OpenWeatherAPI service
     * @throws IOException If an error occurred connecting to the host server
     */
    public Weather() throws IOException {

        try {
            // Initialize an HTTP connection
            formConnection(); 
        }
        catch (Exception e) {
            
        }
    }

    /**
     * Attempts to form an HTTP connection with OpenWeatherAPI
     * @throws IOException If an error occurred connecting to the host server
     */
    public void formConnection() throws IOException {

        // Attempting to form an HTTP connection with the endpoint
        URL endpoint = new URL(this.API_ENDPOINT + this.GET_PARAMS);

        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) endpoint.openConnection();
        } catch (Exception e) {
            return;
        }

        // If the connection was unsuccessful, print an error message and terminate the method
        if ((conn == null) || (conn.getResponseCode() != HttpURLConnection.HTTP_OK)) {
            System.out.println("Error - failed to connect to OpenWeatherAPI.");
        }
        
        this.connection = conn;

    }

    /**
     * Retrieve the current weather conditions on Western University's campus
     * @throws IOException If an error occurred connecting to the host server
     */
    public void fetchCurrentWeather() throws IOException, ParseException {

        if (this.connection != null) {

            // If there is currently a valid connection, process the entire HTTP response body
            BufferedReader in = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
            StringBuilder raw_data = new StringBuilder();
            String currLine;

            while ((currLine = in.readLine()) != null) {
                raw_data.append(currLine);
            }

            // Convert the raw data into a JSON object
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(raw_data.toString());
            JSONArray weatherData = (JSONArray) data.get("weather");
            JSONObject weatherStatus = (JSONObject) weatherData.get(0);
            JSONObject tempData = (JSONObject) data.get("main");

            // Access the necessary fields, and update instance variables accordingly
            this.currCondition = weatherStatus.get("main").toString();
            this.currIcon = weatherStatus.get("icon").toString();
            this.currTemp = tempData.get("feels_like").toString();
            
        }

    }

    /**
     * Retrieve the current weather condition (e.g. rain, snow, sunny, cloudy, etc.)
     * @return The current weather condition
     */
    public String getCurrCondition() {
        return this.currCondition;
    }

    /**
     * Retrieve the current "feels-like" temperature
     * @return The current temperature (in Celsius)
     */
    public String getCurrTemp() {
        return this.currTemp + " C";
    }

    /**
     * Retrieve the icon corresponding to the current weather condition
     * @return The 3-digit icon code
     */
    public URL getCurrIcon() throws MalformedURLException {
        return new URL(this.ICON_URL + String.format("%s@2x.png", this.currIcon));
    }

    /**
     * Retrieve the connection status (used to check if there is a valid, non-null connection)
     * @return The HTTP connection object
     */
    public HttpURLConnection getConnection() {
        return this.connection;
    }
}
