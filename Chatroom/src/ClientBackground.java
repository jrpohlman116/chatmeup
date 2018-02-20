import javax.swing.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

/**
 * ClientBackground class which handles all alterations for the Client
 * in regards to connection to the host Server
 * @see Client
 * @see Server
 */
public class ClientBackground  extends SwingWorker<Void, Void>{
    private JTextField enterField; // enters information from user
    private JTextArea displayArea; // display information to user
    private ObjectOutputStream output; // output stream to server
    private ObjectInputStream input; // input stream from server
    private String message = ""; // message from server
    private String chatServer; // host server for this application
    private Socket client; // socket to communicate with server
    private String chatRoom;
    private String username;

    /**
     * ClientBackground constructor
     * @param displayArea - JTextArea where texts will be displayed on GUI
     * @param enterField - JTextField where the user can type and send text
     * @param chatRoom - String that represents the chatRoom which Client belongs to
     * @param user - username
     */
    public ClientBackground(JTextArea displayArea, JTextField enterField, String chatRoom, String user){
        this.displayArea = displayArea;
        this.enterField = enterField;
        this.chatRoom = chatRoom;
        this.username = user;
    }

    /**
     * Allows server to attempt to connect to server, get streams, and
     * process the connection to the server until the connection is ended
     * @return null data
     * @throws Exception exception if input or output stream has error or connection is ended
     */
    @Override
    public Void doInBackground() throws Exception {
        try
        {
            connectToServer(); // create a Socket to make connection
            getStreams(); // get the input and output streams
            sendData("369NewRoom"+chatRoom);
            processConnection(); // process connection

        }
        catch (EOFException eofException) {
            displayMessage("\nClient terminated connection");
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
        finally {
            closeConnection();
        }
        return null;
    }

    /**
     * connect to server
     * @throws IOException exception if input or output stream has error
     */
    private void connectToServer() throws IOException {
        // create Socket to make connection to server
        client = new Socket(InetAddress.getByName(chatServer), 23555);

        // display connection information
        displayMessage("Connected to: " +
                client.getInetAddress().getHostName());
    }


    /**
     * Retrieves output streams from the connection to the Server
     * @throws IOException exception if input or output stream has error
     */
    private void getStreams() throws IOException {
        // set up output stream for objects
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush(); // flush output buffer to send header information

        // set up input stream for objects
        input = new ObjectInputStream(client.getInputStream());
    }

    /**
     * process connection with Server: process and properly respond to data from Server
     * @throws IOException exception if input or output stream has error
     */
    private void processConnection() throws IOException {
        // enable enterField so client user can send messages
        setTextFieldEditable(true);

        do // process messages sent from server
        {
            try // read message and display it
            {
                message = (String) input.readObject(); // read new message
                displayMessage(message); // display message
            }
            catch (ClassNotFoundException classNotFoundException) {
                displayMessage("\nUnknown object type received");
            }

        } while (!message.equals("SERVER>>> TERMINATE"));
    }

    /**
     * Closes streams and socket if connection is meant to be broken
     */
    private void closeConnection() {
        displayMessage("\nClosing connection");
        setTextFieldEditable(false);

        try {
            output.close();
            input.close();
            client.close();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Sends messages to Server connected
     * @param message String message sent to Server
     */
    public void sendData(String message) {
        try // send object to server
        {
            output.writeObject("CLIENT>>> " + message);
            output.flush(); // flush data to output
        }
        catch (IOException ioException) {
            displayArea.append("\nError writing object");
        }
    }

    /**
     * Add message to GUI representing message from another user or the server
     * @param messageToDisplay String message to be added to GUI
     */
    private void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() // updates displayArea
                    {
                        displayArea.append("\n" + messageToDisplay);
                    }
                }
        );
    }

    /**
     * Controls if enterField is editable for the Client (user)
     * @param editable boolean yes/no if text field is editable
     */
    private void setTextFieldEditable(final boolean editable) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() // sets enterField's editability
                    {
                        enterField.setEditable(editable);
                    }
                }
        );
    }
}
