import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController {
    private static String user;
    private static String password;
    private User currentEntry;
    private UserQueries personQueries;
    private int numberOfEntries = 0;
    private int currentEntryIndex = 0;

    @FXML
    private TextArea usernameTextField;

    @FXML
    private TextArea passwordTextField;

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

        try{
            personQueries = new UserQueries();
            results = personQueries.getUser(user, password);
            numberOfEntries = results.size();

            if (numberOfEntries > 0){
                currentEntry = results.get(currentEntryIndex);

                if (currentEntry.getUsername().equals(user) && currentEntry.getPassword().equals(password)){
                    errorLabel.setText("Success");
                    try{
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChatScreen.fxml"));
                        Parent root = (Parent) fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setTitle(chatroomComboBox.getSelectionModel().toString());
                        stage.setScene(new Scene(root));
                        stage.show();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    errorLabel.setText("Failure - Wrong Username/Password");
                }
            }
            else{
                errorLabel.setText("Error");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void registerButtonPressed(ActionEvent event){
        int results;

        try{
            personQueries = new UserQueries();
            results = personQueries.addUser(user, password);

            if (results == 1){
                errorLabel.setText("Successful Registration");
            }
            else{
                errorLabel.setText("Error");
            }
        }
        catch(Exception e){

        }
    }

    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        chatroomComboBox.getItems().setAll("Group 9", "Software Design");
    }

}
