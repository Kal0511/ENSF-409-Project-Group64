import org.junit.*;

import static org.junit.Assert.*;

import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import java.util.*;
import java.sql.*;

public class SCMTest {

	@Test
	// test if analyzeEntry() parses user input correctly
	public void testAnalyzeEntry() {
		Inventory inventory = new Inventory();

		// test entry
		String testEntry = "executive chair, 3";

		inventory.analyzeEntry(testEntry);

		String expectedCategory = "chair";
		String expectedType = "executive";
		int expectedAmount = 3;
		assertTrue("expected category does not match actual category",
				expectedCategory.equals(inventory.getCategory()));
		assertTrue("expected type does not match actual type", expectedType.equals(inventory.getType()));
		assertTrue("expected amount does not match actual amount", expectedAmount == inventory.getAmount());

	}

	@Test
	// test if findManuID() assigns manufacturer IDs correctly
	public void testfindManuID() throws SQLException {
		Inventory inventory = new Inventory();

		// set string array to all Manufacturer IDs
		String[] x = { "001", "002", "003", "004", "005" };
		String[] result = { "Academic Desks", "Office Furnishings", "Chairs R Us", "Furniture Goods",
				"Fine Office Supplies" };

		// use findManuID to test if it does what is desired
		inventory.findManuID(x);
		assertTrue("Adding data elements and retrieving complete list with getDataElements failed",
				Arrays.equals(x, result));
	}

	@Test
	// test if pullData() retrieves the correct data
	public void testPullData() throws SQLException {
		Inventory inventory = new Inventory();

		// add items
		inventory.addChair("C0010", "Test5", "Y", "N", "N", "N", 10, "005");
		inventory.addChair("C0011", "Test5", "N", "Y", "N", "N", 10, "005");
		inventory.addChair("C0012", "Test5", "N", "N", "Y", "N", 10, "005");
		inventory.addChair("C0013", "Test5", "N", "N", "N", "Y", 10, "005");

		// test input
		String testEntry = "Test5 chair, 1";

		String[] expected = { "C0010", "C0011", "C0012", "C0013" };
		String[] got = new String[4];

		inventory.analyzeEntry(testEntry);
		inventory.pullData();

		int i = 0;
		while (inventory.getResults().next()) {
			got[i] = inventory.getResults().getString("ID");
			i++;
		}
		assertTrue("data retrieved from database is not what was expected", Arrays.equals(expected, got));

		// removing added items from database for testing simplicity
		inventory.removeChair("C0010");
		inventory.removeChair("C0011");
		inventory.removeChair("C0012");
		inventory.removeChair("C0013");
	}

	@Test
	// test if updateDatabase() correctly updates the database
	public void testUpdateDatabase() throws SQLException {
		Inventory inventory = new Inventory();

		// add item
		inventory.addChair("C0009", "Test4", "Y", "Y", "Y", "Y", 50, "005");

		String testEntry = "Test4 chair, 1";
		inventory.analyzeEntry(testEntry);
		inventory.pullData();
		inventory.evaluateRequest();
		inventory.updateDatabase();
		assertTrue("Item has not been removed from database", !inventory.itemExists("C0009", "chair"));
	}

	@Test
	// tests if evaluateRequest() chooses correct pieces for requested item
	public void testEvaluateRequest1() throws SQLException {
		Inventory inventory = new Inventory();
		// Create a user request
		inventory.addChair("C0000", "Test1", "Y", "Y", "Y", "N", 20, "005");
		inventory.addChair("C0001", "Test1", "N", "Y", "Y", "Y", 20, "005");
		inventory.addChair("C0002", "Test1", "Y", "N", "N", "Y", 20, "005");

		String testEntry = "test1 chair, 2";

		ArrayList<String> expected = new ArrayList<String>();
		expected.add("C0000");
		expected.add("C0001");
		expected.add("C0002");

		// Try using testEntry as input check that there is no possible combination of
		// items to fill request
		inventory.analyzeEntry(testEntry);
		inventory.pullData();
		inventory.evaluateRequest();
		ArrayList<String> got = inventory.getItem().IDs;

		assertEquals("Evaluate request was incorrect", expected, got);
		inventory.removeChair("C0000");
		inventory.removeChair("C0001");
		inventory.removeChair("C0002");
	}

	@Test
	// tests if evaluateRequest() chooses correct pieces for requested item
	public void testEvaluateRequest2() throws SQLException {
		Inventory inventory = new Inventory();
		// Add chairs to use for testing
		inventory.addChair("C0003", "Test2", "Y", "Y", "Y", "N", 500, "005");
		inventory.addChair("C0004", "Test2", "N", "N", "N", "Y", 500, "005");
		inventory.addChair("C0005", "Test2", "Y", "N", "N", "N", 50, "005");
		inventory.addChair("C0006", "Test2", "N", "Y", "Y", "Y", 50, "005");

		// Create a user request
		String testEntry = "test2 chair, 1";

		int expected = 100;

		// Try using testEntry as input
		inventory.analyzeEntry(testEntry);
		inventory.pullData();
		inventory.evaluateRequest();
		int got = inventory.getItem().totalPrice;

		assertEquals("Evaluate request was incorrect", expected, got);

		// removing added chairs from database for testing simplicity
		inventory.removeChair("C0003");
		inventory.removeChair("C0004");
		inventory.removeChair("C0005");
		inventory.removeChair("C0006");
	}

	@Test
	// tests if evaluateRequest() chooses correct pieces for requested item
	public void testEvaluateRequest3() throws SQLException {
		Inventory inventory = new Inventory();

		inventory.addChair("C0007", "Test3", "N", "Y", "Y", "Y", 50, "005");

		String testEntry = "Test3 chair, 1";

		// Try using testEntry as input
		inventory.analyzeEntry(testEntry);
		inventory.pullData();
		inventory.evaluateRequest();

		// order should not be filled therefore inventory.item should be null
		assertNull("Item is not null", inventory.getItem());

		// removing added chairs from database for testing simplicity
		inventory.removeChair("C0007");
	}

	@Test
	public void testRequestTooManyItems() throws SQLException {
		Inventory inventory = new Inventory();

		// Create a user request
		String testEntry = "executive chair, 100000000";

		// Try using testEntry as input check that there is no possible combination of
		// items to fill request
		inventory.analyzeEntry(testEntry);
		inventory.pullData();
		inventory.evaluateRequest();

		// order should not be filled therefore inventory.item should be null
		assertNull("Requesting too many items did not return an empty item list", inventory.getItem());
	}

	@Rule
	// Handle System.exit() status
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Test
	// test if asking for invalid category of item will end program accordingly
	public void testInvalidCategory() {
		Inventory inventory = new Inventory();

		// request invalid category item
		String testEntry = "executive table, 2";

		exit.expectSystemExitWithStatus(1);

		inventory.analyzeEntry(testEntry);
	}

	@Test
	// test if invalid input format will end program accordingly
	public void testInvalidInput() {
		Inventory inventory = new Inventory();

		// request invalid category item
		String testEntry = "2 mesh chairs";

		exit.expectSystemExitWithStatus(1);

		inventory.analyzeEntry(testEntry);
	}
}
/*
 * Tests involving expected System.exit() ExpectedSystemExit is provided by
 * org.junit.contrib.java.lang.system.ExpectedSystemExit which can be downloaded
 * as a .jar from https://stefanbirkner.github.io/system-rules/ or found in the
 * provided lib directory. Store together with the hamcrest and junit jar files,
 * and include as part of javac/java calls:
 * .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:lib/system-rules-1.19.0.jar
 * (Mac & Linux)
 * .;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/system-rules-1.19.0.jar
 * (Windows)
 */
