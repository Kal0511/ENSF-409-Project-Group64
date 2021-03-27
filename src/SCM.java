import java.sql.*;
import java.io.*;

public class SCM {
    public static void main(String[] args) {
        Inventory test = new Inventory();
        test.initializeConnection();
        test.getUserRequest();
    }
}
