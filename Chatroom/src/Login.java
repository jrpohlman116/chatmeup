import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/***********************************
 * public class extending JFrame to
 * execute the login swing GUI froma
 ***********************************/
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
    private JLabel errorLabel = new JLabel("");
    private RegisterHandler rHandler = new RegisterHandler();
    private LoginHandler lHandler = new LoginHandler();

    /********************************
     * Login constructor to initialize
     * the GUI frame
     ********************************/
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
        add(errorLabel, constraints);
    } // end Client constructor

    /********************************
     * private class implementing an
     * Action listener to handle
     * the login button command
     ********************************/
    private class LoginHandler implements ActionListener{

        /*************************************************
         * button press event that creates a database query
         * to select username and password provided by the
         * user and start the client based on the results
         * @param e     actionevent
         *************************************************/
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            List<User> results; //results from the select query
            user = usernameText.getText();
            password = passwordText.getText();

            try{
                personQueries = new UserQueries();
                results = personQueries.getUser(user, password); //find the user in the database table
                numberOfEntries = results.size(); //number of entries in the table per given user

                if (numberOfEntries > 0){
                    currentEntry = results.get(currentEntryIndex);

                    //if the entry matches the username and password given
                    if (currentEntry.getUsername().equals(user) && currentEntry.getPassOrMessage().equals(password)){
                        Client client;

                        //start client based on chatroom selected
                        if (chatroomsComboBox.getSelectedItem().toString().equals("Software Design")){
                            client = new Client("chatRoomA", user);
                        }
                        else {
                            client = new Client("chatRoomB", user);
                        }

                    }
                    else{
                        errorLabel.setText("Failure - Wrong Username/Password");
                    }
                }
                else{
                    errorLabel.setText("Failure - Wrong Username/Password");
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /********************************
     * private class implementing an
     * Action listener to handle
     * the register button command
     ********************************/
    private class RegisterHandler implements ActionListener{

        /*************************************************
         * button press event that creates a database query
         * to create username and password provided by the
         * user if it does not already exist in the table
         * @param e     actionevent
         *************************************************/
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            int results;
            List<User> selectResults;
            user = usernameText.getText();
            password = passwordText.getText();

            try{
                //if the user and password fields are empty, provide an error
                if (!user.equals("") && !password.equals("")){
                    personQueries = new UserQueries();
                    //Find and select user based on input
                    selectResults = personQueries.getUser(user, password);
                    numberOfEntries = selectResults.size();

                    //if the user is not found in the database, create new
                    if (numberOfEntries == 0 ){
                        results = personQueries.addUser(user, password);

                        //provide feedback
                        if (results == 1){
                            errorLabel.setText("Successful Registration.\nPlease Select Login.");
                        }
                        else{
                            errorLabel.setText("Error - Unable to Register");
                        }
                    }
                    else{
                        errorLabel.setText("Error: Username not available");
                    }


                }
                else{
                    errorLabel.setText("Username/Password cannot be blank");
                }

            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

}
