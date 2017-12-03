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

}
