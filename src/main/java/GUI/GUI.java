package GUI;

// Libraries required to construct, and interact with GUI
import app.*;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.text.NumberFormatter;
import java.awt.*;

/**
 * Represents the application's visual user interface.
 * Processes user input, and carries out user commands as needed.
 * 
 * The GUI is entirely designed, and constructed using the Java Swing library.
 * 
 * Note: this class is the highest-level class in the project - it is responsible for processing
 * high-level user instructions, and interacting with the lower-level, feature-specific classes to do so.
 * 
 * @author Oliver Clennan
 * @author Taegyun Kim
 * @author Sung Kim
 * @author Jiho Choi
 * @version 1.0
 */
class GUI extends JFrame {
    
    // Declaring relevant GUI attributes
    private Building currBuilding;
    private Floor currFloor;
    private static int buildingType;

    private float[] currPosition = new float[2];
    private float zoomFactor;
    private Weather weatherAPI;
    private JLabel pointOfInterest = new JLabel();

    private JFrame frame;
    private HomePage homePage;
    int id = 0;

    /**
     * Constructor for this class
     * It initializes the application window
     * @throws IOException
     */
    public GUI() throws IOException {
        
        frame = new JFrame();
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);

        // what happens when frame/window closes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //window title
        frame.setTitle("UWO Building Search");
        this.weatherAPI = new Weather();

    }

    /**
     * Helper method which initializes the login page GUI
     */
    public void loadLoginPage() {
        LoginPage lp = new LoginPage();
    }

    /**
     * Main method to run the application - will redirect to the login/register page
     * @param args 
     * @throws IOException
     * @throws ParseException
     */
    public static void main(String[] args) throws IOException, ParseException {
        
        // Initialize application, and proceed to load the login page
        GUI master = new GUI();
        master.loadLoginPage();

    }
    
}