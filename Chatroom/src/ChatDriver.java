import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;

/**************************************
 * A public class to run the login
 * class functionality
 *************************************/
public class ChatDriver {
    /*********************************************
     * main function to set and run the frame
     * @param args      the supplied command-line
     *                  arguments as an array of
     *                  String objects
     ******************************************/
    public static void main(String[] args){
        Login frame = new Login();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
