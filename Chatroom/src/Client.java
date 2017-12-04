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

/**
 * Client class which extends JFrame to create the GUI interface which
 * the ChatMeUp user may use to interact with other users
 */
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

    /**
     * getter function for the chatRoom which the user desires to join/remain
     * @return String representation of ChatRoom
     */
    public String getChatRoom() {
        return chatRoom;
    }

    /**
     * setter function for the chatRoom which the user desires to join/remain
     * @param chatRoom - String representation of ChatRoom
     */
    public void setChatRoom(String chatRoom) {
        this.chatRoom = chatRoom;
    }

    // initialize chatServer and set up GUI
    /**
     * Client Constructor which sets up the GUI interface for the user to see and use
     * while creating the connection between the server and establishing which chatroom
     * to put he new Client user in
     * @param chatRoom - String representation of desired ChatRoom
     * @param user - username
     */
    public Client(String chatRoom, String user) {
        super("Client");
        setLayout(new BorderLayout());

        this.chatRoom = chatRoom;
        this.username = user;
        chatServer = "10.0.1.5"; // set server to which this client connects
        //chatServer = "127.0.0.1";

        Font font = new Font("serif", Font.PLAIN,16);

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
        enterField.setFont(font);

        enterField.addActionListener(
                new ActionListener() {
                    // send message to server
                    public void actionPerformed(ActionEvent event) {
                        int results;

                        if (chatRoom.equals("chatRoomA")){
                            messageQuery = new SWDMessageQueries();
                            results = messageQuery.addUser(user, event.getActionCommand());
                        }
                        else{
                            messageQuery = new Group9MessageQueries();
                            results = messageQuery.addUser(user, event.getActionCommand());
                        }

                        clientBackground.sendData(user + ": " + event.getActionCommand());
                        enterField.setText("");
                    }
                }
        );

        displayArea = new JTextArea(); // create displayArea
        displayArea.setEditable(false);
        displayArea.setFont(font);
        displayArea.setBackground(new Color(191,222,233, 140));
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        add(enterField,BorderLayout.NORTH);
        add(new JScrollPane(displayArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

        setSize(400, 500); // set size of window
        setVisible(true); // show window

        displayDatabaseMessages();

        clientBackground = new ClientBackground(displayArea, enterField, getChatRoom(), username);
        clientBackground.execute();

    }

    /**
     * Display the previous messages in the chat room from the database
     */
    private void displayDatabaseMessages(){
        for (int i = 0; i < numberOfEntries; i++){
            displayArea.append("\n" + messageResults.get(i).getUsername() + ": " + messageResults.get(i).getPassOrMessage());
        }

    }

}