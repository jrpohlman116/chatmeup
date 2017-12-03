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
import java.util.List;

public class Client extends JFrame {
    private static String user;
    private static String password;
    private User currentEntry;
    private MessageQueries messageQuery;
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
    private String username;
    private ClientBackground clientBackground;
    private List<User> messageResults;

    public String getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(String chatRoom) {
        this.chatRoom = chatRoom;
    }

    // initialize chatServer and set up GUI
    public Client(String chatRoom, String user) {
        super("Client");
        setLayout(new BorderLayout());

        this.chatRoom = chatRoom;
        this.username = user;
        chatServer = "127.0.0.1"; // set server to which this client connects




        if (chatRoom.equals("chatRoomA")){
            messageQuery = new SWDMessageQueries();
            messageResults = messageQuery.getMessage();
            numberOfEntries = messageResults.size();
        }
        else{
            messageQuery = new Group9MessageQueries();
            messageResults = messageQuery.getMessage();
            numberOfEntries = messageResults.size();
        }

        enterField = new JTextField(); // create enterField
        enterField.setEditable(false);
        enterField.addActionListener(
                new ActionListener() {
                    // send message to server
                    public void actionPerformed(ActionEvent event) {
                        int results;

                        if (chatRoom.equals("chatRoomA")){
                            messageQuery = new SWDMessageQueries();
                            results = messageQuery.addMessage(user, event.getActionCommand());
                        }
                        else{
                            messageQuery = new Group9MessageQueries();
                            results = messageQuery.addMessage(user, event.getActionCommand());
                        }

                        clientBackground.sendData(user + ": " + event.getActionCommand());
                        enterField.setText("");
                    }
                }
        );

        displayArea = new JTextArea(); // create displayArea
        displayArea.setEditable(false);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        add(enterField,BorderLayout.NORTH);
        add(new JScrollPane(displayArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

        setSize(300, 300); // set size of window
        setVisible(true); // show window

        displayDatabaseMessages();

        clientBackground = new ClientBackground(displayArea, enterField, getChatRoom(), username);
        clientBackground.execute();

    } // end Client constructor

    public void displayDatabaseMessages(){
        for (int i = 0; i < numberOfEntries; i++){
            displayArea.append("\n" + messageResults.get(i).getUsername() + ": " + messageResults.get(i).getPassOrMessage());
        }

    }

}