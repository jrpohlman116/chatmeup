import javax.swing.*;

/**
 * Created by jpohlman on 12/3/17.
 */
public class ClientTest {
    public static void main(String[] args) {
        Client application;
        if (args.length == 0){
            application = new Client("chatRoomB"); // create server
        }
        else{
            application = new Client(args[0]); // create server
        }

        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //application.runClient();
    } // end main
}
