// Fig. 27.7: Client.java
// Client portion of a stream-socket connection between client and server.

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class Client extends JFrame {
    private static String user;
    private static String password;
    private User currentEntry;
    private SWDMessageQueries messageQuery;
    private int numberOfEntries = 0;
    private int currentEntryIndex = 0;
    private JTextField enterField; // enters information from user
    private JTextField chatroomField; // enters chatroom name
    private JLabel selectLabel = new JLabel("Select Chatroom");
    private String[] chatroomsArray = {"Software Design", "Group Nine"};
    private JComboBox<String> chatroomsComboBox = new JComboBox<String>(chatroomsArray);
    private JTextArea displayArea; // display information to user
    private ObjectOutputStream output; // output stream to server
    private ObjectInputStream input; // input stream from server
    private String message = ""; // message from server
    private String chatServer; // host server for this application
    private Socket client; // socket to communicate with server
    private String chatRoom;

    public String getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(String chatRoom) {
        this.chatRoom = chatRoom;
    }

    // initialize chatServer and set up GUI
    public Client(String chatRoom) {
        super("Client");
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        this.chatRoom = chatRoom;
        chatServer = "127.0.0.1"; // set server to which this client connects

        JPanel textEntryPanel = new JPanel();
        textEntryPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        textEntryPanel.add(selectLabel, constraints);

        constraints.gridx = 1;
        chatroomsComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chatroomsComboBox.getSelectedItem().toString().equals("Software Design")){
                    setChatRoom("chatRoomA");
                }
                else{
                    setChatRoom("chatRoomB");
                }

                runClient();
            }
        });
        textEntryPanel.add(chatroomsComboBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 4;


        enterField = new JTextField(); // create enterField
        enterField.setEditable(false);
        enterField.addActionListener(
                new ActionListener() {
                    // send message to server
                    public void actionPerformed(ActionEvent event) {
                        int results;

                        messageQuery = new SWDMessageQueries();
                        results = messageQuery.addMessage(user, event.getActionCommand());

                        sendData(event.getActionCommand());
                        enterField.setText("");
                    }
                }
        );

        textEntryPanel.add(enterField, constraints);

        displayArea = new JTextArea(); // create displayArea
        displayArea.setEditable(false);
        mainPanel.add(textEntryPanel,BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        add(mainPanel);
        setSize(300, 300); // set size of window
        setVisible(true); // show window
    } // end Client constructor

    // connect to server and process messages from server
    public void runClient() {
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
                displayMessage("\n" + message); // display message
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
    private void sendData(String message) {
        try // send object to server
        {
            output.writeObject("CLIENT>>> " + message);
            output.flush(); // flush data to output
            //displayMessage("\nCLIENT>>> " + message);
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
                        displayArea.append(messageToDisplay);
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