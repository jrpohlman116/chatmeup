// Fig. 28.31: PersonQueries.java
// PreparedStatements used by the Address Book application

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserQueries {
    private static final String URL = "jdbc:mysql://s-l112.engr.uiowa.edu:3306/engr_class009";
    private static final String USERNAME = "engr_class009";
    private static final String PASSWORD = "engr_class009-xyz";

    private Connection connection; // manages connection
    private PreparedStatement selectUser;
    private PreparedStatement insertNewUser;

    // constructor
    public UserQueries() {
        try {
            connection =
                    DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // create query that selects all entries in the AddressBook
            selectUser =
                    connection.prepareStatement("SELECT * FROM login WHERE username = ? and password = ?");

            // create insert that adds a new entry into the database
            insertNewUser = connection.prepareStatement(
                    "INSERT INTO login " +
                            "(username,password) " +
                            "VALUES (?, ?)");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.exit(1);
        }
    } // end PersonQueries constructor

    // select all of the addresses in the database
    public List<User> getUser(String user, String password) {
        ArrayList<User> results = new ArrayList<User>();
        ResultSet resultSet = null;

        try {
            selectUser.setString(1, user);
            selectUser.setString(2, password);
            // executeQuery returns ResultSet containing matching entries
            resultSet = selectUser.executeQuery();

            while (resultSet.next()) {
                results.add(new User(
                        resultSet.getString("username"),
                        resultSet.getString("password")));
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
    public int addUser(
            String user, String pass) {
        int result = 0;

        // set parameters, then execute insertNewPerson
        try {
            insertNewUser.setString(1, user);
            insertNewUser.setString(2, pass);

            // insert the new entry; returns # of rows updated
            result = insertNewUser.executeUpdate();
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
} // end class PersonQueries


/**************************************************************************
 * (C) Copyright 1992-2014 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/

 