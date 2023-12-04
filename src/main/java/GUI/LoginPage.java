package GUI;

//importing standard libraries
import app.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Renders the GUI for the login form of the GIS application
 * -text field and password field accepts uer details
 * -pressing login button authenticates user
 * -pressing register button directs user to account creation page
 * @author Sung Hyun Kim
 */
public class LoginPage extends JDialog {

    //Defining Swing GUI elements and relevant variables
    private JTextField idField;
    private JPasswordField pwField;
    private JButton logInButton;
    private JButton registerButton;
    private JPanel loginPanel;
    private JLabel logoImage;
    private JLabel noUserWarning;

    /**
     * constructor for the Login page. Loads and initialises the GUI
     * -login button action listener authenticates the user
     * -register button action listener directs user to register page
     */
    public LoginPage() {

        //constructor variables
        setTitle("Login Page");
        setContentPane(loginPanel);
        setSize(1400, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Login Page");
        registerButton.setOpaque(true);
        registerButton.setBorderPainted(false);
        logInButton.setOpaque(true);
        logInButton.setBorderPainted(false);
        noUserWarning.setVisible(false);
        setVisible(true);

        //action listener for login button pressing
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String id = idField.getText();
                String pw = String.valueOf(pwField.getPassword());
                boolean dev;

                //defining relative path for user data pathfinding
                Path dir = Paths.get("src/main/resources/User");
                File f = new File("src/main/resources/User");
                File[] listOfFiles = f.listFiles();
                JSONParser parser = new JSONParser();

                //validation/authentication of user login details
                for (File file : listOfFiles) {
                    Object o = null;
                    try {
                        o = parser.parse(new FileReader(file));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                    JSONObject accountCheck = (JSONObject) o;

                    String pwCheck = accountCheck.get("pw").toString();
                    String idCheck = accountCheck.get("id").toString();
                    dev = Boolean.parseBoolean(accountCheck.get("dev").toString());
                    if (id.equals(idCheck) && pw.equals(pwCheck)) {
                        User user = new User(id,pw,dev);
                        HomePage hp = new HomePage(user);
                        try {
                            hp.loadHomePage();
                            dispose();
                            break;
                        } catch (IOException ex) {
                            break;
                        } catch (ParseException ex) {
                            break;
                        }
                    }
                    noUserWarning.setVisible(true);

                }

            }
        });

        //pressing register button directs user to account creation page
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterPage rp = new RegisterPage();
                dispose();
            }
        });

    }

    public static void main(String[] args) {
        LoginPage lp = new LoginPage();
    }
}