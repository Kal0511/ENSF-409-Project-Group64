import java.io.*;
import java.sql.SQLException;

public class SCM {
    public static void main(String[] args) throws SQLException {
        Inventory test = new Inventory();
        test.initializeConnection();
        test.getUserRequest();
        test.pullData();
        test.evaluvateRequest();
        if (test.items.equals("")) {
            String output = "";
            output += "Order cannot be filled based on current inventory. Suggested manufacturers are ";
            for (int i = 0; i < test.possibleManufacturersID.length; i++) {
                if (test.possibleManufacturersID[i] != null) {
                    output += test.possibleManufacturersID[i];
                    output += ", ";
                }
            }
            System.out.println(output.substring(0, output.length() - 2));
            System.exit(1);
        } else {
            test.updateDatabase();
            test.write();
        }
    }
}
