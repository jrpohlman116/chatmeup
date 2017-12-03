import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController {
    private static String user;
    private static String password;
    private User currentEntry;
    private UserQueries personQueries;
    private int numberOfEntries = 0;
    private int currentEntryIndex = 0;
    private Parent root;
    private Scene scene;
    private Stage stage;

    @FXML
    private Label userLabel;

    @FXML
    private TextArea enterTextArea;

    @FXML
    private Button sendButton;

    @FXML
    private ListView messageListView;

    @FXML
    private void sendButtonPressed(ActionEvent event){

    }

    public ChatController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChatScreen.fxml"));
        fxmlLoader.setController(this);
        try{
            root = (Parent) fxmlLoader.load();
            scene = new Scene(root);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void openChat(Stage stage, String username, String chatroom){
        stage = new Stage();
        stage.setTitle(chatroom);
        stage.setScene(new Scene(root));
        userLabel.setText(username);
        stage.show();
    }

}
