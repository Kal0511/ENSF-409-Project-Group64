import java.sql.*;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.*;

/**
 * Inventory is a class that uses the SQL inventory database to process the the
 * user requested order for an item of furniture, specified by category and
 * type.
 *
 * @author Adam Abouelhassan
 * @author Khaled Amer
 * @author Kaumil Patel
 * @author Dillon Matthews
 * @version 2.2
 * @since 1.0
 */
public class Inventory {

    private Connection dbConnect;
    private ResultSet results;

    private String category;
    private String type;
    private int amount;

    private String entry;
    private Furniture item;

    // possible manufacturers for each category of furniture
    private final String[] possibleManufacturersChair = {"002", "003", "004", "005"};
    private final String[] possibleManufacturersDesk = {"001", "002", "004", "005"};
    private final String[] possibleManufacturersLamp = {"002", "004", "005"};
    private final String[] possibleManufacturersFiling = {"002", "004", "005"};

    /**
     * Constructor
     */
    public Inventory() {
        initializeConnection();
    }

    /**
     * Getter for category
     *
     * @return category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Getter for type
     *
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Getter for amount
     *
     * @return amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Getter for item
     *
     * @return item
     */
    public Furniture getItem() {
        return item;
    }

    /**
     * Getter for results
     *
     * @return results
     */
    public ResultSet getResults() {
        return results;
    }

    /**
     * This prompts the user for an input and uses a BufferedReader to to read in
     * the user's request. The user's input is then passed to the AnalyzeEntry
     * method to be parsed.
     */
    public void getUserRequest() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("User request: ");
            entry = reader.readLine(); // read input line
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.analyzeEntry(entry);
    }

    /**
     * Returns DBURL of desired MySQL database.
     *
     * @return DBURL
     */
    public String getDburl() {
        return "jdbc:mysql://localhost/INVENTORY";
    }

    /**
     * Returns the name of the MySQL user which the desired MySQL database belongs
     * to.
     *
     * @return user
     */
    public String getUsername() {
        return "adam";
    }

    /**
     * Returns the password to the respective mySQL User.
     *
     * @return password
     */
    public String getPassword() {
        return "ensf409";
    }

    /**
     * Connects to database specified by getDburl(), uses getUsername() and
     * getPassword() to access database.
     */
    public void initializeConnection() {
        try {
            dbConnect = DriverManager.getConnection(this.getDburl(), this.getUsername(), this.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database");
        }
    }

    /**
     * parses user entry into category, type, and amount.
     *
     * @param entry User input
     */
    public void analyzeEntry(String entry) {
        try {
            entry = entry.toLowerCase();
            this.type = entry.split(" ")[0];
            this.category = entry.split(" ")[1].split(",")[0];
            this.amount = Integer.parseInt(entry.split(" ")[2]);
            if (!(category.equals("chair") || category.equals("desk") || category.equals("lamp")
                    || category.equals("filing"))) {
                System.out.println("Unknown item.");
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
            System.exit(1);
        }
    }

    /**
     * Finds pieces of requested item using an adaptive query, stores results.
     */
    public void pullData() {
        String query;
        try {
            query = "SELECT * FROM $tableName WHERE Type = ?";
            query = query.replace("$tableName", category);
            PreparedStatement myStmt = dbConnect.prepareStatement(query);
            myStmt.setString(1, type);
            results = myStmt.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * findManuID takes in a String array of manufacturer ID's as an argument and
     * replaces the contents of that string with the actual names of the respective
     * manufacturers in the same order.
     *
     * @throws SQLException For handling exceptions regarding MySQL
     */
    public void findManuID(String[] x) throws SQLException {
        String query;
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
            if (resultSet != null) {
                while (resultSet.next()) {
                    x[i] = resultSet.getString("Name");
                }
            }
        }
    }

    /**
     * method iterates through the results found by pullDat() and saves all the
     * properties of each piece of furniture to their own String array which are
     * added to a more in depth list of pieces
     *
     * @throws SQLException For handling exceptions regarding MySQL
     */
    public void evaluateRequest() throws SQLException {
        switch (category) {
            case "chair": {
                ArrayList<Chair> list = new ArrayList<>();
                while (results.next()) {
                    Chair temp = new Chair(results.getString("ID"), strToInt(results.getString("Legs")),
                            strToInt(results.getString("Arms")), strToInt(results.getString("Seat")),
                            strToInt(results.getString("Cushion")), results.getInt("Price"));
                    list.add(temp);
                }
                findManuID(possibleManufacturersChair);
                item = Chair.processRequest(list, amount);
                break;
            }
            case "desk": {
                ArrayList<Desk> list = new ArrayList<>();
                while (results.next()) {
                    Desk temp = new Desk(results.getString("ID"), strToInt(results.getString("Legs")),
                            strToInt(results.getString("Top")), strToInt(results.getString("Drawer")),
                            results.getInt("Price"));
                    list.add(temp);
                }
                findManuID(possibleManufacturersDesk);
                item = Desk.processRequest(list, amount);
                break;
            }
            case "filing": {
                ArrayList<Filing> list = new ArrayList<>();
                while (results.next()) {
                    Filing temp = new Filing(results.getString("ID"), strToInt(results.getString("Rails")),
                            strToInt(results.getString("Cabinet")), strToInt(results.getString("Drawers")),
                            results.getInt("Price"));
                    list.add(temp);
                }
                findManuID(possibleManufacturersFiling);
                item = Filing.processRequest(list, amount);
                break;
            }
            case "lamp": {
                ArrayList<Lamp> list = new ArrayList<>();
                while (results.next()) {
                    Lamp temp = new Lamp(results.getString("ID"), strToInt(results.getString("Base")),
                            strToInt(results.getString("Bulb")), results.getInt("Price"));
                    list.add(temp);
                }
                findManuID(possibleManufacturersLamp);
                item = Lamp.processRequest(list, amount);
                break;
            }
        }
        results.close();
    }

    /**
     * strToInt() method simply returns an int, either 1 or 0 to indicate a 'Y' or
     * 'N' of components in the mysql database to increase readability of code
     *
     * @param check Either 'Y' or 'N'
     * @return Either 1 for 'Y' or 0 for 'N'
     */
    public int strToInt(String check) {
        if (check.equals("Y")) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * This method updates the database by deleting the items that were purchased,
     * uses an adaptive query to identify which elements from the database to
     * delete.
     */
    public void updateDatabase() {
        String query;
        PreparedStatement myStmt = null;
        try {
            query = "DELETE FROM $tableName WHERE ID = ?";
            query = query.replace("$tableName", category);
            for (String item : item.IDs) {
                myStmt = dbConnect.prepareStatement(query);
                myStmt.setString(1, item);
                myStmt.execute();
            }
            if (myStmt != null) {
                myStmt.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * checks to see if it is possible to fill the requested order, if it cannot be
     * fulfilled it outputs a message and exits.
     */
    public void checkIfOrderFilled() {
        if (item == null) {
            StringBuilder output = new StringBuilder();
            output.append("Order cannot be filled based on current inventory. Suggested manufacturers are ");
            if (category.equals("chair"))
                for (String s : possibleManufacturersChair) {
                    output.append(s);
                    output.append(", ");
                }
            if (category.equals("desk"))
                for (String s : possibleManufacturersDesk) {
                    output.append(s);
                    output.append(", ");
                }
            if (category.equals("lamp"))
                for (String s : possibleManufacturersLamp) {
                    output.append(s);
                    output.append(", ");
                }
            if (category.equals("filing"))
                for (String s : possibleManufacturersFiling) {
                    output.append(s);
                    output.append(", ");
                }
            System.out.println(output.substring(0, output.length() - 2));
            System.exit(1);
        }
    }

    /**
     * Writes the order details to a file called orderForm.txt.
     */
    public void write() {
        System.out.print("Purchase ");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < item.IDs.size(); i++) {
            System.out.print(item.IDs.get(i));
            if (i != item.IDs.size() - 1)
                System.out.print(" and ");
        }
        System.out.print(" for $" + item.getPrice() + "\n");
        try {
            FileWriter fileWriter = new FileWriter("orderForm.txt");
            PrintWriter fo = new PrintWriter(fileWriter);
            fo.write("Furniture Order Form \n \n");
            fo.write("Faculty Name:\n");
            fo.write("Contact:\n");
            fo.write("Date: " + dtf.format(now) + "\n");
            fo.write("\nOriginal Request: " + entry + "\n");
            fo.write("\nItems Ordered:\n");
            for (String item : item.IDs)
                fo.write("ID: " + item + "\n");
            fo.write("\nTotal Price: $" + item.getPrice());
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addChair(String id, String type, String legs, String arms, String seat, String cushion, int price,
                         String manuID) {
        String query;
        PreparedStatement myStmt;
        try {
            query = "INSERT INTO CHAIR (ID, Type, Legs, Arms, Seat, Cushion, Price, ManuID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            myStmt = dbConnect.prepareStatement(query);
            myStmt.setString(1, id);
            myStmt.setString(2, type);
            myStmt.setString(3, legs);
            myStmt.setString(4, arms);
            myStmt.setString(5, seat);
            myStmt.setString(6, cushion);
            myStmt.setInt(7, price);
            myStmt.setString(8, manuID);
            myStmt.executeUpdate();
            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void removeChair(String id) {
        String query;
        PreparedStatement myStmt;
        try {
            query = "DELETE FROM Chair WHERE ID = ?";
            myStmt = dbConnect.prepareStatement(query);
            myStmt.setString(1, id);
            myStmt.executeUpdate();
            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean itemExists(String id, String category) {
        String query;
        PreparedStatement myStmt;
        ResultSet resultSet;
        try {
            query = "SELECT * FROM $tableName WHERE ID = ?";
            query = query.replace("$tableName", category);
            myStmt = dbConnect.prepareStatement(query);
            myStmt.setString(1, id);
            resultSet = myStmt.executeQuery();
            if (resultSet.next())
                return true;
            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
