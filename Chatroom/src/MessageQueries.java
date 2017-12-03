import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpohlman on 12/3/17.
 */
public abstract class MessageQueries {
    private static final String URL = "jdbc:mysql://s-l112.engr.uiowa.edu:3306/engr_class009";
    private static final String USERNAME = "engr_class009";
    private static final String PASSWORD = "engr_class009-xyz";

    private Connection connection; // manages connection
    private PreparedStatement selectMessage;
    private PreparedStatement insertNewMessage;

    public static String getURL() {
        return URL;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public PreparedStatement getSelectMessage() {
        return selectMessage;
    }

    public void setSelectMessage(PreparedStatement selectMessage) {
        this.selectMessage = selectMessage;
    }

    public PreparedStatement getInsertNewMessage() {
        return insertNewMessage;
    }

    public void setInsertNewMessage(PreparedStatement insertNewMessage) {
        this.insertNewMessage = insertNewMessage;
    }

    // select all of the addresses in the database
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


    // add an entry
    public int addMessage(
            String user, String message) {
        int result = 0;

        // set parameters, then execute insertNewPerson
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
