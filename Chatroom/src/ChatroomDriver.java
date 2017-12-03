import java.util.List;

public class ChatroomDriver {

    public static void main(String[] args) {
        String user = "jpohlman";
        String password = "pass";
        User currentEntry;
        UserQueries personQueries;
        List<User> results;
        int numberOfEntries = 0;
        int currentEntryIndex;

        personQueries = new UserQueries();
        results = personQueries.getUser(user, password);
        numberOfEntries = results.size();

        if (numberOfEntries > 0){
            currentEntry = results.get(0);

            if (currentEntry.getUsername().equals(user) && currentEntry.getPassword().equals(password)){
                System.out.println("Success");
            }
            else{
                System.out.println("Failure");
            }
        }
        else{
            System.out.println("Error");
        }


    }
}
