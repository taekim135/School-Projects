/**
 * This class represents the overall layout of the map part of the application UI
 * It contains all the UI features needed to view the building map.
 * It is the window the user will see once they choose on the bulding.
 * @author Taegyun Kim
 * @author Oliver Clennan
 */

package GUI;

// Core Swing GUI libraries
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JLabel;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

// Additional supporting libraries
import app.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MapPage extends javax.swing.JFrame {

    /**
     * current building object
     */
    private Building currBuilding;

    /**
     * current floor displayed on the map
      */
    private Floor activeFloor;
    /**
     * all the POIs associated with the current map/floor
     */
    private POI[] mapPOIs;
    /**
     * user for this object
     */
    private User user;
    /**
     * Java Swing of a fram for this program
     */
    private JFrame mapFrame;
    /**
     * Swing panels for this program
     */
    private JPanel mapPanel, wrapperPanel;
    /**
     * Swing scroll panels for the map
     */
    private JScrollPane scrollPane;
    /**
     * Swing buttons for this program
     */
    private JButton discoveryB, favB, customB, viewB, addB, editB;
    /**
     * Swing label to define the map area on the screen
     */
    private JLabel mapArea;
    /**
     * Swing slider for zooming on the map
     */
    private JSlider zoomSlider;
    /**
     * Swing menu bar for the program
     */
    private JMenuBar menuBar;
    /**
     * Swing menu to change the floor
     */
    private JMenu floorMenu;
    /**
     * Swing array for all the POIs associated with the floor/building
     */
    private JSONArray POIList;
    /**
     * Swing buttons for Discovery tab
     */
    private JButton[] dButtons;
    private int poiID = 1;
    /**
     * setting the map size on the program window
     */
    private int mapWidth, mapHeight;
    /**
     * path for all the Map images
     */
    private final String baseMapPath = "src/main/resources/Maps/";
    /**
     * path for all JSON data files
     */
    private final String baseJSONPath = "src/main/resources/System Data/";

    /**
     * Displays the given error message in a pop-up window
     * @param errorMessage The contents (description) of the error message
     * @author Jiho Choi
     */
    private void displayErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error!", JOptionPane.ERROR_MESSAGE);
    }
    
    private void addDiscoveryButton(JButton button, int index) {
        
        this.dButtons[index] = button;
        mapPanel.add(button);
        mapPanel.revalidate();
        mapPanel.repaint();
        
    }

    /**
     * Initializes the POI discovery button on the right hand side panel of the window
     */
    private void initDiscovery() {

        // Initialize the discovery button
        this.discoveryB = new JButton("Discovery");
        final int numPOIs = this.activeFloor.getNumPOIs();
        this.dButtons = new JButton[numPOIs];
        
        final JButton[] buttons = this.dButtons;
        final POI[] pois = this.mapPOIs;
        boolean[] discoveryToggled = {false};

        // Displays a list of all POIs on the current floor plan map. (Discovery button)
        discoveryB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // If the POIs are already displayed, clear the list from the panel
                if (discoveryToggled[0]) {
                    
                    discoveryToggled[0] = false;
                    for (int i = 0; i < buttons.length; i++) {
                        mapPanel.remove(buttons[i]);
                        mapPanel.revalidate();
                        mapPanel.repaint();
                    }

                }

                // Otherwise, display list of POIs associated with the current map
                else {
                    
                    discoveryToggled[0] = true;
                    if (numPOIs != 0) {

                        // Display each individual POI as a button element
                        for (int i = 0; i < numPOIs; i++) {
                            
                            POI currPOI = pois[i];
                            JButton poiButton = new JButton(currPOI.getName());

                            // Clicking on the POI will display a PIN on the map and a pop-up window of the POI
                            poiButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        showPOI(currPOI);
                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                            });
                            
                            // Add the button to the map panel element
                            addDiscoveryButton(poiButton, i);

                        }

                    }

                }

            }

        });
        
        // Add the discovery button to the map panel
        mapPanel.add(discoveryB);
        
    }
    
    /**
     * Initializes/refreshes the zoom slider bar
     */
    private void initZoomSlider() {
        
        // Zoom slider implementation - allow user to zoom in or out of the current floor plan image
        this.zoomSlider = new JSlider(JSlider.HORIZONTAL, 75, 125, 100);
        zoomSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                int zoomValue = zoomSlider.getValue();
                double scaleFactor = zoomValue / 100.0;
                Building b = getCurrBuilding();
                int fNum = b.getActiveFloor().getFloorID();

                try {
                    loadMapImage(b, fNum, scaleFactor, false);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }

        });
        
        mapPanel.add(zoomSlider);
        
    }

    /**
     * Helper method for the search feature, which checks if a word is present in a larger piece of text
     * @param searchStr The word being searched for
     * @param otherStr The piece of text being searched
     * @return True if searchStr is a word in otherStr, false otherwise
     * @see searchForPOI() method
     */
    private boolean checkMembership(String searchStr, String otherStr) {

        // Divide the piece of text into tokens (using space as delimiter character)
        String[] tokens = otherStr.split(" ");

        // Iterate over all tokens in the token stream
        for (int i = 0; i < tokens.length; i++) {

            // Check to see if the current token matches the search string (i.e. if the search string is present in the text)
            if (tokens[i].equals(searchStr)) {
                return true;
            }

        }

        return false;

    }

    /**
     * Creates and configures the search field section on the menu bar
     */
    private void addSearchMenu() {

        // Configuring the search field element (including placeholder text)
        JTextField searchField = new JTextField("Search");
        searchField.setMaximumSize(new Dimension(200, 30));
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Search");
                }
            }
        });

        // Initiates a search for the POI if the user presses the enter key while the search field is active
        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String searchStr = searchField.getText();
                    try {
                        // If the search failed, display the error message popup, otherwise display the matching POI
                        if (!searchForPOI(searchStr)) {
                            displayErrorMessage("Search failed - could not find matching POI");
                            searchField.setText("");
                        }
                        searchField.setText("");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        this.menuBar.add(searchField);

    }

    /**
     * Adding an option to the menu bar that allows user to dynamically switch between different buildings
     */
    private void addBuildingMenu() {
        
        JMenuItem alumniHall = new JMenuItem("Alumni Hall");
        JMenuItem middlesexCollege = new JMenuItem("Middlesex College");
        JMenuItem naturalSciences = new JMenuItem("Natural Sciences Centre");

        // Switching to Alumni Hall
        alumniHall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Building newBuilding = new Building(1);
                    loadMapImage(newBuilding, 1, 1, true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Switching to Middlesex College
        middlesexCollege.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Building newBuilding = new Building(2);
                    loadMapImage(newBuilding, 1, 1, true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });

        // Switching to the Natural Sciences Building
        naturalSciences.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Building newBuilding = new Building(3);
                    loadMapImage(newBuilding, 1, 1, true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        
        JMenu buildingMenu = new JMenu("Change Building");
        buildingMenu.add(alumniHall);
        buildingMenu.add(middlesexCollege);
        buildingMenu.add(naturalSciences);
        menuBar.add(buildingMenu);
        
    }

    /**
     * Adding an option to the menu bar that allows the user to navigate between the different floors of a building
     */
    private void addFloorMenu() {
        
        this.floorMenu = new JMenu("Change Floor");

        // Iterate over each floor in the building
        for (int j = 1; j <= this.currBuilding.getNumFloors(); j++) {

            int floorNum = j;
            JMenuItem currItem = new JMenuItem("Floor " + floorNum);

            // Implementing an action listener to allow the user to switch between different floors of a building
            currItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        loadMapImage(currBuilding, floorNum, 1, true);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            floorMenu.add(currItem);

        }

        menuBar.add(floorMenu);
        
    }

    /**
     * Helper method which refreshes/updates the menu bar
     */
    private void updateMenu() {

        // Updates the dynamic (map-specific) components of the menu bar element
        addBuildingMenu();
        addFloorMenu();
        addSearchMenu();

    }

    /**
     * Initializes a window displaying the active floor plan image, and tools for the user to interact with the map
     * @author Taegyun Kim
     * @author Oliver Clennan
     */
    public MapPage(Building currBuilding) throws IOException, ParseException {
        
        // Initializes a main frame
        mapFrame = new JFrame();
        mapFrame.setSize(1400, 750);
        mapFrame.setLocationRelativeTo(null);
        POIList = new JSONArray();
        mapPanel = new JPanel();
        mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configuring GUI elements
        mapFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
        mapPanel.setLayout(new FlowLayout());
        mapPanel.setBackground(Color.lightGray);
        mapPanel.setPreferredSize(new Dimension(500, 650));
        
        // Initialize a menu bar object
        MenuBar mb = new MenuBar(null);
        menuBar = mb.attachMenuBar(mapFrame);
        
        // Set the active floor and building
        this.currBuilding = currBuilding;
        this.activeFloor = currBuilding.getActiveFloor();
        mapFrame.setTitle(this.currBuilding.getName() + " - Floor " + this.currBuilding.getActiveFloor().getFloorID());
        
    }

    /**
     * Retrieves the current building object
     * @return The active building
     */
    public Building getCurrBuilding() {
        return this.currBuilding;
    }

    /**
     * Retrieves the current floor object
     * @return The active floor
     */
    public Floor getCurrFloor() {
        return this.currBuilding.getActiveFloor();
    }

    /**
     * Saves the given POI to the JSON file corresponding to the current building and floor
     * @param p The POI to be saved/appended as a JSON object
     * @author Taegyun Kim
     * @author Oliver Clennan
     */
    public void savePOI(POI p) {
        
        // Initializes a JSON object to represent the given POI
        JSONObject POIINFO = new JSONObject();
        POIINFO.put("name", p.getName());
        POIINFO.put("roomNumber", p.getRoomNumber());
        POIINFO.put("description", p.getDescription());
        
        JSONArray position = new JSONArray();
        position.add(p.getPosition()[0]);
        position.add(p.getPosition()[1]);
        
        POIINFO.put("mapPosition", position);
        POIINFO.put("locationDesc", p.getLocationDesc());
        POIINFO.put("category", p.getCategory());
        
        POIList.add(POIINFO);

        // Update the corresponding JSON file
        try (FileWriter file = new FileWriter(baseJSONPath + this.currBuilding.getCode() + this.activeFloor.getFloorID() + ".json", false)) {
            file.write(POIList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Displays a pop-up window containing the metadata associated with the selected POI
     * @param poiToShow The POI to be displayed on the map
     * @author Taegyun Kim
     */
    public void showPOI(POI poiToShow) throws IOException {

        JLabel pinLabel = new JLabel();
        Image pinImage = ImageIO.read(new File("src/main/resources/Images/pin.jpeg"));
        ImageIcon pinIcon = new ImageIcon(pinImage);
        pinLabel.setIcon(pinIcon);
        mapFrame.add(pinLabel);
        
        // Pop-up window displaying POI metadata
        JOptionPane.showMessageDialog(mapFrame, poiToShow.getName() +
                        "\n Room #: " + poiToShow.getRoomNumber() +
                        "\n Description: " + poiToShow.getDescription() +
                        "\n Capacity: " + poiToShow.getCapacity() +
                        "\n Floor: " + poiToShow.getFloor() +
                        "\n Category: " + poiToShow.getCategory()
                , "POI Metadata", JOptionPane.PLAIN_MESSAGE);

    }

    /**
     * Renders a form where the user/developer can specify the metadata for a new POI at the given coordinates
     * @param xPos The x position on the floor map
     * @param yPos The y position on the floor map
     * @author Jiho Choi
     */
    public void createPOI(float xPos, float yPos) {
        
        // if edit is clicked

        // Edit Custom POI
        // Create textfields to be typed while editing Custom POI
        JTextField nameField = new JTextField(20);
        JTextField roomNumberField = new JFormattedTextField(new NumberFormatter());
        JTextField descriptionField = new JTextField();
        JTextField locationDescField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField capacityField = new JFormattedTextField(new NumberFormatter());

        JPanel POIPanel = new JPanel();
        POIPanel.setLayout(new BoxLayout(POIPanel, BoxLayout.Y_AXIS));
        // Custom POI name edit option
        nameField.setColumns(10);
        POIPanel.add(new JLabel("Name:"));
        POIPanel.add(nameField);

        // Room number edit option
        roomNumberField.setColumns(10);
        roomNumberField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if(((JFormattedTextField)e.getSource()).getText().length()>2)
                    e.consume();
            }
        });

        POIPanel.add(new JLabel("Room #:"));
        POIPanel.add(roomNumberField);

        descriptionField.setColumns(10);
        POIPanel.add(new JLabel("Description:"));
        POIPanel.add(descriptionField);

        locationDescField.setColumns(10);
        POIPanel.add(new JLabel("Location Description:"));
        POIPanel.add(locationDescField);

        categoryField.setColumns(10);
        POIPanel.add(new JLabel("Category:"));
        POIPanel.add(categoryField);

        // Room Capacity edit option
        capacityField.setColumns(10);
        capacityField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if(((JFormattedTextField)e.getSource()).getText().length()>2)
                    e.consume();
            }
        });
        
        POIPanel.add(new JLabel("Capacity:"));
        POIPanel.add(capacityField);
        
        float currPosition[] = {xPos, yPos};
        
        int result = JOptionPane.showConfirmDialog(this, POIPanel,
                "Create POI", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        // If the user successfully submitted the form, create a new POI object, and write it to the JSON file
        if (result == JOptionPane.OK_OPTION) {
            POI newPOI = new POI(poiID, nameField.getText(),Integer.parseInt(roomNumberField.getText()), descriptionField.getText(),
                    currPosition, locationDescField.getText(),categoryField.getText(),Integer.parseInt(capacityField.getText()),
                   this.activeFloor.getFloorID());
            
            savePOI(newPOI);
        }
        
        else {
            displayErrorMessage("Failed to submit the form - please try again!");
        }
        
    }

    /**
     * Renders a form where the user can use edit their custom POIs, and the developer can use to edit built-in POIs
     * @param editPoi The point of interest to be modified
     * @author Jiho Choi
     */
    public void editPOI(POI editPoi){

        // if edit is clicked

        // Edit Custom POI
        // Create textfields to be typed while editing Custom POI
        JTextField nameField = new JTextField(20);
        JTextField roomNumberField = new JFormattedTextField(new NumberFormatter());
        JTextField descriptionField = new JTextField();
        JTextField locationDescField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField capacityField = new JFormattedTextField(new NumberFormatter());
        JTextField floorField = new JFormattedTextField(new NumberFormatter());

        JPanel POIPanel = new JPanel();
        POIPanel.setLayout(new BoxLayout(POIPanel, BoxLayout.Y_AXIS));
        
        // Custom POI name edit option
        nameField.setColumns(10);
        POIPanel.add(new JLabel("Name:"));
        POIPanel.add(nameField);

        // Room number edit option
        roomNumberField.setColumns(10);
        roomNumberField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if(((JFormattedTextField)e.getSource()).getText().length()>2)
                    e.consume();
            }
        });

        POIPanel.add(new JLabel("Room #:"));
        POIPanel.add(roomNumberField);

        descriptionField.setColumns(10);
        POIPanel.add(new JLabel("Description:"));
        POIPanel.add(descriptionField);

        locationDescField.setColumns(10);
        POIPanel.add(new JLabel("Location Description:"));
        POIPanel.add(locationDescField);

        categoryField.setColumns(10);
        POIPanel.add(new JLabel("Category:"));
        POIPanel.add(categoryField);

        // Room Capacity edit option
        capacityField.setColumns(10);
        capacityField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if(((JFormattedTextField)e.getSource()).getText().length()>2)
                    e.consume();
            }
        });
        POIPanel.add(new JLabel("Capacity:"));
        POIPanel.add(capacityField);

        // Floor edit option
        floorField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if(((JFormattedTextField)e.getSource()).getText().length()>0)
                    e.consume();
            }
        });
        floorField.setColumns(10);
        POIPanel.add(new JLabel("Floor #:"));
        POIPanel.add(floorField);
        
        float currPosition[] = {244, 333};

        int result = JOptionPane.showConfirmDialog(this, POIPanel,
                "Create POI", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            new POI(editPoi.getID(), nameField.getText(),Integer.parseInt(roomNumberField.getText()), descriptionField.getText(),
                    currPosition, locationDescField.getText(),categoryField.getText(),Integer.parseInt(capacityField.getText()),
                    Integer.parseInt(floorField.getText()));
        }

    }

    /**
     * Searches for a POI on the currently displayed floor plan map, and displays it if found
     * @param p The search string entered by the user
     * @throws IOException If there is an error retrieving the POIs from JSON files
     * @return True if a matching POI is found, false otherwise
     * @see showPOI() method
     */
    public boolean searchForPOI(String searchStr) throws IOException {
        
        Floor currFloor = this.currBuilding.getActiveFloor();
        POI[] validPOIs = currFloor.getPOIs();
        
        // Iterate over all POIs associated with the current floor
        for (int i = 0; i < currFloor.getNumPOIs(); i++) {
            
            // Retrieve the current POIs metadata attributes
            POI currPOI = validPOIs[i];
            
            if (currPOI != null) {

                String fullName = currPOI.getName();
                String desc = currPOI.getDescription();
                String locDesc = currPOI.getLocationDesc();

                // Check to see if the search string matches POI attributes such as name, category, or part of desc/location desc
                if ((searchStr.equals(fullName)) || (checkMembership(searchStr, fullName)) || (checkMembership(searchStr, desc)) || (checkMembership(searchStr, locDesc))) {

                    // If so, display/highlight the POI on the window
                    showPOI(currPOI);
                    return true;

                }
                
            }
            
        }
        
        return false;
        
    }

    /**
     * Displays all POIs associated with the current floor plan map (via a pin icon)
     * @throws IOException If there is an issue reading/accessing the pin image from the project directory
     */
    public void loadPOIs() throws IOException {

        for (int i = 0; i < currBuilding.getActiveFloor().getNumPOIs(); i++) {

            // Create a new icon with the pin image
            JLabel pinLabel = new JLabel();
            BufferedImage pinImg = ImageIO.read(new File("src/main/resources/Images/pin.jpeg"));
            pinLabel.setIcon(new ImageIcon(pinImg));
            
            // Place the pin at the POIs coordinates
            float[] currPosition = this.mapPOIs[i].getPosition();
            pinLabel.setLocation((int)currPosition[0], (int)currPosition[1]);
            pinLabel.setSize(new Dimension(30, 30));
            wrapperPanel.add(pinLabel);
            scrollPane.add(wrapperPanel);

        }

    }

    /**
     * Helper method which loads, and appropriately scales a floor plan image
     * @param currBuilding The current building object
     * @param floorNum The current floor index
     * @param scale The scale factor (based on the zoom JSlider)
     * @param reset Indicates whether the image is to be reset to original/default dimensions
     * @throws IOException If there is an error locating the floor plan image file in the project directory
     *
     * @author Oliver Clennan
     */
    public void loadMapImage(Building currBuilding, int floorNum, double scale, boolean reset) throws IOException {

        // Updating the current building and floor
        this.currBuilding = currBuilding;
        this.currBuilding.setActiveFloor(floorNum);
        this.activeFloor = currBuilding.getActiveFloor();
        this.mapPOIs = this.currBuilding.getActiveFloor().getPOIs();
        this.dButtons = new JButton[this.activeFloor.getNumPOIs()];

        // Updating GUI elements to reflect the new active floor plan map
        updateMenu();
        
        if (reset) {
            
            this.mapWidth = 800;
            this.mapHeight = 650;
            this.zoomSlider.setValue(100);
            initDiscovery();
            
        }
        
        // Scaling the dimensions of the current image as necessary
        int resWidth = (int)(mapWidth*(scale));
        int resHeight = (int)(mapHeight*(scale));

        // Loads the floor plan image with the desired dimensions, and scales it if necessary (in the case of zooming)
        BufferedImage mapImg = ImageIO.read(new File(baseMapPath + this.currBuilding.getName() + "/floor" + floorNum + ".png"));
        Image newImage = mapImg.getScaledInstance(resWidth, resHeight, Image.SCALE_SMOOTH);
        mapArea.setIcon(new ImageIcon(newImage));
        mapArea.setPreferredSize(new Dimension(mapWidth, mapHeight));
        
        // Allows the user to create new POIs
        mapArea.addMouseListener(new MouseListener() {
            /**
             * Initiate the request to add a new POI at the current coordinates
             * @param e the event to be processed
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                createPOI(e.getX(), e.getY());
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        // Appropriately updating the title of the window to reflect the current building and floor
        mapFrame.setTitle(this.currBuilding.getName() + " - Floor " + this.activeFloor.getFloorID());
        mapWidth = resWidth;
        mapHeight = resHeight;
        
        // Adding the map image to the viewport of the scrolling pane object
        scrollPane.setViewportView(mapArea);

    }

    /**
     * Renders the page which allows the users to view and interact with the floor plan maps
     * @throws IOException If there is an error locating the floor plan in the project directory
     * @throws ParseException If there is an error processing the JSON weather data (in weather section of menu bar)
     *
     * @author Taegyun Kim
     * @author Oliver Clennan
     */
    public void loadMapPage() throws IOException, ParseException {

        // Loading in the appropriate floor plan image
        mapArea = new JLabel();
        wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new OverlayLayout(wrapperPanel));
        
        scrollPane = new JScrollPane();
        scrollPane.add(wrapperPanel);

        // buttons for map view
        favB = new JButton("Fav");
        customB = new JButton("Custom");

        initZoomSlider();
        loadMapImage(this.currBuilding, 1, 1, true);

        // buttons for cusomizing POIs
        // added after acceptance testing by TA
        viewB = new JButton("View");
        editB = new JButton("Edit");
        addB = new JButton("Add");
        
        // Display the cucstom layout for user custom POIs
        // Feature to allow the user to edit and save POIs
        // added after acceptance testing by TA
        customB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                mapPanel.add(viewB);
                mapPanel.add(editB);
                mapPanel.add(addB);

                List<POI> custom = new ArrayList<>();
                custom = user.getCustomPOIs();
                if (custom.size() != 0) {

                    // Display each POI as a clickable button
                    for (int i = 0; i < custom.size(); i++) {

                        POI currPOI = custom.get(i);
                        JButton poiButton = new JButton(currPOI.getName());

                        // Clicking on the POI will display a PIN on the map and a pop-up window of the POI
                        poiButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    showPOI(currPOI);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        });
                        mapPanel.add(poiButton);
                    }
                }
                mapFrame.add(mapPanel);
                mapFrame.setVisible(true);
            }
        });

        // display user's Favourites POIs when "Fav" button is clicked
        // this code is added after acceptance testing by TA
        favB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<POI> fav = new ArrayList<>();
                fav = user.getFavourites();

                if (fav.size() != 0) {

                    // Display each POI as a clickable button
                    for (int i = 0; i < fav.size(); i++) {

                        POI currPOI = fav.get(i);
                        JButton poiButton = new JButton(currPOI.getName());

                        // Clicking on the POI will display a PIN on the map and a pop-up window of the POI
                        poiButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    showPOI(currPOI);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        });
                        mapPanel.add(poiButton);
                    }
                }
                mapFrame.add(mapPanel);
                mapFrame.setVisible(true);
            }
        });
        
        mapPanel.add(favB);
        mapPanel.add(customB);
        mapPanel.add(zoomSlider);
        
        mapFrame.add(scrollPane);
        mapFrame.add(mapPanel);
        mapFrame.setJMenuBar(menuBar);
        mapFrame.setVisible(true);

    }

    /**
     * main method for this class. Used for testing the applicaiton window before merging to other branches
     * @param args
     * @throws IOException
     * @throws ParseException
     * @author Taegyun Kim
     * @author Oliver Clennan
     */
    public static void main(String[] args) throws IOException, ParseException {
        
        // Supported Buildings:
        // Alumni Hall [1]
        // Middlesex College [2]
        // Natural Sciences Centre [3]
        Building defaultBuilding = new Building(1);
        MapPage test = new MapPage(defaultBuilding);
        test.loadMapPage();

    }

}
