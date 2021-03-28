import java.sql.*;
import java.util.LinkedList;
import java.io.*;
import java.util.*;

public class Inventory {
    public final String DBURL = "jdbc:mysql://localhost/inventory";       // store the database url information
    public final String USERNAME = "adam";         // store the user's account username
    public final String PASSWORD = "ensf409";       // store the user's account password

    private Connection dbConnect;
    private ResultSet results;

    public String category;
    public String type;
    public int amount;

    String entry = null;
    String items = null;
    int itemPrice = 0;

    //    Set<String[]> resultList = new HashSet<String[]>();
    LinkedList<String[]> resultList = new LinkedList<String[]>();


    public void getUserRequest() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));       // for reading input
        try {
            System.out.print("User request: ");
            entry = reader.readLine();                     // read input line
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.AnalyzeEntry(entry);
        //System.out.println("Category: " + this.category);
        //System.out.println("Type: " + this.type);
        //System.out.println("Amount: " + this.amount);
    }

    public Inventory() {
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

    // reads from from required table in mysql database INVENTORY
    public void pullData() {
        String query = null;
        try {
            if (category.equals("Chair"))
                query = "SELECT * FROM Chair WHERE Type = ?";
            else if (category.equals("Desk"))
                query = "SELECT * FROM Desk WHERE Type = ?";
            else if (category.equals("Lamp"))
                query = "SELECT * FROM Lamp WHERE Type = ?";
            else if (category.equals("Filing"))
                query = "SELECT * FROM Filing WHERE Type = ?";
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

    // retrives all items matching user request, calls other methods to calculate cheapest option
    public void evaluvateRequest() throws SQLException {
        int i = 0;
        if (category.equals("Chair")) {

            while (results.next()) {
                String[] arr = {results.getString("ID"), results.getString("Type"),
                        results.getString("Legs"), results.getString("Arms"), results.getString("Seat"),
                        results.getString("Cushion"), results.getString("Price"), results.getString("ManuID")};
                resultList.add(arr);
                i++;
            }
            Chair chair = new Chair();
            chair.checkRequest(resultList);
            chair.checkPrices();
            //System.out.println("Minimum price per Chair: " + chair.minPrice);
            this.itemPrice = chair.minPrice;
            //System.out.println("Result: " + chair.result);
            this.items = chair.result;

        } else if (category.equals("Desk")) {

            while (results.next()) {
                String[] arr = {results.getString("ID"), results.getString("Type"),
                        results.getString("Legs"), results.getString("Top"), results.getString("Drawer"),
                        results.getString("Price"), results.getString("ManuID")};
                resultList.add(arr);
                i++;
            }
            Desk desk = new Desk();
            desk.checkRequest(resultList);
            desk.checkPrices();
            //System.out.println("Minimum price per Desk: " + desk.minPrice);
            this.itemPrice = desk.minPrice;
            //System.out.println("Result: " + desk.result);
            this.items = desk.result;

        } else if (category.equals("Filing")) {

            while (results.next()) {
                String[] arr = {results.getString("ID"), results.getString("Type"),
                        results.getString("Rails"), results.getString("Drawers"), results.getString("Cabinet"),
                        results.getString("Price"), results.getString("ManuID")};
                resultList.add(arr);
                i++;
            }
            Filing filing = new Filing();
            filing.checkRequest(resultList);
            filing.checkPrices();
            //System.out.println("Minimum price per Filing: " + filing.minPrice);
            this.itemPrice = filing.minPrice;
            //System.out.println("Result: " + filing.result);
            this.items = filing.result;

        } else if (category.equals("Lamp")) {

            while (results.next()) {
                String[] arr = {results.getString("ID"), results.getString("Type"),
                        results.getString("Base"), results.getString("Bulb"),
                        results.getString("Price"), results.getString("ManuID")};
                resultList.add(arr);
                i++;
            }
            Lamp lamp = new Lamp();
            lamp.checkRequest(resultList);
            lamp.checkPrices();
            //System.out.println("Minimum price per lamp: " + lamp.minPrice);
            this.itemPrice = lamp.minPrice;
            //System.out.println("Result: " + lamp.result);
            this.items = lamp.result;
        }
        results.close();
    }

    // updates database by deleting items being bought
    public void updateDatabase() {
        String query = null;
        PreparedStatement myStmt = null;
        try {
//            System.out.println(items.charAt(0));
            if (items.charAt(0) == 'C')
                query = "DELETE FROM chair WHERE ID = ?";
            else if (items.charAt(0) == 'D')
                query = "DELETE FROM desk WHERE ID = ?";
            else if (items.charAt(0) == 'L')
                query = "DELETE FROM lamp WHERE ID = ?";
            else if (items.charAt(0) == 'F')
                query = "DELETE FROM filing WHERE ID = ?";

            for (int i = 0; i < items.split(" ").length; i++) {
                myStmt = dbConnect.prepareStatement(query);
//                System.out.println(items.split(" ")[i]);
                myStmt.setString(1, items.split(" ")[i]);
            }
            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    // write to file called orderForm.txt
    public void write() {
        System.out.print("Output: Purchase ");
        for (int i = 0; i < items.split(" ").length; i++) {
            System.out.print(items.split(" ")[i]);
            if (i != items.split(" ").length - 1)
                System.out.print(" and ");
        }
        System.out.print(" for $" + itemPrice + "\n");
        try {
            FileWriter fileWriter = new FileWriter("orderForm.txt");
            PrintWriter fo = new PrintWriter(fileWriter);
            fo.write("Furniture Order Form \n \n");
            fo.write("Faculty Name:\n");
            fo.write("Contact:\n");
            fo.write("Date:\n");
            fo.write("\nOriginal Request: " + entry + "\n");
            fo.write("\nItmes Ordered:\n");
            for (int i = 0; i < items.split(" ").length; i++)
                fo.write("ID: " + items.split(" ")[i] + "\n");
            fo.write("\nTotal Price: $" + amount * itemPrice);
            fo.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
