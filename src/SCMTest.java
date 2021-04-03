import org.junit.*;

import static org.junit.Assert.*;

import java.util.*;
import java.sql.*;

public class SCMTest {

    @Test
    public void testAnalyzeEntry() {
        Inventory inventory = new Inventory();

        String testEntry = "executive chair, 3";

        inventory.analyzeEntry(testEntry);
        // Retrieve list, convert to array, see if it matches
        String expectedCategory = "chair";
        String expectedType = "executive";
        int expectedAmount = 3;
        assertTrue("expected category does not match actual category",
                expectedCategory.equals(inventory.getCategory()));
        assertTrue("expected type does not match actual type",
                expectedType.equals(inventory.getType()));
        assertTrue("expected amount does not match actual amount",
                expectedAmount == inventory.getAmount());

    }

    @Test
    public void testfindManuID() throws SQLException {
        Inventory inventory = new Inventory();

        // set string array to all Manufacturer IDs
        String[] x = {"001", "002", "003", "004", "005"};
        String[] result = {"Academic Desks", "Office Furnishings", "Chairs R Us", "Furniture Goods",
                "Fine Office Supplies"};

        // use findManuID to test if it does what is desired
        inventory.findManuID(x);
        assertTrue("Adding data elements and retrieving complete list with getDataElements failed",
                Arrays.equals(x, result));
    }

    @Test
    public void testRequestTooManyItems() throws SQLException {
        Inventory inventory = new Inventory();

        // Create a user request
        String testEntry = "executive chair, 100";
        // Try using testEntry as input check that there is no possible combination of items to fill request
        inventory.analyzeEntry(testEntry);
        inventory.pullData();
        inventory.evaluateRequest();
        assertNull("Requesting too many items did not return an empty item list", inventory.getItem());
    }

    @Test
    // tests if evaluate request chooses correct pieces for requested item
    public void testEvaluateRequest1() throws SQLException {
        Inventory inventory = new Inventory();
        // Create a user request
        inventory.addChair("C9000", "Test1", "Y", "Y", "Y", "N",
                20, "005");
        inventory.addChair("C9100", "Test1", "N", "Y", "Y", "Y",
                20, "005");
        inventory.addChair("C9200", "Test1", "Y", "N", "N", "Y",
                20, "005");

        String testEntry = "test1 chair, 2";

        ArrayList<String> expected = new ArrayList<String>();
        expected.add("C9000");
        expected.add("C9100");
        expected.add("C9200");

        // Try using testEntry as input check that there is no possible combination of items to fill request
        inventory.analyzeEntry(testEntry);
        inventory.pullData();
        inventory.evaluateRequest();
        ArrayList<String> got = inventory.getItem().IDs;

        assertEquals("Evaluate request was incorrect", expected, got);
        inventory.updateDatabase();
    }

    @Test
    // tests if evaluate request chooses cheapest combination of pieces for requested item
    public void testEvaluateRequest2() throws SQLException {
        Inventory inventory = new Inventory();
        // Create a user request
        inventory.addChair("C9000", "Test2", "Y", "Y", "Y", "N",
                500, "005");
        inventory.addChair("C9100", "Test2", "N", "N", "N", "Y",
                500, "005");
        inventory.addChair("C9200", "Test2", "Y", "N", "N", "N",
                50, "005");
        inventory.addChair("C9300", "Test2", "N", "Y", "Y", "Y",
                50, "005");

        String testEntry = "test2 chair, 1";

        int expected = 100;

        // Try using testEntry as input
        inventory.analyzeEntry(testEntry);
        inventory.pullData();
        inventory.evaluateRequest();
        int got = inventory.getItem().totalPrice;

        assertEquals("Evaluate request was incorrect", expected, got);
        // removing added chairs from database for testing simplicity
        inventory.updateDatabase();
        inventory.pullData();
        inventory.evaluateRequest();
        inventory.updateDatabase();
    }
    
    @Test
    // tests if evaluate request chooses cheapest combination of pieces for requested item
    public void testInvalidType() throws SQLException {
        Inventory inventory = new Inventory();
       
        

        String testEntry = "noo chair, 1";

      
        exit.expectSystemExitWithStatus(1);

        // Try using testEntry as input
        inventory.analyzeEntry(testEntry);
        inventory.pullData();
        inventory.evaluateRequest();

        // removing added chairs from database for testing simplicity

    }
    
}
/* Tests involving expected System.exit()
 *  ExpectedSystemExit is provided by org.junit.contrib.java.lang.system.ExpectedSystemExit
 *  which can be downloaded as a .jar from https://stefanbirkner.github.io/system-rules/
 *  or found in the provided lib directory.
 *  Store together with the hamcrest and junit jar files, and include as part of javac/java calls:
 *  .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:lib/system-rules-1.19.0.jar (Mac & Linux)
 *  .;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/system-rules-1.19.0.jar (Windows)
 */
