import java.sql.*;
import java.io.*;
import java.util.*;

/*
* Inventory is a class that uses the SQL inventory database to process the
* the user requested order for a piece of furniture, specified by category and type. 
*/

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

	/*
	 * getUserRequest is a a method with no return type and no arguments that
	 * prompts the user for input and uses a BufferedReader to to read in the user
	 * request. The user input is then passed to the AnalyzeEntry method to be
	 * parsed.
	 */
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

	/*
	 * getDBURL is a method with no arguments that returns the String DBURL that
	 * connects to mySQL
	 */
	public String getDBURL() {
		return DBURL;
	}

	/*
	 * getUSERNAME is a method with no arguments that returns the String USERNAME.
	 * This is the name of the current MySQL user
	 */
	public String getUSERNAME() {
		return USERNAME;
	}

	/*
	 * getPASSWORD is a method with no arguments that returns the String PASSWORD.
	 * This is the password to the respective mySQL User
	 */
	public String getPASSWORD() {
		return PASSWORD;
	}

	/*
	 * initializeConnection is a method with no arguments and no return type that
	 * creates a connection from the program to the SQL database
	 */
	public void initializeConnection() {
		try {
			dbConnect = DriverManager.getConnection(this.DBURL, this.USERNAME, this.PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/*
	* close is a method that has no arguments and no return type. 
	* close closes the connection to the SQL database.
	*/
	public void close() {
		try {
			results.close();
			dbConnect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * AnalyzeEntry is a method that takes in the user inputted String, entry as an
	 * argument and parses it to determine the category, type of furniture, and
	 * amount that the user has requested. This method has no return type.
	 */
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

	/*
	 * toTitleCase is a method that takes one String as an argument, converts it to
	 * title case and returns it 
	 */
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

	/*
	 * pullData has no arguments and no return type. method loads in all furniture
	 * belonging to the respective category and type into a ResultSet
	 */
	public void pullData() {
		String query = null;
		try {
//			if (category.equals("chair"))
//				query = "SELECT * FROM Chair WHERE Type = ?";
//			else if (category.equals("desk"))
//				query = "SELECT * FROM Desk WHERE Type = ?";
//			else if (category.equals("lamp"))
//				query = "SELECT * FROM Lamp WHERE Type = ?";
//			else if (category.equals("filing"))
			query = "SELECT * FROM $tableName WHERE Type = ?";
			query = query.replace("$tableName", category);
			PreparedStatement myStmt = dbConnect.prepareStatement(query);
			myStmt.setString(1, type);
			results = myStmt.executeQuery();
			// while (results.next()) {
			// System.out.println("Print results: " + results.getString("id"));
			// }
			//myStmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * findManuID takes in a String array of manufacturer ID's as an argument and
	 * replaces the contents of that string with the actual names of the respective
	 * manufacturers in the same order. This method returns nothing.
	 * 
	 */
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

	/*
	 * evaluateRequest is a method with no arguments and no return type. method goes
	 * through the results ResultSet and saves all the properties of each piece of
	 * furniture to their own String arrays that are added to a the resultList
	 * LinkedList
	 */
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

	/*
	 * updateDatabase is a method that has no arguments and no returns nothing This
	 * method updates the database by deleting the items that were purchased.
	 */
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

	/*
	 * checkIfOrderFilled is a method with no arguments that checks to see if it is
	 * possible to fill the order. if it cannot be fulfilled it outputs a message.
	 * returns nothing
	 */
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

	/*
	 * write is a method with no arguments that writes the order details to a file
	 * called orderForm.txt. no return type.
	 */
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
