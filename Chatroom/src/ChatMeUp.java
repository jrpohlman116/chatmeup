import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by jpohlman on 12/2/17.
 */
public class ChatMeUp extends Application {
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));

        final ComboBox<String> chatroomComboBox = new ComboBox<String>();
        chatroomComboBox.getItems().addAll("Group 9", "Software Design");
        chatroomComboBox.setValue("Group 9");

        Scene scene = new Scene(root);

        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
