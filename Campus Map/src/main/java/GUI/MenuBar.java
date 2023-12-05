package GUI;

// Relevant libraries to build menu bar GUI
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import app.Weather;
import app.User;
import org.json.simple.parser.ParseException;

/**
 * Renders the menu bar - an element visible on the top of each page in the application (excluding the login page)
 * The menu bar contains the following sections:
 * - Home, a button that if clicked, redirects the user to the homepage window
 * - About, a button that if clicked, displays a pop-up window containing information about the application and its developers
 * - Help, a button that if clicked, redirects the user to an external PDF help guide on how to use the application
 * - Weather, which displays a dropdown window with the current weather conditions
 * - Change Building, a dropdown menu that allows the user to easily navigate between different buildings
 * 
 * @author Oliver Clennan
 */
public class MenuBar {

    // Relevant GUI elements for the menu bar
    private JMenuBar menuBar;
    private JMenu weatherMenu;
    private JButton aboutButton;
    private JButton helpButton;
    private JButton homeButton;
    private JOptionPane aboutPop;
    private Weather weatherAPI;
    private User currUser;

    /**
     * Constructs and initializes the menu bar GUI
     * @throws IOException If there is an issue connecting to the weather API service
     */
    public MenuBar(User currUser) throws IOException {
        
        // Initialize connection to weather API
        weatherAPI = new Weather();
        this.currUser = currUser;
        
    }

    /**
     * Creates and populates a menu bar object
     * @return The created JMenuBar
     * @throws IOException If there is an error retrieving the weather icon
     * @throws ParseException If there is an error processing the JSON weather data
     */
    public JMenuBar attachMenuBar(JFrame mainFrame) throws IOException, ParseException {
        
        menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(800, 25));

        // Fetching the current weather conditions on campus
        weatherAPI.fetchCurrentWeather();

        // Creating buttons to access about section, help guide, and homepage window
        aboutButton = new JButton("About");
        helpButton = new JButton("Help Guide");
        homeButton = new JButton("Home");

        // Adding the above items to the menu bar
        menuBar.add(homeButton);
        menuBar.add(aboutButton);
        menuBar.add(helpButton);

        // If the user is currently accessing the application offline, do not include the weather functionality
        if (weatherAPI.getConnection() != null) {

            // Configuring the weather section
            URL iconURL = weatherAPI.getCurrIcon();
            JMenuItem weatherIcon = new JMenuItem(new ImageIcon(iconURL));
            weatherMenu = new JMenu("Weather");
            weatherMenu.add(weatherIcon);
            weatherMenu.add(new JMenuItem(weatherAPI.getCurrTemp()));
            weatherMenu.add(new JMenuItem(weatherAPI.getCurrCondition()));
            menuBar.add(weatherMenu);

        }
        
        // Redirects the user to the homepage if they click the "Home" button on the menu bar
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HomePage hp = new HomePage(null);
                try {
                    hp.loadHomePage();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        
        // Configure about section
        String aboutText = "UWO Geographic Information System\nVersion: 1.0\nReleased: April. 4th, 2023\n\nDevelopers:\nTaegyun Kim | tkim363@uwo.ca\nOliver Clennan | oclennan@uwo.ca\nSung Hyun Kim | skim2536@uwo.ca\nJiho Choi | jchoi525@uwo.ca\n\nCopyright @ 2023\nCS 2212B";
        Image bi = ImageIO.read(new File("src/main/resources/Images/uwo_logo.jpg"));
        bi = bi.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon ic = new ImageIcon(bi);
        
        // Implement about pop-up window when about menu button is pressed
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainFrame, aboutText, "About", JOptionPane.INFORMATION_MESSAGE, ic);
            }
            
        });
        
        // Open external pdf help guide document when help button is pressed
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        File helpGuide = new File("src/main/resources/System Data/help_guide.pdf");
                        Desktop.getDesktop().open(helpGuide);
                    }
                    catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        
        // Attach the menu bar to the main frame of the page
        mainFrame.setJMenuBar(menuBar);
        
        return menuBar;
        
    }
    
}
