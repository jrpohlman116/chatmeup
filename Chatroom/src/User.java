// Fig. 28.30: Person.java
// Person class that represents an entry in an address book.
public class User {
    private String username;
    private String passOrMessage;

    // constructor
    public User() {
    }

    // constructor
    public User(String username, String passOrMessage) {
        setUsername(username);
        setPassOrMessage(passOrMessage);
    }

    // returns the first name
    public String getUsername() {
        return username;
    }

    // sets the username
    public void setUsername(String username) {
        this.username = username;
    }

    // returns the last name
    public String getPassOrMessage() {
        return passOrMessage;
    }

    // sets the password
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

 