import java.sql.*;
import java.util.LinkedList;
import java.io.*;
import java.util.*;

public class Inventory {
    public final String DBURL = "jdbc:mysql://localhost/inventory";       // store the database url information
    public final String USERNAME = "khaled";         // store the user's account username
    public final String PASSWORD = "5446223";       // store the user's account password

    private Connection dbConnect;
    private ResultSet results;

    public String category;
    public String type;
    public int amount;
    
//    Set<String[]> resultList = new HashSet<String[]>();
    LinkedList<String[]> resultList = new LinkedList<String[]>();


    public void getUserRequest() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));       // for reading input
        String entry = null;
        try {
            System.out.print("User request:");
            entry = reader.readLine();                     // read input line
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.AnalyzeEntry(entry);
        System.out.println("Category: " + this.category);
        System.out.println("Type: " + this.type);
        System.out.println("Amount: " + this.amount);
    }

    public Inventory() {
        String DBURL = "jdbc:mysql://localhost/inventory";
        String USERNAME = "adam";
        String PASSWORD = "ensf409";
    }

    // getter
    public String getDBURL() {
        return DBURL;
    }

    // getter
    public String getUSERNAME() {
        return USERNAME;
    }

    // getter
    public String getPASSWORD() {
        return PASSWORD;
    }

    // Initialize connection to mysql
    public void initializeConnection() {
        try {
            dbConnect = DriverManager.getConnection(this.DBURL, this.USERNAME, this.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Dissects input from user into required variables
    public void AnalyzeEntry(String entry) {
        try {
            String tmp = null;
            if (entry.toLowerCase().contains("chair")) {
                this.category = "Chair";
                tmp = entry.toLowerCase().split(" chair")[0];
                this.type = toTitleCase(tmp);
            } else if (entry.toLowerCase().contains("desk")) {
                this.category = "Desk";
                tmp = entry.toLowerCase().split(" desk")[0];
                this.type = toTitleCase(tmp);
            } else if (entry.toLowerCase().contains("lamp")) {
                this.category = "Lamp";
                tmp = entry.toLowerCase().split(" lamp")[0];
                this.type = toTitleCase(tmp);
            } else if (entry.toLowerCase().contains("filing")) {
                this.category = "Filing";
                tmp = entry.toLowerCase().split(" filing")[0];
                this.type = toTitleCase(tmp);
            }
            tmp = entry.split(", ")[1];
            this.amount = Integer.parseInt(tmp);
        } catch (Exception e) {
            System.out.println("Invalid entry.");
        }
    }

    // convert to title case
    public String toTitleCase(String x) {
        StringBuilder titleCase = new StringBuilder(x.length());
        boolean nextTitleCase = true;

        for (char c : x.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }

    public void pullData() {
        try {
            String query = "SELECT * FROM Chair WHERE Type = ?";
            PreparedStatement myStmt = dbConnect.prepareStatement(query);
            myStmt.setString(1, type);
            results = myStmt.executeQuery();
//            while (results.next()){
//                System.out.println("Print results: " + results.getString("id"));
//            }
//            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
      public Set<String[]> evaluvateRequest() throws SQLException {
        int i = 0;
        if (category.equals("Chair")) {
            
            while (results.next()) {
                String[] arr = {results.getString("ID"), results.getString("Type"),
                        results.getString("Legs"), results.getString("Arms"), results.getString("Seat"),
                        results.getString("Cushion"), results.getString("Price"), results.getString("ManuID")};
                    resultList.add(arr);
                i++;
            }
            Chair chair = new Chair(resultList);
            chair.checkRequest(resultList);
            chair.checkPrices();
            return null;
        } //else if (category.equals("Desk")) {

//        } else if (category.equals("Filing")) {

//        } else if (category.equals("Lamp")) {

//        } 
        return null;
    }
}
