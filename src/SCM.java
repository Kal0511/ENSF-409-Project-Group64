import java.sql.SQLException;

/**
 * SCM is a program which takes a user request as input from the terminal window
 * and searches mysql database for requested items, it outputs a order form .txt
 * file if the order can be filled, otherwise outputs suggested manufacturers
 * for the requested item.
 */
public class SCM {

	/**
	 * This initializes the Inventory class and calls all required methods to
	 * implement useful functionality.
	 *
	 * @param args Optional command-line argument
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {
		Inventory test = new Inventory();
		test.initializeConnection();
		test.getUserRequest();
		test.pullData();
		test.evaluateRequest();
		test.updateDatabase();
		test.write();
	}
}
