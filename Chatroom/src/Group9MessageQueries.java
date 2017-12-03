// Fig. 28.31: PersonQueries.java
// PreparedStatements used by the Address Book application

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Group9MessageQueries extends MessageQueries{
    private static final String URL = "jdbc:mysql://s-l112.engr.uiowa.edu:3306/engr_class009";
    private static final String USERNAME = "engr_class009";
    private static final String PASSWORD = "engr_class009-xyz";

    private Connection connection; // manages connection
    private PreparedStatement selectMessage;
    private PreparedStatement insertNewMessage;

    // constructor
    public Group9MessageQueries() {
        try {
            setConnection(DriverManager.getConnection(getURL(), getUSERNAME(), getPASSWORD()));

            // create query that selects all entries in the AddressBook
            setSelectMessage(getConnection().prepareStatement("SELECT * FROM group9"));

            // create insert that adds a new entry into the database
            setInsertNewMessage(getConnection().prepareStatement(
                    "INSERT INTO group9 " +
                            "(username,message) " +
                            "VALUES (?, ?)"));

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.exit(1);
        }
    } // end PersonQueries constructor

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

 