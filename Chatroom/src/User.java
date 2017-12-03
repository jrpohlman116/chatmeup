/**************************
 * public class User for the
 * users, the passwords,
 * and their messages on the
 * database
 **************************/
public class User {
    private String username;
    private String passOrMessage;

    // constructor

    /**************************
     * User default constructor
     **************************/
    public User() {
    }

    // constructor

    /**************************
     * User constructor given
     * a username and a password
     * or message
     * @param username
     * @param passOrMessage
     **************************/
    public User(String username, String passOrMessage) {
        setUsername(username);
        setPassOrMessage(passOrMessage);
    }

    /***************************
     * get the username from the
     * User class
     * @return  String username
     **************************/
    public String getUsername() {
        return username;
    }

    /**************************
     * sets the username
     * @param username
     **************************/
    public void setUsername(String username) {
        this.username = username;
    }

    // returns the last name

    /**************************
     * get the password or message
     * @return String password or message
     **************************/
    public String getPassOrMessage() {
        return passOrMessage;
    }

    // sets the password

    /***************************
     * Set the user's password
     * or messagge
     * @param passOrMessage
     ***************************/
    public void setPassOrMessage(String passOrMessage) {
        this.passOrMessage = passOrMessage;
    }
} // end class Person


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

 