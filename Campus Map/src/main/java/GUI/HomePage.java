package GUI;

// Standard libraries
import app.Building;
import app.Weather;
import app.User;
import org.json.simple.parser.ParseException;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

// Libraries required to construct and interact with homepage GUI
import javax.swing.*;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Renders the GUI for the homepage of the GIS application, containing the following elements:
 * - A menu bar giving easy access to the about section, help guide, and current weather {@link MenuBar}
 * - A JComboBox element allowing the user to select a supported building
 * - An image of Western's campus (or a particular building, if one is selected)
 *
 * @author Oliver Clennan
 */
public class HomePage extends JFrame {
    
    // Defining Swing GUI elements, and other relevant instance variables
    private JFrame homeFrame;
    private JSplitPane homeText;
    private JSplitPane mainSplit;
    private JLabel campusImage;
    private JComboBox buildingSelect;
    private JButton exploreButton;
    private final String[] buildingOptions = {"Select Building", "Alumni Hall", "Middlesex College", "Natural Sciences Centre"};
    private final String imageBasePath = "src/main/resources/Images/";
    private final String defaultImage = "western_campus.jpg";
    private Weather weatherAPI;
    private User currUser;

    /**
     * Constructs and initializes the homepage GUI window via Swing
     * @throws IOException If an error occurred while connecting to the weather API service {@link Weather}
     */
    public HomePage(User currUser) {
        
        // Creating, and configuring a frame to represent the page
        homeFrame = new JFrame();
        homeFrame.setLayout(new GridLayout());
        homeFrame.setSize(1400, 750);
        homeFrame.setLocationRelativeTo(null);
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeFrame.setTitle("Home Page");
        
        this.currUser = currUser;
        
    }

    /**
     * Helper method which loads an image into the JLabel element on the right portion of the homepage
     * @param fileName The file path of the image
     * @throws IOException If there is an error reading the image (i.e. invalid file path)
     */
    public void loadImage(String fileName) throws IOException {

        int newWidth = 1050;
        int newHeight = 750;
        
        // Read, and scale the image to appropriately fit the panel
        BufferedImage unscaledImage = ImageIO.read(new File(imageBasePath + fileName));
        Image scaledImage = unscaledImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        
        // Attaching the image icon to the JPanel element
        ImageIcon campusIcon = new ImageIcon(scaledImage);
        campusImage.setIcon(campusIcon);
        
    }

    /**
     * Responsible for rendering all the elements on the homepage window
     * @throws IOException If there is an error connecting to the weather API service
     * @throws ParseException If there is an error parsing the JSON weather data
     */
    public void loadHomePage() throws IOException, ParseException {
        
        // Attaching the menu bar to the top of the home page
        MenuBar mb = new MenuBar(currUser);
        mb.attachMenuBar(homeFrame);

        exploreButton = new JButton();
        exploreButton.setText("Explore");
        exploreButton.setEnabled(false);
        
        // Stores names of buildings supported by the application
        buildingSelect = new JComboBox(buildingOptions);
        final int[] buildingIndex = new int[1];
        
        // If the user selects a building from the list, display the image of that building on the frame
        buildingSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedBuilding = buildingSelect.getSelectedIndex();
                buildingIndex[0] = selectedBuilding;
                switch (selectedBuilding) {
                    // Default image of Western's campus
                    case 0:
                        try {
                            loadImage(defaultImage);
                            exploreButton.setEnabled(false);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    // Image of Alumni Hall
                    case 1:
                        try {
                            loadImage("alumni_hall.jpg");
                            exploreButton.setEnabled(true);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    // Image of Middlesex College
                    case 2:
                        try {
                            loadImage("middlesex_college.jpg");
                            exploreButton.setEnabled(true);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    // Image of Natural Sciences Centre
                    case 3:
                        try {
                            loadImage("natural_sciences.jpg");
                            exploreButton.setEnabled(true);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                }
            }
        });
        
        // Load the default image onto the JLabel
        campusImage = new JLabel();
        loadImage(defaultImage);
        
        /**
         * If the explore button is clicked (and a valid building is selected), render the map of that building {@link MapPage}
         */
        exploreButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MapPage mp = null;
                try {
                    mp = new MapPage(new Building(1));
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    mp.loadMapPage();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Vertically splitting the frame into two equally-sized sections
        // The left side contains the building options, and explore button
        // The right side contains the image of Western's campus, or a selected building
        homeText = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buildingSelect, exploreButton);
        homeText.setDividerSize(0);
        homeText.setDividerLocation(homeFrame.getHeight()/2);
        
        // Horizontally splitting the left section of the aforementioned split
        mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, homeText, campusImage);
        mainSplit.setDividerSize(0);
        mainSplit.setDividerLocation(350);

        // Attaching elements to panels, and panels to frames
        homeFrame.add(mainSplit);
        homeFrame.setVisible(true);
        
    }
    public static void main (String[] args) throws IOException, ParseException {
        
        HomePage hp = new HomePage(null);
        hp.loadHomePage();
        
    }
    
}
