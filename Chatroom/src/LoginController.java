import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class LoginController {
    private static String user;
    private static String password;
    private User currentEntry;
    private UserQueries personQueries;
    private int numberOfEntries = 0;
    private int currentEntryIndex = 0;
    private ChatController chatController;
    private Parent root;
    private Scene scene;
    private Stage stage;

    public LoginController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChatScreen.fxml"));
        fxmlLoader.setController(this);
        try{
            root = (Parent) fxmlLoader.load();
            scene = new Scene(root);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private TextField usernameText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private ComboBox<String> chatroomComboBox;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    @FXML
    private void loginButtonPressed(ActionEvent event){
        List<User> results;
        user = usernameText.getText();
        password = passwordText.getText();

        try{
            personQueries = new UserQueries();
            results = personQueries.getUser(user, password);
            numberOfEntries = results.size();

            if (numberOfEntries > 0){
                currentEntry = results.get(currentEntryIndex);

                if (currentEntry.getUsername().equals(user) && currentEntry.getPassword().equals(password)){
                        chatController = new ChatController();
                        chatController.openChat(stage, user, chatroomComboBox.getSelectionModel().toString());
                }
                else{
                    errorLabel.setText("Failure - Wrong Username/Password");
                }
            }
            else{
                errorLabel.setText("Failure - Wrong Username/Password");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void registerButtonPressed(ActionEvent event){
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
        catch(Exception e){

        }
    }

}
