package GUI;

//importing standard libraries
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Renders the GUI for the account creation form of the GIS application
 * -text field and password field accepts uer details
 * -pressing create button performs following actions
 *      1. check if password confirmation is equal as the original password
 *      2. if password is confirmed, create new JSON file with the relevant id, password data
 *      3. direct user back to the login page
 * @author Sung Hyun Kim
 */
public class RegisterPage extends JDialog{

    //Defining Swing GUI elements and relevant variables
    private JPanel registerPanel;
    private JTextField idField;
    private JPasswordField pwField;
    private JPasswordField pwConfirmField;
    private JButton createButton;
    private JLabel logoImage;
    private JCheckBox devCheckBox;
    private JLabel existingUserWarning;
    private JButton returnButton;
    private JLabel pwUnmatchWarning;

    /**
     * constructor for the Register page. Loads and initialises the GUI
     * -jcheckbox action listener determines if the new account created is dev account
     * -create action listener allows creation of new account (if account does not already exist)
     */
    public RegisterPage() {

        //constructor variables
        setTitle("Register Page");
        setContentPane(registerPanel);
        setSize(1400, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createButton.setOpaque(true);
        createButton.setBorderPainted(false);
        pwUnmatchWarning.setVisible(false);
        existingUserWarning.setVisible(false);
        setVisible(true);

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginPage lp = new LoginPage();
                dispose();
            }
        });

        //action for pressing account creation button
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pwUnmatchWarning.setVisible(false);
                existingUserWarning.setVisible(false);
                String id = idField.getText();
                String pw = String.valueOf(pwField.getPassword());
                String pwCheck = String.valueOf(pwConfirmField.getPassword());

                //defining relative path for user data pathfinding
                Path dir = Paths.get("src/main/resources/User");
                File f = new File("src/main/resources/User");
                File[] listOfFiles = f.listFiles();
                JSONParser parser = new JSONParser();
                boolean showIDWarn = false;
                boolean showPWWarn = false;
                boolean makeAccount = true;
                boolean isDev = false;

                //checking if the new account is dev account
                if (devCheckBox.isSelected()) {
                    isDev = true;
                }

                for (File file : listOfFiles) {
                    Object o = null;
                    try {
                        o = parser.parse(new FileReader(file));
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                    JSONObject accountCheck = (JSONObject) o;
                    String existingID = accountCheck.get("id").toString();
                    //checking if id already exists
                    if (id.equals(existingID)) {
                        makeAccount = false;
                        showIDWarn = true;
                    }
                }
                if (makeAccount == true) {
                    //authenticating password confirmation
                    if (pw.equals(pwCheck)) {
                        JSONObject userData = new JSONObject();
                        userData.put("id", id);
                        userData.put("pw", pw);
                        //checking boolean value for Dev status
                        if (isDev == true) {
                            userData.put("dev", "true");
                        } else {
                            userData.put("dev", "false");
                        }

                        //creating json file with username as title
                        String filename = id + ".json";
                        try (PrintWriter out = new PrintWriter(new FileWriter("src/main/resources/User/" + filename))) {
                            out.write(userData.toString());
                        } catch (Exception a) {
                            a.printStackTrace();
                        }
                        LoginPage lp = new LoginPage();
                        dispose();
                    } else {
                        showPWWarn = true;
                    }
                }
                //show error message for either pw unmatch or existing user id
                if (showIDWarn == true) {
                    existingUserWarning.setVisible(true);
                } else if (showPWWarn == true) {
                    pwUnmatchWarning.setVisible(true);
                }
                showIDWarn = false;
                showPWWarn = false;


            }
        });
    }

    public static void main(String[] args) {
        RegisterPage rp = new RegisterPage();
    }
}
