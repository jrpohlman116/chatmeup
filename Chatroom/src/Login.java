import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class Login extends JFrame{
    private static String user;
    private static String password;
    private User currentEntry;
    private UserQueries personQueries;
    private int numberOfEntries = 0;
    private int currentEntryIndex = 0;
    private JTextField usernameText = new JTextField("Username");
    private JTextField passwordText = new JTextField("Password");
    private JLabel selectLabel = new JLabel("Select Chatroom");
    private String[] chatroomsArray = {"Software Design", "Group Nine"};
    private JComboBox<String> chatroomsComboBox = new JComboBox<String>(chatroomsArray);
    private JButton loginButton = new JButton("Login");
    private JButton registerButton = new JButton("Register");
    private JLabel errorLabel = new JLabel("Incorrect Username/Password");
    private RegisterHandler rHandler = new RegisterHandler();
    private LoginHandler lHandler = new LoginHandler();

    // initialize chatServer and set up GUI
    public Login() {
        super("Login");

        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 5, 10, 5);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 4;
        constraints.gridheight = 1;

        Icon logo = new ImageIcon(getClass().getResource("ChatMeUp.png"));
        JLabel label = new JLabel("", logo, SwingConstants.CENTER);
        add(label, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        add(selectLabel, constraints);

        constraints.gridx = 1;
        chatroomsComboBox.setSelectedItem("Software Design");
        add(chatroomsComboBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 4;
        add(usernameText, constraints);

        constraints.gridy = 3;
        add(passwordText, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        loginButton.addActionListener(lHandler);
        add(loginButton, constraints);

        constraints.gridx = 1;
        registerButton.addActionListener(rHandler);
        add(registerButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 4;
        errorLabel.setVisible(false);
        add(errorLabel, constraints);
    } // end Client constructor

    private class LoginHandler implements ActionListener{

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            List<User> results;
            user = usernameText.getText();
            password = passwordText.getText();

            try{
                personQueries = new UserQueries();
                results = personQueries.getUser(user, password);
                numberOfEntries = results.size();

                if (numberOfEntries > 0){
                    currentEntry = results.get(currentEntryIndex);

                    if (currentEntry.getUsername().equals(user) && currentEntry.getPassOrMessage().equals(password)){
                        Client client;

                        chatroomsComboBox.getSelectedIndex();

                        if (chatroomsComboBox.getSelectedItem().toString().equals("Software Design")){
                            client = new Client("chatRoomA", user);
                        }
                        else {
                            client = new Client("chatRoomB", user);
                        }

                    }
                    else{
                        errorLabel.setText("Failure - Wrong Username/Password");
                        errorLabel.setVisible(true);
                    }
                }
                else{
                    errorLabel.setText("Failure - Wrong Username/Password");
                    errorLabel.setVisible(true);
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private class RegisterHandler implements ActionListener{

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            int results;
            user = usernameText.getText();
            password = passwordText.getText();

            try{
                personQueries = new UserQueries();
                results = personQueries.addUser(user, password);

                if (results == 1){
                    errorLabel.setText("Successful Registration.\nPlease Select Login.");
                }
                else{
                    errorLabel.setText("Error Try Again");
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

}
