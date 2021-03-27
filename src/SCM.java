import java.io.*;
import java.sql.SQLException;

public class SCM {
    public static void main(String[] args) throws SQLException {
        Inventory test = new Inventory();
        test.initializeConnection();
        test.getUserRequest();
        test.pullData();
        test.evaluvateRequest();
    }
}
