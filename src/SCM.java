import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SCM {
    public static void main(String[] args) {
        Inventory test = new Inventory();
        test.initializeConnection();

        BufferedReader reader = new BufferedReader(         // for reading input
                new InputStreamReader(System.in));
        String entry = null;
        try {
            System.out.print("User request:");
            entry = reader.readLine();                     // read input line
        } catch (IOException e) {
            e.printStackTrace();
        }
        test.AnalyzeEntry(entry);
//        System.out.println("Category: " + test.category);
//        System.out.println("Type: " + test.type);
//        System.out.println("Amount: " + test.amount);
    }
}
