// Modified Fig. 27.5: Multi-threaded Chat Server.java
// Server portion of a client/server stream-socket connection. 

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

public class Server extends JFrame {
    private JTextArea displayArea; // display information to user
    private ExecutorService executor; // will run players
    private ServerSocket server; // server socket
    private ArrayList<SockServer> chatRoomA, chatRoomB, noChatRoom;
    private int counter = 1; // counter of number of connections
    private int nClientsActive = 0;

    // set up GUI
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

    // manipulates displayArea in the event-dispatch thread
    // ************* DO NOT NEED BUT WILL USE FOR ERROR CHECKING
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

    /* This new Inner Class implements Runnable and objects instantiated from this
     * class will become server threads each serving a different client
     */
    private class SockServer implements Runnable {
        private ObjectOutputStream output; // output stream to client
        private ObjectInputStream input; // input stream from client
        private Socket connection; // connection to client
        private int myConID;
        private boolean alive = false;

        public SockServer(int counterIn) {
            myConID = counterIn;
        }

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

        // wait for connection to arrive, then display connection info
        private void waitForConnection() throws IOException {

            displayMessage("Waiting for connection" + myConID + "\n");
            connection = server.accept(); // allow server to accept connection
            displayMessage("Connection " + myConID + " received from: " +
                    connection.getInetAddress().getHostName());
        }

        private void getStreams() throws IOException {
            // set up output stream for objects
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush(); // flush output buffer to send header information

            // set up input stream for objects
            input = new ObjectInputStream(connection.getInputStream());

            displayMessage("\nGot I/O streams\n");
        }

        // process connection with client
        private void processConnection() throws IOException {
            String message = "Connection " + myConID + " successful";
            //sendData(message); // send connection successful message

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