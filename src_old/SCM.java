/*
 * SCM is a the main class where the supply chain management
 * system is run from.
 */
import java.io.*;
import java.sql.SQLException;

public class SCM {
 
    public static void main(String[] args) throws SQLException {
        Inventory test = new Inventory();
        test.initializeConnection();
        test.getUserRequest();
        test.pullData();
        test.evaluvateRequest();
        test.checkIfOrderFilled();
        test.updateDatabase();
        test.write();
    }
}
