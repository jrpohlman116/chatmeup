import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*************************************
 * Abstract public class handling the
 * database queries and constants
 *************************************/
public abstract class MessageQueries {
    private static final String URL = "jdbc:mysql://s-l112.engr.uiowa.edu:3306/engr_class009"; //jdbc connection string
    private static final String USERNAME = "engr_class009"; //group username
    private static final String PASSWORD = "engr_class009-xyz"; //group password

    private Connection connection; // manages connection
    private PreparedStatement selectMessage;
    private PreparedStatement insertNewMessage;

    /**********************************
     * gets the JDBC connection string from
     * the MessageQueries instance
     * @return      String url
     **********************************/
    public static String getURL() {
        return URL;
    }

    /**********************************
     * gets the JDBC username string from
     * the MessageQueries instance
     * @return      String username
     **********************************/
    public static String getUSERNAME() {
        return USERNAME;
    }

    /**********************************
     * gets the JDBC password string from
     * the MessageQueries instance
     * @return      String password
     **********************************/
    public static String getPASSWORD() {
        return PASSWORD;
    }

    /**********************************
     * gets the JDBC connection from
     * the MessageQueries instance
     * @return      Connection
     **********************************/
    public Connection getConnection() {
        return connection;
    }

    /*********************************
     * Set the connection instance variable
     * @param connection
     ********************************/
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /*******************************
     * get the PreparedStatement from
     * the select database query
     * @return PreparedStatement
     ******************************/
    public PreparedStatement getSelectMessage() {
        return selectMessage;
    }

    /******************************
     * Set the select message
     * instance variable
     * @param selectMessage
     *****************************/
    public void setSelectMessage(PreparedStatement selectMessage) {
        this.selectMessage = selectMessage;
    }

    /*****************************
     * get the PreparedStatement from
     * the insert database query
     * @return PreparedStatement
     *****************************/
    public PreparedStatement getInsertNewMessage() {
        return insertNewMessage;
    }

    /*****************************
     * Set the insertNewMessage
     * instance variable
     * @param insertNewMessage
     *****************************/
    public void setInsertNewMessage(PreparedStatement insertNewMessage) {
        this.insertNewMessage = insertNewMessage;
    }

    // select all of the addresses in the database

    /*****************************
     * Select all of the messages
     * in the database
     * @return a list of all the users
     *****************************/
    public List<User> getMessage() {
        ArrayList<User> results = new ArrayList<User>();
        ResultSet resultSet = null;

        try {

            // executeQuery returns ResultSet containing matching entries
            resultSet = selectMessage.executeQuery();

            while (resultSet.next()) {
                results.add(new User(
                        resultSet.getString("username"),
                        resultSet.getString("message")));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                close();
            }
        }

        return results;
    }

    /*****************************
     * Select the specific user
     * in the database
     * @return a list of all the users
     *****************************/
    public List<User> getUser(String username, String password) {
        ArrayList<User> results = new ArrayList<User>();
        ResultSet resultSet = null;

        try {
            selectMessage.setString(1, username);
            selectMessage.setString(2, password);

            // executeQuery returns ResultSet containing matching entries
            resultSet = selectMessage.executeQuery();

            while (resultSet.next()) {
                results.add(new User(
                        resultSet.getString("username"),
                        resultSet.getString("message")));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                close();
            }
        }

        return results;
    }


    /*************************
     * Add an entry
     * @param user
     * @param message
     * @return  an integer of
     * the number of entries added
     *************************/
    public int addUser(
            String user, String message) {
        int result = 0;

        // set parameters, then execute insertNewMessage
        try {
            insertNewMessage.setString(1, user);
            insertNewMessage.setString(2, message);

            // insert the new entry; returns # of rows updated
            result = insertNewMessage.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            close();
        }

        return result;
    }

    // close the database connection
    public void close() {
        try {
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
