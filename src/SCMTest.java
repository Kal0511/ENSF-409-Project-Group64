import org.junit.Test;

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
        assertTrue("expected category does not match actual category", expectedCategory.equals(inventory.getCategory()));
        assertTrue("expected type does not match actual type", expectedType.equals(inventory.getType()));
        assertTrue("expected amount does not match actual amount", expectedAmount == inventory.getAmount());

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
    // tests if evaluate requests
    //
    public void testEvaluateRequest1() throws SQLException {
        Inventory inventory = new Inventory();
        // Create a user request
        inventory.addChair("C9000", "Mesh", "Y", "Y", "Y", "N", 20, "005");
        inventory.addChair("C9100", "Mesh", "N", "Y", "Y", "Y", 20, "005");
        inventory.addChair("C9200", "Mesh", "Y", "N", "N", "Y", 20, "005");

        String testEntry = "mesh chair, 2";

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

//    @Test
//    // Constructor created with one argument
//    // Use setFileName, which should replace the stored value
//    public void testConstructor1SetFileName1GetFileName() {
//        String origfn = "thefilename";
//        String newfn = "differentfile";
//        String expected = addPath(newfn);
//        ENSFStorage storage = new ENSFStorage(origfn);
//
//        // Replace fileName, see if it was replaced
//        storage.setFileName(newfn);
//        String got = storage.getFileName();
//        assertEquals("setFileName() didn't replace stored FileName", expected, got);
//    }
//
//    @Test
//    // Constructor created with zero arguments
//    // writeFile() with 1 argument
//    // File does not already exist
//    public void testConstructor0writeFile1File0() throws Exception {
//        ENSFStorage storage = new ENSFStorage();
//
//        // Add some lines of data
//        String[] wOwen = {
//                "In all my dreams before my helpless sight",
//                "He plunges at me, guttering, choking, drowning."
//        };
//        for (String val : wOwen) { // Add some lines of data
//            storage.addDataElement(val);
//        }
//
//        // Write out to named file. The directory name should have been appended when
//        // the filename was provided.
//        storage.writeFile(FILE);
//
//        // Read in the data file, ensure all lines match expectations
//        String[] ret = readFile(addPath(FILE));
//        boolean match = true;
//        for (int i = 0; i < ret.length; i++) {
//            if (ret[i].equals(wOwen[i]) == false) {
//                match = false;
//            }
//        }
//        assertTrue("Empty file incorrectly written (zero arg constructor)", match);
//    }
//
//    @Test
//    // cleanUp() method removes the data file and directory
//    public void testCleanUp() throws Exception {
//        String[] original = writeTestData();
//
//        // Create the constructor, delete the file
//        ENSFStorage storage = new ENSFStorage(FILE);
//        storage.cleanUp();
//
//        // Determine if file exists - it should not
//        File fn = new File(addPath(FILE));
//        assertFalse("File stored as fileName was not deleted", fn.exists());
//    }
//
//    @Test
//    // Check method readFile() with 0 arguments, fileName already specified
//    public void testReadFile0() throws Exception {
//        String[] original = writeTestData();
//        ENSFStorage storage = new ENSFStorage(FILE);
//        storage.readFile();
//        String[] check = storage.asStringArray();
//
//        assertTrue("Reading in the file with no arguments failed", Arrays.equals(original, check));
//    }
//
//    @Test
//    // Check method readFile() with 1 argument
//    public void testReadFile1() throws Exception {
//        String[] original = writeTestData();
//
//        ENSFStorage storage = new ENSFStorage();
//        storage.readFile(FILE);
//        String[] check = storage.asStringArray();
//        assertTrue("readFile() with one argument failed", Arrays.equals(original, check));
//    }
//
//    @Test
//    // Read in file, modify file. File should be automatically written
//    public void testModifyFile() throws Exception {
//        String[] original = writeTestData();
//
//        ENSFStorage storage = new ENSFStorage();
//        storage.readFile(FILE);
//
//        // Data we will be inserting
//        String[] tmp = {
//                "Rare pears and greengages,",
//                "Wild free-born cranberries,"
//        };
//
//        // Create a new array of the data we expect to get
//        // We are adding one element at the end, and overwriting the first element
//        String[] expected = new String[original.length + 1];
//        for (int i = 0; i < original.length; i++) {
//            expected[i] = original[i];
//        }
//        expected[0] = tmp[0];
//        expected[original.length] = tmp[1];
//
//        // Overwrite the elements. This should automatically write them to the file.
//        storage.addDataElement(tmp[0], 0);
//        storage.addDataElement(tmp[1]);
//
//        // Check if the data in the file matches our expectations
//        String[] check = readFile(addPath(FILE));
//        assertTrue("addDataElement() automatically writes when data is changed on existing file", Arrays.equals(expected, check));
//    }
//
//
//
//    /* Tests involving expected System.exit()
//     *  ExpectedSystemExit is provided by org.junit.contrib.java.lang.system.ExpectedSystemExit
//     *  which can be downloaded as a .jar from https://stefanbirkner.github.io/system-rules/
//     *  or found in the provided lib directory.
//     *  Store together with the hamcrest and junit jar files, and include as part of javac/java calls:
//     *  .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:lib/system-rules-1.19.0.jar (Mac & Linux)
//     *  .;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/system-rules-1.19.0.jar (Windows)
//     */
//
//    @Rule
//    // Handle System.exit() status
//    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
//
//    @Test
//    // If file already exists, we cannot add a line longer than the longest existing line
//    public void testEnforcedLongestLine() throws Exception {
//        String[] original = writeTestData();
//
//        ENSFStorage storage = new ENSFStorage();
//        storage.readFile(FILE);
//
//        // Try to add a really long line
//        exit.expectSystemExitWithStatus(1);
//        storage.addDataElement("A really long line which far exceeds the number of characters in the existing longest line");
//    }
//
//    @Test
//    // Constructor created with zero arguments
//    // addDataElement with 2 arguments and illegal position
//    // Should give System.err message and exit(1)
//    public void testConstructor0Add2TooHighIndex() {
//        ENSFStorage storage = new ENSFStorage();
//        exit.expectSystemExitWithStatus(1);
//
//        // Add data to fill elements 0 and 1
//        storage.addDataElement("Maybe January light will consume");
//        storage.addDataElement("My heart with its cruel");
//
//        // Try to add out of range, at element 3. We expect a system exit.
//        exit.expectSystemExit();
//        storage.addDataElement("Ray, stealing my key to true calm.", 3);
//    }
//
//    @Test
//    // Constructor created with zero arguments
//    // addDataElement with 2 arguments and illegal position
//    public void testConstructor0Add2NegativeIndex() {
//        ENSFStorage storage = new ENSFStorage();
//
//        // Try to add out of range, at element -1. We expect a system exit.
//        exit.expectSystemExitWithStatus(1);
//        storage.addDataElement("But soft! What light through yonder window breaks?", -1);
//    }
//
//    @Test
//    // Constructor created with zero arguments
//    // readFile() called with zero arguments
//    public void testConstructor0ReadFile0() {
//        ENSFStorage storage = new ENSFStorage();
//
//        // File to read is never specified. We expect a system exit.
//        exit.expectSystemExitWithStatus(1);
//        storage.readFile();
//    }
//
//
//
//
//    /*
//     *  Pre- and Post-test processes
//     */
//
//    @Before
//    public void start() {
//        removeAllData(DIR);
//    }
//
//    @After
//    public void end() {
//        removeAllData(DIR);
//    }
//
//    /*
//     *  Utility methods to perform common routines
//     */
//
//    // Write data to file
//    public void writeFile(String[] data) throws Exception {
//        BufferedWriter file = null;
//        File directory = new File(DIR);
//
//        // Create directory if it doesn't exist
//        if (!directory.exists()) {
//            directory.mkdir();
//        }
//
//        String fn = addPath(FILE);
//        file = new BufferedWriter(new FileWriter(fn));
//
//        for (String txt : data) {
//            file.write(txt, 0, txt.length());
//            file.newLine();
//        }
//        file.close();
//    }
//
//    // Remove directory or file, after determining the absolute path
//    public void removeAllData(String file) {
//        // Get the directory the program was called from and append the provided file/dir
//        String absolute = System.getProperty("user.dir");
//        File abs = new File(absolute);
//        File path = new File(abs, file);
//        removeAllData(path);
//    }
//
//    // Remove directory or file, given File obj with absolute path
//    public void removeAllData(File path) {
//        // If there are files in the directory, we have to delete them first
//        if (path.isDirectory()) {
//            // Get all files in the directory
//            File[] files = path.listFiles();
//
//            // Recursively delete all files/subdirs
//            if (files != null) {
//                for (File f : files) {
//                    removeAllData(f);
//                }
//            }
//        }
//
//        // Plain file or empty directory
//        path.delete();
//    }
//
//    // Add a directory path to a file
//    public String addPath(String file) {
//        File path = new File(DIR);
//        File full = new File(path, file);
//        return full.getPath();
//    }
//
//    // Read in a specified file, given path+filename
//    public String[] readFile(String fileAndPath) throws Exception {
//        BufferedReader file = new BufferedReader(new FileReader(fileAndPath));
//        String tmp = new String();
//        ArrayList<String> contents = new ArrayList<String>();
//
//        while ((tmp = file.readLine()) != null) {
//            contents.add(tmp);
//        }
//
//        file.close();
//        return contents.toArray(new String[contents.size()]);
//    }
//
//    public String[] writeTestData() throws Exception {
//        // Create some data and write it to the file
//        String[] cRossetti = {
//                "Apples and quinces,",
//                "Lemons and oranges,",
//                "Plump unpeck’d cherries,",
//                "Melons and raspberries,",
//                "Bloom-down-cheek’d peaches,",
//                "Swart-headed mulberries,"
//        };
//        writeFile(cRossetti);
//        return cRossetti;
//    }
//
}

