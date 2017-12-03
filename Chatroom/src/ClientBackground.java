import javax.swing.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class ClientBackground  extends SwingWorker<String, Void>{
    private JTextField enterField; // enters information from user
    private JTextArea displayArea; // display information to user
    private ObjectOutputStream output; // output stream to server
    private ObjectInputStream input; // input stream from server
    private String message = ""; // message from server
    private String chatServer; // host server for this application
    private Socket client; // socket to communicate with server
    private String chatRoom;
    private String username;

    public ClientBackground(JTextArea displayArea, JTextField enterField, String chatRoom, String user){
        this.displayArea = displayArea;
        this.enterField = enterField;
        this.chatRoom = chatRoom;
        this.username = user;
    }

    @Override
    protected String doInBackground() throws Exception {
        try // connect to server, get streams, process connection
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

    // connect to server
    private void connectToServer() throws IOException {
        // create Socket to make connection to server
        client = new Socket(InetAddress.getByName(chatServer), 23555);

        // display connection information
        displayMessage("Connected to: " +
                client.getInetAddress().getHostName());
    }


    // get streams to send and receive data
    private void getStreams() throws IOException {
        // set up output stream for objects
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush(); // flush output buffer to send header information

        // set up input stream for objects
        input = new ObjectInputStream(client.getInputStream());
    }

    // process connection with server
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

    // close streams and socket
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

    // send message to server
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

    // manipulates displayArea in the event-dispatch thread
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

    // manipulates enterField in the event-dispatch thread
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
