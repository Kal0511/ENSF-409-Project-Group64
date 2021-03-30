import java.sql.*;
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
    
    // possible manufacturers for each category of furniture
    public String[] possibleManufacturersChair = {"002", "003", "004", "005"};
    public String[] possibleManufacturersDesk = {"001", "002", "004", "005"};
    public String[] possibleManufacturersLamp = {"002", "004", "005"};
    public String[] possibleManufacturersFiling = {"002", "004", "005"};

    LinkedList<String[]> resultList = new LinkedList<String[]>();

    public int getAmount() {
        return this.amount;
    }
    
    // reads input from user
    public void getUserRequest() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
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
            } else {
                System.out.println("Unknown item.");
                System.exit(1);
            }
            tmp = entry.split(", ")[1];
            this.amount = Integer.parseInt(tmp);
        } catch (Exception e) {
            System.out.println("Invalid entry.");
            System.exit(1);
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

    // find manufacturer names from database, replace possibleManufacturersID with actual names
    public void findManuID(String[] x) throws SQLException {
        String query = null;
        ResultSet resultSet = null;
        for (int i = 0; i < x.length; i++) {
            try {
                query = "SELECT Name FROM Manufacturer WHERE ManuID = ?";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);
                myStmt.setString(1, x[i]);
                resultSet = myStmt.executeQuery();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            while (resultSet.next()) {
                x[i] = resultSet.getString("Name");
            }
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
            findManuID(possibleManufacturersChair);
            Chair chair = new Chair(this.amount);
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
            findManuID(possibleManufacturersDesk);
            Desk desk = new Desk(amount);
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
            findManuID(possibleManufacturersFiling);
            Filing filing = new Filing(amount);
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
            findManuID(possibleManufacturersLamp);
            Lamp lamp = new Lamp(amount);
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
                myStmt.execute();
            }
            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // makes sure an order can be filled, outputs message if not
    public void checkIfOrderFilled() {
        if (items.equals("")) {
            String output = "";
            output += "Order cannot be filled based on current inventory. Suggested manufacturers are ";
            if (category.equals("Chair"))
                for (int i = 0; i < possibleManufacturersChair.length; i++) {
                    output += possibleManufacturersChair[i];
                    output += ", ";
                }
            if (category.equals("Desk"))
                for (int i = 0; i < possibleManufacturersDesk.length; i++) {
                    output += possibleManufacturersDesk[i];
                    output += ", ";
                }
            if (category.equals("Lamp"))
                for (int i = 0; i < possibleManufacturersLamp.length; i++) {
                    output += possibleManufacturersLamp[i];
                    output += ", ";
                }
            if (category.equals("Filing"))
                for (int i = 0; i < possibleManufacturersFiling.length; i++) {
                    output += possibleManufacturersFiling[i];
                    output += ", ";
                }
            System.out.println(output.substring(0, output.length() - 2));
            System.exit(1);
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
            fo.write("\nTotal Price: $" + itemPrice);
            fo.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
