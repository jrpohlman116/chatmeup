import javax.swing.*;
import java.awt.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server class which will facilitate interactions between multiple Client
 * users for the ChatMeUp application
 */
public class Server extends JFrame {
    private JTextArea displayArea; // display information to user
    private ExecutorService executor; // will run players
    private ServerSocket server; // server socket
    private ArrayList<SockServer> chatRoomA, chatRoomB, noChatRoom;
    private int counter = 1; // counter of number of connections
    private int nClientsActive = 0;

    /**
     * Server constructor which sets up the GUI interface for error checking
     * and creates multiple dynamic ArrayList to track any number of users in
     * each chat Room
     */
    public Server() {
        super("Server");

        chatRoomA = new ArrayList<SockServer>();
        chatRoomB = new ArrayList<SockServer>();
        noChatRoom = new ArrayList<SockServer>();
        executor = Executors.newFixedThreadPool(100); // create thread pool

        displayArea = new JTextArea(); // create displayArea
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        setSize(300, 150);
        setVisible(true);
    }

    // set up and run server

    /**
     * Sets up and runs the server through a ServerSocket conncection which
     * can connect to multiple SockServers
     */
    public void runServer() {
        try // set up server to receive connections; process connections
        {
            server = new ServerSocket(23555, 100); // create ServerSocket

            while (true) {
                try {
                    SockServer temp = new SockServer(counter);
                    noChatRoom.add(temp);
                    temp.waitForConnection();
                    nClientsActive++;
                    executor.execute(temp);
                }
                catch (EOFException eofException) {
                    displayMessage("\nServer terminated connection");
                } finally { ++counter; }
            }
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * manipulates the GUI to track events occurring with different clients
     * @param messageToDisplay String displayed on Server GUI
     */
    private void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() // updates displayArea
                    {
                        displayArea.append(messageToDisplay); // append message
                    }
                } // end anonymous inner class
        );
    }

    /*
     * This new Inner Class implements Runnable; the SockServer objects instantiated
     * from this class will become server threads, each serving a different client
     */
    private class SockServer implements Runnable {
        private ObjectOutputStream output; // output stream to client
        private ObjectInputStream input; // input stream from client
        private Socket connection; // connection to client
        private int myConID;
        private boolean alive = false;

        /**
         * SockServer constructor, sets counterIn to keep track of which
         * SockServer object, or thread, is being handled
         * @param counterIn number to identity object, and therefore Client thread
         */
        private SockServer(int counterIn) {
            myConID = counterIn;
        }

        /**
         * Attempts to get input and output streams, then process the connection
         * of the thread until the connection is broken and the number of Clients decreases
         */
        public void run() {
            try {
                alive = true;
                try {
                    getStreams(); // get input & output streams
                    processConnection(); // process connection
                    nClientsActive--;
                }
                catch (EOFException eofException) {
                    displayMessage("\nServer" + myConID + " terminated connection");
                } finally {
                    closeConnection();
                }
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        /**
         * waits for connection from Client threads & updates Server GUI accordingly
         * @throws IOException
         */
        private void waitForConnection() throws IOException {

            displayMessage("Waiting for connection" + myConID + "\n");
            connection = server.accept(); // allow server to accept connection
            displayMessage("Connection " + myConID + " received from: " +
                    connection.getInetAddress().getHostName());
        }

        /**
         * Retrieves output streams from the connections to the Clients
         * @throws IOException exception if input or output stream has error
         */
        private void getStreams() throws IOException {
            // set up output stream for objects
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush(); // flush output buffer to send header information

            // set up input stream for objects
            input = new ObjectInputStream(connection.getInputStream());

            displayMessage("\nGot I/O streams\n");
        }

        /**
         * process connection with client, process and properly respond to data from client
         * @throws IOException exception if input or output stream has error
         */
        private void processConnection() throws IOException {
            String message = "Connection " + myConID + " successful";

            do // process messages sent from client
            {
                try // read message and display it
                {
                    message = (String) input.readObject(); // read new message
                    message = message.replace("CLIENT>>> ","");
                    displayMessage("\nConnection" + myConID + ": "+message+"\n");

                    if (message.equals("369NewRoomchatRoomA")) {
                        for (SockServer chatter: noChatRoom ) {
                            if(chatter.connection == connection) {
                                chatRoomA.add(chatter);
                                noChatRoom.remove(chatter);
                                displayMessage("Connection " +myConID+" added to ChatRoomA\n");
                                message = ("Welcome to the Software Design Chat Room");
                                sendData(message);
                            }
                        }
                    } else if (message.equals("369NewRoomchatRoomB")) {
                        for (SockServer chatter: noChatRoom ) {
                            if(chatter.connection == connection) {
                                chatRoomB.add(chatter);
                                noChatRoom.remove(chatter);
                                displayMessage("Connection " +myConID+" added to ChatRoomB\n");
                                message = ("Welcome to the Group 9 Chat Room");
                                sendData(message);
                            }
                        }
                    } else {
                        message = checkEmoji(message);
                        if(chatRoomA.contains(this)) {
                            displayMessage("Connection " +myConID+" wrote in ChatRoomA\n");
                            for (SockServer chatter : chatRoomA) {
                                if (chatter.alive) {
                                    chatter.sendData(message);
                                }
                            }
                        } else if (chatRoomB.contains(this)) {
                            displayMessage("Connection " +myConID+" wrote in ChatRoomB\n");
                            for (SockServer chatter : chatRoomB) {
                                if (chatter.alive) {
                                    chatter.sendData(message);
                                }
                            }
                        } else {
                            displayMessage("Error trying to add or write in chatroom.\n");
                            throw new ClassNotFoundException("Chat Room not accessed or updated.");
                        }
                    }
                }
                catch (ClassNotFoundException classNotFoundException) {
                    displayMessage("Unknown object type received\n");
                }

            } while (!message.equals("CLIENT>>> TERMINATE"));
        }

        /**
         * Replaces emoticons such as ":)" with its emoji representative in unicode
         * @param message String checked for emoticons
         * @return message with emoji's
         * @throws IOException exception if input or output stream has error
         */
        private String checkEmoji(String message) throws IOException {
            message = message.replace(":)","\uD83D\uDE00");
            message = message.replace(":(","\u2639");
            message = message.replace(";)","\uD83D\uDE18");
            message = message.replace("B)","\uD83D\uDE0E");
            message = message.replace("poo","\uD83D\uDCA9");
            message = message.replace("kitty","\uD83D\uDE3A");
            message = message.replace("This is due tomorrow","\uD83D\uDE31");
            message = message.replace("finals","\uD83D\uDE2D");
            message = message.replace("<3","\uD83D\uDC94");
            return message;
        }

        // close streams and socket

        /**
         * 
         */
        private void closeConnection() {
            nClientsActive--;
            displayMessage("\nTerminating connection " + myConID + "\n");
            displayMessage("\nNumber of connections = " + nClientsActive + "\n");
            alive = false;

            try {
                output.close();
                input.close();
                connection.close();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        private void sendData(String message) {
            try
            {
                output.writeObject(message);
                output.flush(); // flush output to client
            }
            catch (IOException ioException) {
                displayArea.append("\nError writing object");
            }
        }
    }
}