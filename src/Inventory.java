import java.sql.*;
import java.io.*;
import java.util.*;

public class Inventory {
	private final String DBURL = "jdbc:mysql://localhost/inventory"; // store the database url information
	private final String USERNAME = "adam"; // store the user's account username
	private final String PASSWORD = "ensf409"; // store the user's account password

	private Connection dbConnect;
	private ResultSet results;

	private String category;
	private String type;
	private int amount;

	private String entry;
	private ArrayList<String> items;
	private int itemPrice;

	// possible manufacturers for each category of furniture
	private String[] possibleManufacturersChair = { "002", "003", "004", "005" };
	private String[] possibleManufacturersDesk = { "001", "002", "004", "005" };
	private String[] possibleManufacturersLamp = { "002", "004", "005" };
	private String[] possibleManufacturersFiling = { "002", "004", "005" };

	public Inventory() {
	}

	// reads input from user
	public void getUserRequest() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.print("User request: ");
			entry = reader.readLine(); // read input line
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.AnalyzeEntry(entry);
		// System.out.println("Category: " + this.category);
		// System.out.println("Type: " + this.type);
		// System.out.println("Amount: " + this.amount);
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
			entry = entry.toLowerCase();
			this.type = entry.split(" ")[0];
			this.category = entry.split(" ")[1].split(",")[0];
			this.amount = Integer.parseInt(entry.split(" ")[2]);
			if (!category.equals("chair") && !category.equals("desk") && !category.equals("lamp")
					&& !category.equals("filing")) {
				System.out.println("Unknown item.");
				System.exit(1);
			}
		} catch (Exception e) {
			System.out.println("Invalid item.");
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
			if (category.equals("chair"))
				query = "SELECT * FROM Chair WHERE Type = ?";
			else if (category.equals("desk"))
				query = "SELECT * FROM Desk WHERE Type = ?";
			else if (category.equals("lamp"))
				query = "SELECT * FROM Lamp WHERE Type = ?";
			else if (category.equals("filing"))
				query = "SELECT * FROM Filing WHERE Type = ?";
			PreparedStatement myStmt = dbConnect.prepareStatement(query);
			// myStmt.setString(1, category);
			myStmt.setString(1, type);
			results = myStmt.executeQuery();
			// while (results.next()) {
			// System.out.println("Print results: " + results.getString("id"));
			// }
//            myStmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// find manufacturer names from database, replace possibleManufacturersID with
	// actual names
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

	// retrives all items matching user request, calls other methods to calculate
	// cheapest option
	public void evaluvateRequest() throws SQLException {
		if (category.equals("chair")) {
			ArrayList<Chair> list = new ArrayList<Chair>();
			while (results.next()) {
				Chair temp = new Chair(results.getString("ID"), strToInt(results.getString("Legs")),
						strToInt(results.getString("Arms")), strToInt(results.getString("Seat")),
						strToInt(results.getString("Cushion")), results.getInt("Price"));
				list.add(temp);
			}
			findManuID(possibleManufacturersLamp);
			Chair best = Chair.processRequest(list, amount);
			this.items = best.getIDs();
			this.itemPrice = best.getPrice();
			
		} else if (category.equals("desk")) {
			ArrayList<Desk> list = new ArrayList<Desk>();
			while (results.next()) {
				Desk temp = new Desk(results.getString("ID"), strToInt(results.getString("Legs")),
						strToInt(results.getString("Top")), strToInt(results.getString("Drawer")),
						results.getInt("Price"));
				list.add(temp);
			}
			findManuID(possibleManufacturersLamp);
			Desk best = Desk.processRequest(list, amount);
			this.items = best.getIDs();
			this.itemPrice = best.getPrice();
		} else if (category.equals("filing")) {
			ArrayList<Filing> list = new ArrayList<Filing>();
			while (results.next()) {
				Filing temp = new Filing(results.getString("ID"), strToInt(results.getString("Rails")),
						strToInt(results.getString("Cabinet")), strToInt(results.getString("Drawers")),
						results.getInt("Price"));
				list.add(temp);
			}
			findManuID(possibleManufacturersLamp);
			Filing best = Filing.processRequest(list, amount);
			this.items = best.getIDs();
			this.itemPrice = best.getPrice();
		} else if (category.equals("lamp")) {
			ArrayList<Lamp> list = new ArrayList<Lamp>();
			while (results.next()) {
				Lamp temp = new Lamp(results.getString("ID"), strToInt(results.getString("Base")),
						strToInt(results.getString("Bulb")), results.getInt("Price"));
				list.add(temp);
			}
			findManuID(possibleManufacturersLamp);
			Lamp best = Lamp.processRequest(list, amount);
			this.items = best.getIDs();
			this.itemPrice = best.getPrice();
		}
		checkIfOrderFilled();
		results.close();
	}

	public int strToInt(String check) {
		if (check.equals("Y")) {
			return 1;
		} else {
			return 0;
		}
	}

	// updates database by deleting items being bought
	public void updateDatabase() {
		String query = null;
		PreparedStatement myStmt = null;
		try {
			if (category.equals("chair"))
				query = "DELETE FROM chair WHERE ID = ?";
			else if (category.equals("desk"))
				query = "DELETE FROM desk WHERE ID = ?";
			else if (category.equals("lamp"))
				query = "DELETE FROM lamp WHERE ID = ?";
			else if (category.equals("filing"))
				query = "DELETE FROM filing WHERE ID = ?";
			for (int i = 0; i < items.size(); i++) {
				myStmt = dbConnect.prepareStatement(query);
				myStmt.setString(1, items.get(i));
				myStmt.execute();
			}
			myStmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// makes sure an order can be filled, outputs message if not
	public void checkIfOrderFilled() {
		if (items.equals(null)) {
			String output = "";
			output += "Order cannot be filled based on current inventory. Suggested manufacturers are ";
			if (category.equals("chair"))
				for (int i = 0; i < possibleManufacturersChair.length; i++) {
					output += possibleManufacturersChair[i];
					output += ", ";
				}
			if (category.equals("desk"))
				for (int i = 0; i < possibleManufacturersDesk.length; i++) {
					output += possibleManufacturersDesk[i];
					output += ", ";
				}
			if (category.equals("lamp"))
				for (int i = 0; i < possibleManufacturersLamp.length; i++) {
					output += possibleManufacturersLamp[i];
					output += ", ";
				}
			if (category.equals("filing"))
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
		for (int i = 0; i < items.size(); i++) {
			System.out.print(items.get(i));
			if (i != items.size() - 1)
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
			for (int i = 0; i < items.size(); i++)
				fo.write("ID: " + items.get(i) + "\n");
			fo.write("\nTotal Price: $" + itemPrice);
			fo.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
